package com.fransis.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fransis.email.EmailSender;
import com.fransis.model.*;
import com.fransis.repository.*;
import com.google.common.collect.Lists;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.json.JsonArray;
import com.restfb.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by francisco on 26/09/2016.
 */
@RestController
@RequestMapping("/wh")
@Component("WebhookController")
public class WebhookController {

    private static final Logger log = LoggerFactory.getLogger(WebhookController.class);

    private DefaultFacebookClient facebookClient;

    @Autowired
    private EmailSender sender;

    private List<String> emailsTo;

    public WebhookController() {
        String token = System.getProperties().getProperty("leads_token", "");
        String emails = System.getProperties().getProperty("emailTo", "");
        if(!"".equals(emails)){
            emailsTo = Lists.newArrayList(emails.split(","));
        }
        else {
            emailsTo = Collections.emptyList();
        }
        facebookClient = new DefaultFacebookClient(token, Version.VERSION_3_0);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> post(@RequestBody Update update) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(update);
        log.info(json);
        if(update.entry.size() > 0) {
            Entry entry = update.entry.get(0);
            if(entry.changes.size() > 0) {
                Change change = entry.changes.get(0);
                if (change.field.equals("leadgen")) {
                    Map<String, Long> leadMap = (Map<String, Long>) change.value;
                    log.info(String.valueOf(leadMap.get("leadgen_id")));
                    JsonObject data = facebookClient.fetchObject("/" + leadMap.get("leadgen_id"), JsonObject.class);

                    StringBuilder html = new StringBuilder();
                    html.append("Datos del Formulario<br>");

                    String createTime = data.get("created_time").asString();
                    html.append("<b>").append("Fecha: ").append(createTime).append("</b><br>");
                    JsonArray fieldData = data.get("field_data").asArray();
                    for(int i = 0; i<fieldData.size(); i++){
                        JsonObject field = fieldData.get(i).asObject();
                        html.append("<b>").append(field.get("name").asString()).append(": </b>");
                        JsonArray values = field.get("values").asArray();
                        for (int k = 0; k < values.size(); k++) {
                            html.append(values.get(k).asString());
                            if (k < values.size() - 1) {
                                html.append(", ");
                            }
                        }
                        html.append("<br>");
                    }

                    Object jsonPretty = objectMapper.readValue(data.toString(), Object.class);
                    String pretty = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonPretty);
                    log.info(pretty);
                    String subject = "Alertas de formulario " + leadMap.get("form_id");
                    //sender.send("no-reply@yomeanimoyvos.com", "Formularios YoMeAnimoYVos", "gianafrancisco@gmail.com", "Giana Francisco", subject, html.toString());
                    for(String emailTo: emailsTo){
                        sender.send("no-reply@yomeanimoyvos.com", "Alertas de formularios", emailTo, emailTo, subject, html.toString());
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> get(@RequestParam("hub.mode") String mode, @RequestParam("hub.verify_token") String verify_token, @RequestParam("hub.challenge") String challenge){
        if(mode.equals("subscribe") && verify_token.equals("yomeanimo_token") ){
            log.info("sibscribe challenge " + challenge);
            return ResponseEntity.ok(challenge);
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("");
    }

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public ResponseEntity<Void> get(){
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
