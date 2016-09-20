/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package com.fransis.scheduler;

import com.fransis.repository.FeedRepository;
import com.fransis.repository.FilterRepository;
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

    @Scheduled(fixedRate = 60000)
    public void verificar(){

        AsyncTaskGetFeed asyncTaskGetFeed = new AsyncTaskGetFeed("1027119427344209","EAACEdEose0cBAPpIpAjNSii9qkuK3y5e1MTp1b2KkZCjTIkkZBGGyXE3rsbmB9ZCiZCq2BLZAZBNuJjdWHRjNxSAxnfOQ5zVZC73jwEQHhvW8g9XF36MXzWqiP505JvxTjjQYc66ZBL7EycstKlKFGoWaObpdGjJoNqAWkxMQ93SYAZDZD", feedRepository, filterRepository);
        asyncTaskGetFeed.run();
        log.info("Verificando Feeds");
    }

}
