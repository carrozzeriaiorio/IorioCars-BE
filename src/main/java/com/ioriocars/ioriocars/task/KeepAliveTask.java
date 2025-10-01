package com.ioriocars.ioriocars.task;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Component
public class KeepAliveTask {

    private final DataSource dataSource;

    public KeepAliveTask(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Scheduled(fixedRate = 86400000) // ogni 24 ore
    public void pingDatabase() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("SELECT 1;");
            System.out.println("Ping al database Prisma eseguito");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
