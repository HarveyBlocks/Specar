package com.harvey.se.service;

import com.harvey.se.pojo.dto.ChatTextPiece;
import com.harvey.se.pojo.dto.UserDto;

import java.util.List;

/**
 * @author <a href="mailto:harvey.blocks@outlook.com">Harvey Blocks</a>
 * @version 1.0
 * @date 2025-11-09 02:29
 */
public interface RobotChatService {

    List<String> summaryMessage(String message, int appIndex);

    List<ChatTextPiece> pullPieces(Long chatId, Integer limit);


    Long chat(UserDto user, String message, int chatIndex) throws InterruptedException;
}
