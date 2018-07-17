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

    private String fromEmail = "no-reply@yomeanimoyvos.com";
    private String fromName = "Alertas Yo me animo";
    private String subjectPrefix = "Alertas de grupo ";
    private int rowsLimit = 10;

    public AsyncTaskGetFeed(FeedRepository feedRepository, EmailSender emailSender, Watcher watcher, DefaultFacebookClient facebookClient, int rowsLimit) {
        this.facebookClient = facebookClient;
        this.feedRepository = feedRepository;
        this.watcher = watcher;
        this.sender = emailSender;
        this.rowsLimit = rowsLimit;
    }

    public void run() {
            for(FbGroup fbGroup: watcher.getGroups()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("Grupo " + fbGroup.getGroupName());
                groupFeeds = facebookClient.fetchObject("/" + fbGroup.getGroupId() + "/feed", JsonObject.class, Parameter.with("fields", "id,message,from,permalink_url"), Parameter.with("limit", rowsLimit));
                JsonArray data = groupFeeds.get("data").asArray();
                log.info("rows:  " + data.size() + "/" + rowsLimit);
                for (int i = 0; i < data.size(); i++) {
                    JsonObject feed = data.get(i).asObject();
                    if (feed.get("message") != null) {
                        String message = feed.get("message").asString();
                        log.info("message id:  " + feed.get("id").asString());
                        log.info("message data:  " + message);
                        if (!feedRepository.exists(feed.get("id").asString())) {
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
                                FbFeed fbMessage = new FbFeed(feed.get("id").asString(), feed.get("message").asString());
                                //JsonObject from = feed.get("from").asObject();
                                feedRepository.saveAndFlush(fbMessage);
                                log.info("------------------------------------------");
                                log.info("Alerta: " + message);
                                log.info("------------------------------------------");
                                StringBuilder html = new StringBuilder();
                                html.append("El post id <br><b>" + fbMessage.getId());
                                html.append("</b><br>Contiene el siguiente texto<br><b>");
                                html.append(fbMessage.getMessage() + "</b><br>");
                                //html.append("El post fue realizado por <b>");
                                //html.append(from.get("name").asString() + "</b><br>");
                                if(feed.get("permalink_url") != null){
                                    String permalinkUrl = feed.get("permalink_url").asString();
                                    html.append("Link del post es " + permalinkUrl + "<br>");
                                    log.info("permalink_url " + permalinkUrl);
                                }
                                html.append("Las palabras encontadas son: <br><b>");
                                for(FbFilter f: list){
                                    html.append(f.getValue() + "<br>");
                                }
                                html.append("</b>");

                                //log.debug("From ID:" + from.get("id").asString());
                                log.info("FbUser ID:" + watcher.getUsername().getUsuarioId());
                                /*
                                if(!from.get("id").asString()
                                        .trim()
                                        .toLowerCase()
                                        .equals(watcher.getUsername().getUsuarioId().trim().toLowerCase())){
                                */
                                    for (Email dst : watcher.getEmails()) {
                                        String subject = subjectPrefix + fbGroup.getGroupName();
                                        sender.send(fromEmail, fromName, dst.getEmail(), dst.getDescription(), subject, html.toString());
                                    }
                                /*
                                }else{
                                    log.info("Admin post " + fbMessage.getId());
                                }
                                */
                            }
                        }
                    }
                }
            }
    }
}
