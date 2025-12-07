package com.jamuara.crs.shceduler;

import com.jamuara.crs.common.service.TboAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Profile("ec2")
public class CronJobScheduler {

    @Autowired
    private TboAuthService tboAuthService;

    @Scheduled(cron = "0 31 18 * * *")
//    @Scheduled(cron = "10 *  * * * *")
    public void tboAuthToken() {
        tboAuthService.authenticate();
    }

}
