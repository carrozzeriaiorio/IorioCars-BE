package com.ioriocars.ioriocars.task;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class KeepAliveTask {

    // ogni 10 minuti
    @Scheduled(fixedRate = 10 * 60 * 1000)
    public void keepAlive() {
        System.out.println("Task KeepAlive eseguito con successo!");
    }

}
