package com.fransis.email;

/**
 * Created by francisco on 9/25/16.
 */
public interface EmailSender {
    boolean send(String from, String fromName, String to, String toName, String subject, String body);
}
