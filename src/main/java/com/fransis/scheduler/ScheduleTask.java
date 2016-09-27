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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by francisco on 22/08/2016.
 */
@Component("scheduleTask")
public class ScheduleTask {

    private static final Logger log = LoggerFactory.getLogger(ScheduleTask.class);

    @Autowired
    private FeedRepository feedRepository;
    @Autowired
    private UsernameRepository usernameRepository;
    @Autowired
    private EmailSender sender;
    @Autowired
    private WatcherRepository watcherRepository;

    @Scheduled(fixedRate = 10000)
    public void verificar(){
        List<Watcher> watchers = watcherRepository.findAll();
        for(Watcher watcher: watchers){
            if(watcher.getUsername() != null){
                AsyncTaskGetFeed asyncTaskGetFeed = new AsyncTaskGetFeed(feedRepository, sender, watcher);
                new Thread(asyncTaskGetFeed).start();
                log.info("Verificando Feeds");
            }
        }
    }

}
