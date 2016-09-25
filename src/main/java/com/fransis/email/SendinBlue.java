package com.fransis.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * Created by francisco on 9/25/16.
 */
@Component()
public class SendinBlue {
    private static SendinBlue ourInstance = new SendinBlue();
    private Mailin mailin;

    private String key;

    public static SendinBlue getInstance() {
        return ourInstance;
    }

    private SendinBlue() {
        key = System.getProperties().getProperty("sendinblue_key","");
        mailin = new Mailin("https://api.sendinblue.com/v2.0", key, 5000);
    }

    public Mailin getMailin() {
        return mailin;
    }
}
