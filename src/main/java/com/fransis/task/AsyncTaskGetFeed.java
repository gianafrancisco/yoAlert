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

/**
 * Created by francisco on 9/4/16.
 */
public class AsyncTaskGetFeed implements Runnable{

    private String accessToken;

    private JsonObject groupFeeds;
    private FacebookClient facebookClient;

    private FeedRepository feedRepository;

    private FbGroup fbGroup;
    private FbUsername fbUsername;
    private Watcher watcher;
    private EmailSender sender;

    public AsyncTaskGetFeed(FeedRepository feedRepository, EmailSender emailSender, Watcher watcher, FbUsername fbUsername) {
        this.fbGroup = watcher.getGroups().iterator().next();
        this.fbUsername = fbUsername;
        this.accessToken = fbUsername.getAccessToken();
        facebookClient = new DefaultFacebookClient( this.accessToken, Version.VERSION_2_7);
        this.feedRepository = feedRepository;
        this.watcher = watcher;
        this.sender = emailSender;
    }

    public void run() {
            groupFeeds = facebookClient.fetchObject("/" + fbGroup.getGroupId() + "/feed", JsonObject.class, Parameter.with("fields","id,message,from"), Parameter.with("limit", 1000));
            JsonArray data = groupFeeds.getJsonArray("data");
            for(int i = 0; i < data.length(); i++){
                JsonObject feed = data.getJsonObject(i);
                if(feed.has("message")) {
                    String message = feed.getString("message");
                    System.out.println(message);
                    if(!feedRepository.exists(feed.getString("id"))){
                        boolean m = watcher.getFilters().stream().anyMatch(fbFilter -> message.contains(fbFilter.getValue()));
                        if(m){
                            //Notificar
                            FbFeed fbMessage = new FbFeed(feed.getString("id"), feed.getString("message"));
                            feedRepository.saveAndFlush(fbMessage);
                            System.out.println("Alerta: " + message);
                            Email dst = watcher.getEmails().iterator().next();
                            StringBuilder html = new StringBuilder();
                            html.append("El post id " + fbMessage.getId());
                            html.append(" contiene el siguiente texto<br><b>");
                            html.append(fbMessage.getMessage() + "</b><br>");
                            html.append("El post fue realizado por <b>");
                            JsonObject from = feed.getJsonObject("from");
                            html.append(from.getString("name") + "</b>");
                            sender.send("no-reply@yomeanimoyvos.com", "Alertas Yo me animo", dst.getEmail(), dst.getDesc(), "Alertas de grupo " + fbGroup.getGroupName(), html.toString());
                        }
                    }
                }
            }
    }
}
