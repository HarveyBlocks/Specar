package com.harvey.se.controller.admin;

import com.harvey.se.exception.UncompletedException;
import com.harvey.se.pojo.dto.UserActionLogDto;
import com.harvey.se.pojo.vo.Result;
import com.harvey.se.properties.ConstantsProperties;
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

import java.util.List;

/**
 * TODO
 *
 * @author <a href="mailto:harvey.blocks@outlook.com">Harvey Blocks</a>
 * @version 1.0
 * @date 2025-11-08 00:44
 */
@Slf4j
@RestController
@Api(tags = "管理员管理用户行为日志")
@RequestMapping("/admin/action")
@EnableConfigurationProperties(ConstantsProperties.class)
public class UserActionLogController {

    @GetMapping(value = "/cost/{longer-than}/{limit}/{page}")
    @ApiOperation("查询请求消耗时间大于某段时长的请求记录")
    @ApiResponse(code = 200, message = "返回在从花费长到花费短")
    public Result<List<UserActionLogDto>> longCost(
            @PathVariable(value = "longer-than")
            @ApiParam(value = "长度大于此毫秒数(包括)的, 将被返回", required = true) Integer longerThan,
            @PathVariable(value = "limit", required = false)
            @ApiParam(value = "页长", defaultValue = ConstantsProperties.DEFAULT_PAGE_SIZE) Integer limit,
            @PathVariable(value = "page", required = false) @ApiParam(value = "页号", defaultValue = "1")
            Integer page) {
        // 在服务器消费耗费长的操作
        // 倒序排序
        throw new UncompletedException("在服务器消费耗费长的操作, 从花费长到花费短");
    }


    @GetMapping(value = "/request-time-latest/{time-from}/{time-to}/{limit}/{page}")
    @ApiOperation("查询一定时间内的用户行为日志")
    @ApiResponse(code = 200, message = "按照时间排序, 返回的时间顺序和参数的from-to一致")
    public Result<List<UserActionLogDto>> getLatestActionByRequestTimeRange(
            @PathVariable(value = "time-from", required = false)
            @ApiParam(value = "日期查询的起点(包含)", example = ConstantsProperties.AUTHORIZATION_HEADER)
            String timeFrom,
            @PathVariable(value = "time-to", required = false)
            @ApiParam(value = "日期查询的终点(包含)", example = ConstantsProperties.AUTHORIZATION_HEADER) String timeTo,
            @PathVariable(value = "limit", required = false)
            @ApiParam(value = "页长", defaultValue = ConstantsProperties.DEFAULT_PAGE_SIZE) Integer limit,
            @PathVariable(value = "page", required = false) @ApiParam(value = "页号", defaultValue = "1")
            Integer page) {
        // 依据请求发送的时间查询, 从新到旧排序
        throw new UncompletedException("依据请求发送的时间查询, 从新到旧排序");
    }
}
