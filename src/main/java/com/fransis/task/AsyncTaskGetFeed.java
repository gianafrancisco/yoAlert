package com.fransis.task;

import com.fransis.email.EmailSender;
import com.fransis.model.*;
import com.fransis.repository.FeedRepository;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.json.JsonArray;
import com.restfb.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by francisco on 9/4/16.
 */
public class AsyncTaskGetFeed implements Runnable{

    private static final Logger log = LoggerFactory.getLogger(AsyncTaskGetFeed.class);

    private JsonObject groupFeeds;
    private FacebookClient facebookClient;
    private FeedRepository feedRepository;
    private Watcher watcher;
    private EmailSender sender;

    private final String fromEmail = "no-reply@yomeanimoyvos.com";
    private final String fromName = "Alertas Yo me animo";
    private final String subjectPrefix = "Alertas de grupo ";

    public AsyncTaskGetFeed(FeedRepository feedRepository, EmailSender emailSender, Watcher watcher, DefaultFacebookClient facebookClient) {
        this.facebookClient = facebookClient;
        this.feedRepository = feedRepository;
        this.watcher = watcher;
        this.sender = emailSender;
    }

    public void run() {
            for(FbGroup fbGroup: watcher.getGroups()) {
                groupFeeds = facebookClient.fetchObject("/" + fbGroup.getGroupId() + "/feed", JsonObject.class, Parameter.with("fields", "id,message,from,permalink_url"), Parameter.with("limit", 100));
                JsonArray data = groupFeeds.getJsonArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JsonObject feed = data.getJsonObject(i);
                    if (feed.has("message")) {
                        String message = feed.getString("message");
                        if (!feedRepository.exists(feed.getString("id"))) {
                            final List<FbFilter> list = new ArrayList<>();
                            boolean m = watcher.getFilters().stream().anyMatch(fbFilter -> {
                                boolean ret = message.toLowerCase().contains(fbFilter.getValue().toLowerCase());
                                if(ret){
                                    list.add(fbFilter);
                                }
                                return ret;
                            });
                            if (m) {
                                //Notificar
                                FbFeed fbMessage = new FbFeed(feed.getString("id"), feed.getString("message"));
                                JsonObject from = feed.getJsonObject("from");
                                feedRepository.saveAndFlush(fbMessage);
                                log.info("------------------------------------------");
                                log.info("Alerta: " + message);
                                log.info("------------------------------------------");
                                StringBuilder html = new StringBuilder();
                                html.append("El post id <br><b>" + fbMessage.getId());
                                html.append("</b><br>Contiene el siguiente texto<br><b>");
                                html.append(fbMessage.getMessage() + "</b><br>");
                                html.append("El post fue realizado por <b>");
                                html.append(from.getString("name") + "</b><br>");
                                if(feed.has("permalink_url")){
                                    String permalinkUrl = feed.getString("permalink_url");
                                    html.append("Link del post es " + permalinkUrl + "<br>");
                                    log.debug("permalink_url " + permalinkUrl);
                                }
                                html.append("Las palabras encontadas son: <br><b>");
                                for(FbFilter f: list){
                                    html.append(f.getValue() + "<br>");
                                }
                                html.append("</b>");

                                log.debug("From ID:" + from.getString("id"));
                                log.debug("FbUser ID:" + watcher.getUsername().getUsuarioId());
                                if(!from.getString("id")
                                        .trim()
                                        .toLowerCase()
                                        .equals(watcher.getUsername().getUsuarioId().trim().toLowerCase())){
                                    for (Email dst : watcher.getEmails()) {
                                        String subject = subjectPrefix + fbGroup.getGroupName();
                                        sender.send(fromEmail, fromName, dst.getEmail(), dst.getDescription(), subject, html.toString());
                                    }
                                }else{
                                    log.info("Admin post " + fbMessage.getId());
                                }
                            }
                        }
                    }
                }
            }
    }
}
