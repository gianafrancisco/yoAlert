package com.fransis.task;

import com.fransis.email.Mailin;
import com.fransis.model.FbFeed;
import com.fransis.model.FbUsername;
import com.fransis.model.FbGroup;
import com.fransis.repository.FeedRepository;
import com.fransis.repository.FilterRepository;
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

    private String groupId;
    private String accessToken;

    private JsonObject groupFeeds;
    private FacebookClient facebookClient;

    private FeedRepository feedRepository;
    private FilterRepository filterRepository;

    private FbGroup fbGroup;
    private FbUsername fbUsername;

    public AsyncTaskGetFeed(FeedRepository feedRepository, FilterRepository filterRepository, FbUsername fbUsername, FbGroup fbGroup) {
        this.groupId = fbGroup.getGroupId();
        this.accessToken = fbUsername.getAccessToken();
        facebookClient = new DefaultFacebookClient( this.accessToken, Version.VERSION_2_7);
        this.feedRepository = feedRepository;
        this.filterRepository = filterRepository;
        this.fbGroup = fbGroup;
        this.fbUsername = fbUsername;
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
                            feedRepository.saveAndFlush(new FbFeed(feed.getString("id"), feed.getString("message")));
                            System.out.println("Alerta: " + message);

                            /*Mailin http = new Mailin("https://api.sendinblue.com/v2.0", "your access key", 5000);   //Optional parameter: Timeout in MS
                            String str = http.get_account();
                            System.out.println(str);*/

                        }
                    }
                }
            }
    }
}
