package com.harvey.se.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harvey.se.pojo.dto.HotWordDto;
import com.harvey.se.pojo.entity.HotWord;

import java.util.List;

/**
 * @author <a href="mailto:harvey.blocks@outlook.com">Harvey Blocks</a>
 * @version 1.0
 * @date 2025-11-08 06:51
 */
public interface HotWordService extends IService<HotWord> {
    List<HotWordDto> top(Integer limit);

    void batchInsert(List<String> keywords);

    void summaryKeyword(String text);
}
