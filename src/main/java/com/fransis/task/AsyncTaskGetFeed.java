package com.fransis.task;

import com.fransis.email.EmailSender;
import com.fransis.model.*;
import com.fransis.repository.FeedRepository;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.json.JsonArray;
import com.restfb.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by francisco on 9/4/16.
 */
public class AsyncTaskGetFeed implements Runnable{

    private String accessToken;

    private JsonObject groupFeeds;
    private FacebookClient facebookClient;

    private FeedRepository feedRepository;

    private Watcher watcher;
    private EmailSender sender;

    public AsyncTaskGetFeed(FeedRepository feedRepository, EmailSender emailSender, Watcher watcher) {
        this.accessToken = watcher.getUsername().getAccessToken();
        facebookClient = new DefaultFacebookClient( this.accessToken, Version.VERSION_2_7);
        this.feedRepository = feedRepository;
        this.watcher = watcher;
        this.sender = emailSender;
    }

    public void run() {
            for(FbGroup fbGroup: watcher.getGroups()) {
                groupFeeds = facebookClient.fetchObject("/" + fbGroup.getGroupId() + "/feed", JsonObject.class, Parameter.with("fields", "id,message,from"), Parameter.with("limit", 100));
                JsonArray data = groupFeeds.getJsonArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JsonObject feed = data.getJsonObject(i);
                    if (feed.has("message")) {
                        String message = feed.getString("message");
                        //System.out.println(message);
                        if (!feedRepository.exists(feed.getString("id"))) {
                            final List<FbFilter> list = new ArrayList<>();
                            boolean m = watcher.getFilters().stream().anyMatch(fbFilter -> {
                                boolean ret = message.contains(fbFilter.getValue());
                                if(ret){
                                    list.add(fbFilter);
                                }
                                return ret;
                            });
                            if (m) {
                                //Notificar
                                FbFeed fbMessage = new FbFeed(feed.getString("id"), feed.getString("message"));
                                feedRepository.saveAndFlush(fbMessage);
                                System.out.println("------------------------------------------");
                                System.out.println("Alerta: " + message);
                                System.out.println("------------------------------------------");
                                StringBuilder html = new StringBuilder();
                                html.append("El post id <br><b>" + fbMessage.getId());
                                html.append("</b><br>Contiene el siguiente texto<br><b>");
                                html.append(fbMessage.getMessage() + "</b><br>");
                                html.append("El post fue realizado por <b>");
                                JsonObject from = feed.getJsonObject("from");
                                html.append(from.getString("name") + "</b><br>");
                                html.append("Las palabras encontadas son: <br><b>");
                                for(FbFilter f: list){
                                    html.append(f.getValue() + "<br>");
                                }
                                html.append("</b>");
                                for (Email dst : watcher.getEmails()) {
                                    sender.send("no-reply@yomeanimoyvos.com", "Alertas Yo me animo", dst.getEmail(), dst.getDescription(), "Alertas de grupo " + fbGroup.getGroupName(), html.toString());
                                }
                            }
                        }
                    }
                }
            }
    }
}
