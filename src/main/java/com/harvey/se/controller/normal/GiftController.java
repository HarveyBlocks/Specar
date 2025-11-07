package com.harvey.se.controller.normal;

import com.harvey.se.exception.UncompletedException;
import com.harvey.se.pojo.dto.GiftDto;
import com.harvey.se.pojo.dto.GiftInfoDto;
import com.harvey.se.pojo.vo.Null;
import com.harvey.se.pojo.vo.Result;
import com.harvey.se.properties.ConstantsProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO 奖品功能, 展示奖品
 *
 * @author <a href="mailto:harvey.blocks@outlook.com">Harvey Blocks</a>
 * @version 1.0
 * @date 2025-11-08 01:06
 */
@Slf4j
@RestController
@Api(tags = "礼品查询")
@RequestMapping("/gift")
@EnableConfigurationProperties(ConstantsProperties.class)
public class GiftController {
    @GetMapping(value = "/all/{limit}/{page}")
    @ApiOperation("分页查询Gift的简略信息")
    @ApiResponse(code = 200, message = "按照id升序")
    public Result<List<GiftDto>> queryAll(
            @PathVariable(value = "limit", required = false)
            @ApiParam(value = "页长", defaultValue = ConstantsProperties.DEFAULT_PAGE_SIZE) Integer limit,
            @PathVariable(value = "page", required = false) @ApiParam(value = "页号", defaultValue = "1")
            Integer page) {
        // 不提供就使用默认值
        throw new UncompletedException("分页查询商品简略信息");
    }

    @GetMapping(value = "/cost-in-range/{lower}/{upper}/{limit}/{page}")
    @ApiOperation("用预算的上下限来计算, 分页查询Gift的简略信息")
    @ApiResponse(code = 200, message = "使用升序排序")
    public Result<List<GiftDto>> queryCostInRange(
            @PathVariable(value = "lower", required = false)
            @ApiParam(value = "商品花费积分下限(包含)", defaultValue = "0") Integer lower,
            @PathVariable(value = "upper", required = false)
            @ApiParam(value = "商品花费积分上限(包含)", defaultValue = "null, 表示无限") Integer upper,
            @PathVariable(value = "limit", required = false)
            @ApiParam(value = "页长", defaultValue = ConstantsProperties.DEFAULT_PAGE_SIZE) Integer limit,
            @PathVariable(value = "page", required = false) @ApiParam(value = "页号", defaultValue = "1")
            Integer page) {
        // 按照花销排序, 使用升序排序
        throw new UncompletedException("依据积分花费的上下限来进行查询");
    }

    @GetMapping(value = "/detail/{id}")
    @ApiOperation("获取某一商品的详情信息")
    public Result<GiftInfoDto> giftDetail(
            @PathVariable(value = "id")
            @ApiParam(value = "目标礼品id, 依据用户选择的商品简略信息来获取", required = true) Long id) {
        // 查礼品询详细信息
        throw new UncompletedException("获取礼品细节信息");
    }

    @PutMapping(value = "/consume/")
    @ApiOperation("用用户自己的积分进行消费")
    public Result<Null> consume(
            @RequestBody @ApiParam(value = "目标礼品id", required = true) Long id) {
        // 进行消费
        throw new UncompletedException("消费积分换取礼品");
    }
}
