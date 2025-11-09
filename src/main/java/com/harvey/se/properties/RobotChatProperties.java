package com.harvey.se.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author <a href="mailto:harvey.blocks@outlook.com">Harvey Blocks</a>
 * @version 1.0
 * @date 2025-11-09 14:26
 */
@Data
@Component
@ConfigurationProperties(prefix = "h-se.robot.chat")
public class RobotChatProperties {
    private String apiKey;
    private List<String> appId;
}
