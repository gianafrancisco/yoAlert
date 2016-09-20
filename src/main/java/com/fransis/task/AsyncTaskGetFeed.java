package com.fransis.task;

import com.fransis.repository.FeedRepository;
import com.fransis.repository.FilterRepository;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.json.JsonArray;
import com.restfb.json.JsonObject;
import com.sun.istack.internal.logging.Logger;

import java.util.regex.Pattern;

/**
 * Created by francisco on 9/4/16.
 */
public class AsyncTaskGetFeed implements Runnable{

    private String groupId;
    private String accessToken;

    private JsonObject groupFeeds;
    private FacebookClient facebookClient;

    private FeedRepository feedRepository;
    private FilterRepository filterRepository;

    public AsyncTaskGetFeed(String groupId, String accessToken, FeedRepository feedRepository, FilterRepository filterRepository) {
        this.groupId = groupId;
        this.accessToken = accessToken;
        facebookClient = new DefaultFacebookClient( this.accessToken, Version.VERSION_2_7);
        this.feedRepository = feedRepository;
        this.filterRepository = filterRepository;
    }

    public void run() {
            groupFeeds = facebookClient.fetchObject("/" + groupId + "/feed", JsonObject.class, Parameter.with("fields","id,message"), Parameter.with("limit", 1000));
            JsonArray data = groupFeeds.getJsonArray("data");
            for(int i = 0; i < data.length(); i++){
                JsonObject feed = data.getJsonObject(i);
                if(feed.has("message")) {
                    String message = feed.getString("message");
                    System.out.println(message);
                    if(!feedRepository.exists(feed.getString("id"))){
                        boolean m = filterRepository.findAll().stream().anyMatch(fbFilter -> {
                            return message.contains(fbFilter.getValue());
                        });
                        if(m){
                            //Notificar
                            System.out.println("Alerta: " + message);
                        }
                    }
                }
            }
    }
}
