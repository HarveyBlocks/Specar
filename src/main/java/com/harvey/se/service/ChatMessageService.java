package com.harvey.se.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.harvey.se.pojo.dto.ChatMessageDto;
import com.harvey.se.pojo.entity.ChatMessage;
import com.harvey.se.pojo.vo.DateRange;

import java.util.List;

/**
 * @author <a href="mailto:harvey.blocks@outlook.com">Harvey Blocks</a>
 * @version 1.0
 * @date 2025-11-09 16:25
 */
public interface ChatMessageService extends IService<ChatMessage> {
    void saveMessage(ChatMessageDto chatMessageDto);

    List<ChatMessageDto> queryByUser(Long userId, DateRange dateRange, Page<ChatMessage> page);
}
