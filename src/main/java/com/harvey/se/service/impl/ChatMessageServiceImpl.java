package com.harvey.se.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.se.dao.ChatMessageMapper;
import com.harvey.se.pojo.dto.ChatMessageDto;
import com.harvey.se.pojo.entity.ChatMessage;
import com.harvey.se.pojo.vo.DateRange;
import com.harvey.se.service.ChatMessageService;
import com.harvey.se.service.HotWordService;
import com.harvey.se.service.ServiceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:harvey.blocks@outlook.com">Harvey Blocks</a>
 * @version 1.0
 * @date 2025-11-09 16:26
 */
@Service
@Slf4j
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements ChatMessageService {


    @Resource
    private HotWordService hotWordService;

    @Override
    public void saveMessage(ChatMessageDto chatMessageDto) {
        hotWordService.summaryKeyword(chatMessageDto.getText());
        boolean save = save(ChatMessage.adapt(chatMessageDto));
        if (!save) {
            log.warn("保存聊天文本 {} 失败", chatMessageDto);
        }
    }

    @Override
    public List<ChatMessageDto> queryByUser(Long userId, DateRange dateRange, Page<ChatMessage> page) {
        return ServiceUtil.queryAndOrderWithDate(new LambdaQueryChainWrapper<>(baseMapper).eq(
                        ChatMessage::getUserId,
                        userId
                ), ChatMessage::getCreateTime, dateRange, page)
                .stream()
                .map(ChatMessageDto::adapt)
                .collect(Collectors.toList());
    }
}
