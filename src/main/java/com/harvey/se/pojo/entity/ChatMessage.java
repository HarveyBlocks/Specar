package com.harvey.se.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.harvey.se.exception.ResourceNotFountException;
import com.harvey.se.pojo.dto.ChatMessageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author <a href="mailto:harvey.blocks@outlook.com">Harvey Blocks</a>
 * @version 1.0
 * @date 2025-11-09 14:14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("`tb_chat_message`")
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("user_id")
    private Long userId;

    private String text;

    @TableField(value = "from_user")
    private boolean formUser;

    @TableField(value = "create_time")
    private java.util.Date createTime;

    public static ChatMessage adapt(ChatMessageDto dto) {
        if (dto == null) {
            throw new ResourceNotFountException("请求参数是不存在的资源");
        }
        return new ChatMessage(
                dto.getId(),
                dto.getUserId(),
                dto.getText(),
                dto.isFormUser(),
                dto.getCreateTime()
        );
    }
}
