package com.harvey.se.controller.admin;

import com.harvey.se.pojo.dto.GiftInfoDto;
import com.harvey.se.pojo.vo.Null;
import com.harvey.se.pojo.vo.Result;
import com.harvey.se.properties.ConstantsProperties;
import com.harvey.se.service.GiftService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 管理键盘
 *
 * @author <a href="mailto:harvey.blocks@outlook.com">Harvey Blocks</a>
 * @version 1.0
 * @date 2025-11-08 01:21
 */
@Slf4j
@RestController
@Api(tags = "管理员管理礼品信息")
@RequestMapping("/admin/gift")
@EnableConfigurationProperties(ConstantsProperties.class)
public class AdminGiftController {
    @Resource
    private GiftService giftService;

    @PostMapping(value = "/insert")
    @ApiOperation("新增商品信息")
    public Result<Null> insert(
            @RequestBody @ApiParam(value = "id由后端生成, 其他属性一般不能为null", required = true)
            GiftInfoDto giftInfoDto) {
        giftService.insert(giftInfoDto);
        return Result.ok();
    }

    @PutMapping(value = "/update")
    @ApiOperation("更新商品信息")
    public Result<Null> update(
            @RequestBody @ApiParam(value = "如果属性值为null, 表示不做更新, id是检索目标的依据", required = true)
            GiftInfoDto giftInfoDto) {
        giftService.update(giftInfoDto);
        return Result.ok();
    }

    @DeleteMapping(value = "/delete/{id}")
    @ApiOperation("下架商品")
    public Result<Null> delete(
            @PathVariable(value = "id") @ApiParam(value = "目标下架礼品ID", required = true) Long id) {
        giftService.delete(id);
        return Result.ok();
    }
}
