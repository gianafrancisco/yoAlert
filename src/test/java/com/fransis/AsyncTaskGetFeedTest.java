package com.fransis;

import com.fransis.email.EmailSender;
import com.fransis.model.*;
import com.fransis.repository.FeedRepository;
import com.fransis.task.AsyncTaskGetFeed;
import com.restfb.DefaultFacebookClient;
import com.restfb.Parameter;
import com.restfb.json.JsonArray;
import com.restfb.json.JsonObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by francisco on 19/10/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan("com.fransis")
@ContextConfiguration(classes = {MemoryDBConfig.class})
public class AsyncTaskGetFeedTest {

    private AsyncTaskGetFeed feed;
    private DefaultFacebookClient facebookMock;

    @Autowired
    private FeedRepository feedRepository;
    private EmailSenderMock emailSender;
    private Watcher watcher;

    @Before
    public void setUp() throws Exception {
        emailSender = new EmailSenderMock();
    }

    @Test
    public void match_ignore_case() throws Exception {

        watcher = new Watcher();
        FbGroup group = new FbGroup("1111111", "Test 1");
        watcher.getGroups().add(group);
        FbFilter filter = new FbFilter("seguro");
        watcher.getFilters().add(filter);
        watcher.getEmails().add(new Email("ggg@gm.com", "Admin"));
        watcher.setUsername(new FbUsername("usuario1", "ddddd", "123456789"));

        facebookMock = new FacebookMock("Quiero vernder mi SeGuRo", "Elver Galarga", "3333");
        feed = new AsyncTaskGetFeed(feedRepository, emailSender, watcher, facebookMock);
        feed.run();

        Assert.assertThat(feedRepository.findAll().size(), is(1));
        Assert.assertThat(emailSender.getEmailEnviados(), is(1));

    }

    @Test
    public void ignore_notified_when_admin_group() throws Exception {

        watcher = new Watcher();
        FbGroup group = new FbGroup("1111111", "Test 1");
        watcher.getGroups().add(group);
        FbFilter filter = new FbFilter("seguro");
        watcher.getFilters().add(filter);
        watcher.getEmails().add(new Email("ggg@gm.com", "Admin"));
        watcher.setUsername(new FbUsername("usuario1", "ddddd", "123456789"));

        facebookMock = new FacebookMock("Quiero vernder mi SeGuRo", "Elver Galarga", "123456789");
        feed = new AsyncTaskGetFeed(feedRepository, emailSender, watcher, facebookMock);
        feed.run();

        Assert.assertThat(feedRepository.findAll().size(), is(1));
        Assert.assertThat(emailSender.getEmailEnviados(), is(0));

    }

    @After
    public void tearDown() throws Exception {
        feedRepository.deleteAll();
    }
}


class FacebookMock extends DefaultFacebookClient {

    private String message;
    private String name;
    private String usuarioId;

    public FacebookMock(String message, String name, String usuarioId){
        this.message = message;
        this.name = name;
        this.usuarioId = usuarioId;
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
        from.put("id", usuarioId);
        feed.put("from", from);
        data.put(feed);
        reply.put("data", data);
        return (T)reply;
    }
}

class EmailSenderMock implements EmailSender {

    private int emailEnviados = 0;

    @Override
    public boolean send(String from, String fromName, String to, String toName, String subject, String body) {
        emailEnviados++;
        return true;
    }

    public int getEmailEnviados(){
        return emailEnviados;
    }
}