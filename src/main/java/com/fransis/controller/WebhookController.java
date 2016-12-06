package com.fransis.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fransis.email.EmailSender;
import com.fransis.model.*;
import com.fransis.repository.*;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
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

    public WebhookController() {
        String token = System.getProperties().getProperty("leads_token", "");
        facebookClient = new DefaultFacebookClient(token, Version.VERSION_2_7);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> post(@RequestBody Update update) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(update);
        log.info(json);
        if(update.entry.size() > 0) {
            Entry entry = update.entry.get(0);
            if(entry.changes.size() > 0) {
                Change change = entry.changes.get(0);
                if (change.field.equals("leadgen")) {
                    Map<String, Long> leadMap = (Map<String, Long>) change.value;
                    log.info(String.valueOf(leadMap.get("leadgen_id")));
                    JsonObject data = facebookClient.fetchObject("/" + leadMap.get("leadgen_id"), JsonObject.class);
                    //String pretty = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
                    log.info(data.toString());

                    StringBuilder html = new StringBuilder();
                    html.append("Datos del Formulario</b>");
                    html.append(data.toString());
                    html.append("</b>");

                    String subject = "Datos del formulario " + leadMap.get("form_id");
                    sender.send("no-reply@yomeanimoyvos.com", "Formularios YoMeAnimoYVos", "gianafrancisco@gmail.com", "Giana Francisco", subject, html.toString());
                }
            }
        }
        /*
        JsonObject oJson = objectMapper.readValue(json, JsonObject.class);
        if(oJson.getString("object").equals("page")){
            JsonObject entry = oJson.getJsonArray("entry").getJsonObject(0);
            JsonObject change = entry.getJsonArray("changes").getJsonObject(0);
            if(change.getString("field").equals("leadgen")){
                JsonObject value = change.getJsonObject("value");
                log.info(String.valueOf(value.getLong("leadgen_id")));
            }
        }*/
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
