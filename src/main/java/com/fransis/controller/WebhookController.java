package com.fransis.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fransis.model.*;
import com.fransis.repository.*;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> post(@RequestBody Update update) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(update);
        log.info(json);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> get(@RequestParam("hub.mode") String mode, @RequestParam("hub.verify_token") String verify_token, @RequestParam("hub.challenge") String challenge){
        if(mode.equals("subscribe") && verify_token.equals("yomeanimo_token") ){
            return ResponseEntity.ok(challenge);
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("");
    }

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public ResponseEntity<Void> get(){
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
