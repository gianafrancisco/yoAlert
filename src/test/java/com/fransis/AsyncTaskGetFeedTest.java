package com.fransis;

import com.fransis.email.EmailSender;
import com.fransis.model.FbFeed;
import com.fransis.model.Watcher;
import com.fransis.repository.FeedRepository;
import com.fransis.task.AsyncTaskGetFeed;
import com.restfb.DefaultFacebookClient;
import com.restfb.Parameter;
import com.restfb.json.JsonArray;
import com.restfb.json.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Created by francisco on 19/10/2016.
 */
public class AsyncTaskGetFeedTest {

    private AsyncTaskGetFeed feed;
    private DefaultFacebookClient facebookMock;
    private FeedRepository feedRepository;
    private EmailSender emailSender;
    private Watcher watcher;

    @Before
    public void setUp() throws Exception {

        emailSender = new EmailSender() {
            @Override
            public boolean send(String from, String fromName, String to, String toName, String subject, String body) {
                return true;
            }
        };
    }

    @Test
    public void ignore_case() throws Exception {
        facebookMock = new FacebookMock("Quiero vernder mi SeGuRo", "Elver Galarga");
        feed = new AsyncTaskGetFeed(feedRepository, emailSender, watcher, facebookMock);
        feed.run();
    }
}


class FacebookMock extends DefaultFacebookClient {

    private String message;
    private String name;

    public FacebookMock(String message, String name){
        this.message = message;
        this.name = name;
    }

    @Override
    public <T> T fetchObject(String s, Class<T> aClass, Parameter... parameters) {
        JsonObject reply = new JsonObject();
        JsonArray data = new JsonArray();
        JsonObject feed = new JsonObject();
        feed.put("message", message);
        feed.put("id", "XXXXXXXXXXXXXXXXXXXXX");
        JsonObject from = new JsonObject();
        from.put("name", name);
        feed.put("from", from);
        data.put(feed);
        reply.put("data", data);
        return (T)reply;
    }
}
