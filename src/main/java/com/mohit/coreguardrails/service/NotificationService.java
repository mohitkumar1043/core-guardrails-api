package com.mohit.coreguardrails.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void handleBotNotification(Long userId, Long botId) {

        String cooldownKey = "user:" + userId + ":notif_cooldown";
        String listKey = "user:" + userId + ":pending_notifs";

        Boolean cooldownExists = redisTemplate.hasKey(cooldownKey);

        if (Boolean.FALSE.equals(cooldownExists)) {

            System.out.println("Push Notification Sent to User: " + userId);

            redisTemplate.opsForValue()
                    .set(cooldownKey, "1", 15, TimeUnit.MINUTES);

        } else {

           
            redisTemplate.opsForList()
                    .rightPush(listKey,
                            "Bot " + botId + " interacted with user " + userId);
        }
    }
}
