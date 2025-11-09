package com.harvey.se.pojo.dto;

import com.harvey.se.exception.ResourceNotFountException;
import com.harvey.se.pojo.entity.ChatMessage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:harvey.blocks@outlook.com">Harvey Blocks</a>
 * @version 1.0
 * @date 2025-11-09 14:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "聊天的文本")
public class ChatMessageDto {

    @ApiModelProperty(value = "和ChatId不同, 系统会重新生成另一个Id")
    private Long id;

    @ApiModelProperty(value = "从属与哪个User. 系统会设置成当前已经登录的哪个用户")
    private Long userId;

    @ApiModelProperty(value = "文本", required = true)
    private String text;

    @ApiModelProperty(value = "true 表示是用户发的, false 表示是机器人发的", required = true)
    private boolean formUser;

    @ApiModelProperty(value = "其实是上传时间")
    private java.util.Date createTime;

    public static ChatMessageDto adapt(ChatMessage entity) {
        if (entity == null) {
            throw new ResourceNotFountException("请求不存在的资源");
        }
        return new ChatMessageDto(
                entity.getId(),
                entity.getUserId(),
                entity.getText(),
                entity.isFormUser(),
                entity.getCreateTime()
        );
    }
}
