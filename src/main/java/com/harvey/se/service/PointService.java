package com.harvey.se.service;

import com.harvey.se.pojo.dto.UserDto;

import java.util.concurrent.TimeUnit;

/**
 * 积分接口, 本身与SQL无关
 *
 * @author <a href="mailto:harvey.blocks@outlook.com">Harvey Blocks</a>
 * @version 1.0
 * @date 2025-11-08 01:07
 */
public interface PointService {

    /**
     * @param keyPre  键
     * @param user    目标用户
     * @param count   一次间隔允许加几次分
     * @param point   每次加分
     * @param timeout 时间间隔
     * @param unit    间隔单位
     */
    void add(String keyPre, UserDto user, int count, int point, int timeout, TimeUnit unit);
}
