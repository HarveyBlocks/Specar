package com.harvey.se.controller.admin;

import com.harvey.se.exception.BadRequestException;
import com.harvey.se.pojo.dto.ChatMessageDto;
import com.harvey.se.pojo.vo.DateRange;
import com.harvey.se.pojo.vo.Result;
import com.harvey.se.properties.ConstantsProperties;
import com.harvey.se.service.ChatMessageService;
import com.harvey.se.util.ConstantsInitializer;
import com.harvey.se.util.ServerConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

/**
 * @author <a href="mailto:harvey.blocks@outlook.com">Harvey Blocks</a>
 * @version 1.0
 * @date 2025-11-08 00:14
 */
@Slf4j
@RestController
@Api(tags = "管理员管理用户的聊天记录")
@RequestMapping("/admin/robot")
@EnableConfigurationProperties(ConstantsProperties.class)
public class AdminChatController {

    @Resource
    private ChatMessageService chatMessageService;

    @Resource
    private ConstantsInitializer constantsInitializer;


    @GetMapping(value = {"/history/{user-id}/{time-from}/{time-to}/{limit}/{page}",
            "/history/{user-id}/{time-from}/{time-to}/{limit}", "/history/{user-id}/{time-from}/{time-to}",
            "/history/{user-id}/{time-from}", "/history/{user-id}",})
    @ApiOperation("查询一定时间内的用户聊天记录")
    @ApiResponse(code = 200, message = "按照时间排序, 返回的时间顺序和参数的from-to一致")
    public Result<List<ChatMessageDto>> queryByTimeRange(
            @PathVariable(value = "user-id") @ApiParam(value = "目标用户的ID", required = true) Long userId,
            @PathVariable(value = "time-from", required = false)
            @ApiParam(value = "日期查询的起点(包含)", example = ServerConstants.DATE_TIME_FORMAT_STRING)
            String timeFrom,
            @PathVariable(value = "time-to", required = false)
            @ApiParam(value = "日期查询的终点(包含)", example = ServerConstants.DATE_TIME_FORMAT_STRING) String timeTo,
            @PathVariable(value = "limit", required = false)
            @ApiParam(value = "页长", defaultValue = ServerConstants.DEFAULT_PAGE_SIZE) Integer limit,
            @PathVariable(value = "page", required = false) @ApiParam(value = "页号", defaultValue = "1")
            Integer page) {
        // 依据请求发送的时间查询
        DateRange dateRange;
        try {
            dateRange = ConstantsInitializer.initDateRange(timeFrom, timeTo);
        } catch (ParseException e) {
            throw new BadRequestException("错误的日期格式", e);
        }
        return new Result<>(chatMessageService.queryByUser(
                userId,
                dateRange,
                constantsInitializer.initPage(page, limit)
        ));
    }
}
