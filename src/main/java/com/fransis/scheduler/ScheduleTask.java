/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package com.fransis.scheduler;

import com.fransis.model.FbUsername;
import com.fransis.model.FbGroup;
import com.fransis.repository.UsernameRepository;
import com.fransis.repository.FeedRepository;
import com.fransis.repository.FilterRepository;
import com.fransis.repository.GroupRepository;
import com.fransis.task.AsyncTaskGetFeed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by francisco on 22/08/2016.
 */
@Component("scheduleTask")
public class ScheduleTask {

    private static final Logger log = LoggerFactory.getLogger(ScheduleTask.class);

    @Autowired
    private FeedRepository feedRepository;
    @Autowired
    private FilterRepository filterRepository;

    @Autowired
    private UsernameRepository usernameRepository;

    @Autowired
    private GroupRepository groupRepository;


    @Scheduled(fixedRate = 60000)
    public void verificar(){

        FbUsername fbUsername = usernameRepository.findOne("franciscogiana@hotmail.com");
        FbGroup fbGroup = groupRepository.findAll().get(0);

        AsyncTaskGetFeed asyncTaskGetFeed = new AsyncTaskGetFeed(feedRepository, filterRepository, fbUsername, fbGroup);
        asyncTaskGetFeed.run();
        log.info("Verificando Feeds");
    }

}
