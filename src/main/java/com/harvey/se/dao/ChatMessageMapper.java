package com.harvey.se.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harvey.se.pojo.entity.ChatMessage;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author <a href="mailto:harvey.blocks@outlook.com">Harvey Blocks</a>
 * @version 1.0
 * @date 2025-11-09 16:35
 */
@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {
}
