package com.harvey.se.controller.admin;

import com.harvey.se.exception.UncompletedException;
import com.harvey.se.pojo.dto.FeedbackDto;
import com.harvey.se.pojo.vo.Null;
import com.harvey.se.pojo.vo.Result;
import com.harvey.se.properties.ConstantsProperties;
import com.harvey.se.util.ConstantsInitializer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * TODO 管理键盘
 *
 * @author <a href="mailto:harvey.blocks@outlook.com">Harvey Blocks</a>
 * @version 1.0
 * @date 2025-11-08 01:21
 */
@Slf4j
@RestController
@Api(tags = "管理员管理反馈信息")
@RequestMapping("/admin/feedback")
@EnableConfigurationProperties(ConstantsProperties.class)
public class AdminFeedbackController {

    @Resource
    private ConstantsInitializer constantsInitializer;

    @GetMapping(value = "/feedback-oldest/{time-from}/{time-to}/{limit}/{page}")
    @ApiOperation("查询一定时间内的未读的用户反馈")
    @ApiResponse(code = 200, message = "按照时间排序, 返回的时间顺序和参数的from-to一致")
    public Result<List<FeedbackDto>> getFeedbackDtoByTimeRange(
            @PathVariable(value = "time-from", required = false)
            @ApiParam(value = "日期查询的起点(包含)", example = ConstantsProperties.AUTHORIZATION_HEADER)
            String timeFrom,
            @PathVariable(value = "time-to", required = false)
            @ApiParam(value = "日期查询的终点(包含)", example = ConstantsProperties.AUTHORIZATION_HEADER) String timeTo,
            @PathVariable(value = "limit", required = false)
            @ApiParam(value = "页长", defaultValue = ConstantsProperties.DEFAULT_PAGE_SIZE) Integer limit,
            @PathVariable(value = "page", required = false) @ApiParam(value = "页号", defaultValue = "1")
            Integer page) {
        // 依据请求发送的时间查询, 从旧到新排序

        throw new UncompletedException("查询从旧到新的未读反馈");
    }

    @GetMapping(value = "/read/feedback-latest/{time-from}/{time-to}/{limit}/{page}")
    @ApiOperation("查询一定时间内的已读的用户反馈")
    @ApiResponse(code = 200, message = "按照时间排序, 返回的时间顺序和参数的from-to一致")
    public Result<List<FeedbackDto>> getReadFeedbackDtoByTimeRange(
            @PathVariable(value = "time-from", required = false)
            @ApiParam(value = "日期查询的起点(包含)", example = ConstantsProperties.AUTHORIZATION_HEADER)
            String timeFrom,
            @PathVariable(value = "time-to", required = false)
            @ApiParam(value = "日期查询的终点(包含)", example = ConstantsProperties.AUTHORIZATION_HEADER) String timeTo,
            @PathVariable(value = "limit", required = false)
            @ApiParam(value = "页长", defaultValue = ConstantsProperties.DEFAULT_PAGE_SIZE) Integer limit,
            @PathVariable(value = "page", required = false) @ApiParam(value = "页号", defaultValue = "1")
            Integer page) {
        // 已读的
        // 依据请求发送的时间查询, 从新到旧排序
        throw new UncompletedException("查询从新到旧的已读反馈");
    }

    @GetMapping(value = "/user/{user-id}/{read}/{limit}/{page}")
    @ApiOperation("查询某一个特定用户的反馈")
    public Result<List<FeedbackDto>> getByUserId(
            @PathVariable(value = "user-id", required = true) @ApiParam(value = "用户id", required = true) Long userId,
            @PathVariable(value = "read", required = false) @ApiParam(value = "默认null, 表示两个都查") Boolean read,
            @PathVariable(value = "limit", required = false)
            @ApiParam(value = "页长", defaultValue = ConstantsProperties.DEFAULT_PAGE_SIZE) Integer limit,
            @PathVariable(value = "page", required = false) @ApiParam(value = "页号", defaultValue = "1")
            Integer page) {
        // 对某条反馈已读
        throw new UncompletedException("将反馈标记为已读");
    }


    @PutMapping(value = "/read")
    @ApiOperation("将某一个用户反馈标记为已读")
    public Result<Null> read(
            @RequestBody @ApiParam(value = "在body里面简单的就是一个id, 不需要键值对", required = true) Long id) {
        // 对某条反馈已读
        throw new UncompletedException("将反馈标记为已读");
    }
}
