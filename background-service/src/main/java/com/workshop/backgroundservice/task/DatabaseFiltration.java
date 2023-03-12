package com.workshop.backgroundservice.task;


import com.workshop.backgroundservice.service.AuthService;
import com.workshop.backgroundservice.service.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@EnableAsync
@Component
@Profile("prod")
public class DatabaseFiltration {

    private final AuthService authService;
    private final MetadataService metadataService;

    @Autowired
    public DatabaseFiltration(AuthService authService, MetadataService metadataService) {
        this.authService = authService;
        this.metadataService = metadataService;
    }


    @Async
    @Scheduled(cron = "${task.auth.filtration.cron}") //prod: clear expired users avery day at 4:00 AM
    public void clearExpiredUser() {
        authService.removeExpiredUsers();
    }


    @Async
    @Scheduled(cron = "${task.metadata.filtration.cron}")
    public void clearNonValidMetadata() {
        Date to = new Date();
        Calendar calendar  = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date from = new Date(calendar.getTimeInMillis());

        metadataService.removeNonValidLikesForPeriod(from, to);
        metadataService.removeNonValidCommentsForPeriod(from, to);
        metadataService.removeNonValidReviewsForPeriod(from, to);
    }

}
