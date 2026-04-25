package com.mohit.coreguardrails.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class NotificationScheduler {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Scheduled(fixedRate = 300000) 
    public void sweepNotifications() {

        System.out.println("[Scheduler] Running notification sweep...");

        Set<String> keys = redisTemplate.keys("user:*:pending_notifs");

        if (keys == null) return;

        for (String key : keys) {

            List<String> notifications =
                    redisTemplate.opsForList().range(key, 0, -1);

            if (notifications == null || notifications.isEmpty()) {
                continue;
            }

            int count = notifications.size();
            String first = notifications.get(0);

            System.out.println(
                "Summarized Push Notification: "
                + first
                + " and [" + (count - 1) + "] others interacted with your posts."
            );

            redisTemplate.delete(key);

            System.out.println("[Scheduler] Cleared queue: " + key);
        }
    }
}
