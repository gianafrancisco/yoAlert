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
import com.restfb.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by francisco on 22/08/2016.
 */
@Component("scheduleTask")
public class ScheduleTask {

    private static final Logger log = LoggerFactory.getLogger(ScheduleTask.class);

    @Value("${fb.rowsLimit}")
    private int rowsLimits = 10;

    @Autowired
    private FeedRepository feedRepository;
    @Autowired
    private EmailSender sender;
    @Autowired
    private WatcherRepository watcherRepository;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private FilterRepository filterRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UsernameRepository usernameRepository;

    @Scheduled(fixedRate = 360000)
    public void verificar(){
        List<Watcher> watchers = watcherRepository.findAll();
        for(Watcher watcher: watchers){
            List<FbUsername> usernameList = usernameRepository.findByWatcher(watcher);
            FbUsername username = null;
            if(usernameList.size() > 0) {
                username = usernameList.get(0);
                watcher.setUsername(username);
            }
            if(watcher.getUsername() != null){
                watcher.setGroups(groupRepository.findByWatcher(watcher));
                watcher.setEmails(emailRepository.findByWatcher(watcher));
                watcher.setFilters(filterRepository.findByWatcher(watcher));
                DefaultFacebookClient facebookClient = new DefaultFacebookClient(watcher.getUsername().getAccessToken(), Version.VERSION_3_0);
                AsyncTaskGetFeed asyncTaskGetFeed = new AsyncTaskGetFeed(feedRepository, sender, watcher, facebookClient, rowsLimits);
                log.info("Verificando grupos");
                new Thread(asyncTaskGetFeed).start();
            }
        }
    }

}
