/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package com.fransis.scheduler;

import com.fransis.email.EmailSender;
import com.fransis.model.FbUsername;
import com.fransis.model.Watcher;
import com.fransis.repository.*;
import com.fransis.task.AsyncTaskGetFeed;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by francisco on 22/08/2016.
 */
@Component("refreshTokenTask")
public class RefreshTokenTask {

    private static final String MY_APP_ID = "1334300239928512";
    private static final String MY_APP_SECRET = "fd26d7bc50496527912c4abcee2bf172";

    private static final Logger log = LoggerFactory.getLogger(RefreshTokenTask.class);

    @Autowired
    private UsernameRepository usernameRepository;

    @Scheduled(fixedRate = 86400000)
    public void refresh(){
        List<FbUsername> usernameList = usernameRepository.findAll();
        for(FbUsername user: usernameList){
            FacebookClient.AccessToken accessTokenExtended =
                    new DefaultFacebookClient().obtainExtendedAccessToken(MY_APP_ID,
                            MY_APP_SECRET, user.getAccessToken());
            FbUsername user2 = new FbUsername(user.getUsername(), accessTokenExtended.getAccessToken(), user.getUsuarioId());
            user2.setWatcher(user.getWatcher());
            log.info("Current Token: " + user.getAccessToken());
            log.info("New Token: " + user2.getAccessToken());
            //usernameRepository.saveAndFlush(user2);
        }
    }

}
