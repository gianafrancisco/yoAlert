package com.fransis.email;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by francisco on 9/25/16.
 */
@Component("emailSender")
public class EmailSenderImpl implements EmailSender {

    private Mailin http;

    public EmailSenderImpl() {
        http = SendinBlue.getInstance().getMailin();
    }

    @Override
    public boolean send(String fromEmail, String fromName, String toEmail, String toName, String subject, String body) {

        Map<String, Object> mapData = new HashMap<>();

        Map<String, String> to = new HashMap<>();
        to.put(toEmail, toName);

        mapData.put("to", to);

        ArrayList<String> from = new ArrayList<>();
        from.add(fromEmail);
        from.add(fromName);

        mapData.put("from", from);

        mapData.put("subject", subject);

        mapData.put("html", body);

        String response = http.send_email(mapData);
        System.out.println(response);

        return response.contains("\"code\":\"success\"");
    }
}
