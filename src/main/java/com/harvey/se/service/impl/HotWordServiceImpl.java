package com.harvey.se.service.impl;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.se.dao.HotWordMapper;
import com.harvey.se.pojo.dto.HotWordDto;
import com.harvey.se.pojo.entity.HotWord;
import com.harvey.se.service.HotWordService;
import com.harvey.se.service.RobotChatService;
import com.harvey.se.util.ConstantsInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


/**
 * @author <a href="mailto:harvey.blocks@outlook.com">Harvey Blocks</a>
 * @version 1.0
 * @date 2025-11-08 06:56
 * @see HotWord
 * @see HotWordMapper
 * @see HotWordService
 */
@Service
@Slf4j
public class HotWordServiceImpl extends ServiceImpl<HotWordMapper, HotWord> implements HotWordService {
    @Resource
    private ConstantsInitializer constantsInitializer;

    @Override
    public List<HotWordDto> top(Integer limit) {
        return new LambdaQueryChainWrapper<>(baseMapper).orderByDesc(
                        HotWord::getFrequency,
                        (SFunction<HotWord, ?>[]) null
                )
                .page(constantsInitializer.initPage(1, limit))
                .getRecords()
                .stream()
                .map(HotWordDto::adapte)
                .collect(Collectors.toList());
    }

    private final ExecutorService executor = Executors.newCachedThreadPool(task -> new Thread(task, "chat-thread"));
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private RobotChatService robotChatService;

    @Override
    public void batchInsert(List<String> keywords) {
        // 查询已存在旧词
        Map<String, HotWord> inDbWords = new LambdaQueryChainWrapper<>(baseMapper).in(HotWord::getWord, keywords)
                .list()
                .stream()
                .collect(Collectors.toMap(HotWord::getWord, e -> e));
        // 增加旧词的频率
        StringJoiner inDbJoiner = new StringJoiner(",", "(", ")");
        List<HotWord> toBeInsert = new ArrayList<>();
        for (String keyword : keywords) {
            HotWord inDb = inDbWords.get(keyword);
            if (inDb == null) {
                toBeInsert.add(new HotWord(null, keyword, 1));
            } else {
                inDbJoiner.add(String.valueOf(inDb.getId()));
            }
        }
        if (!inDbWords.isEmpty()) {
            String joinedWords = inDbJoiner.toString();
            log.debug("将要增加的词频的id: {} ", joinedWords);
            jdbcTemplate.update("update tb_hot_word set frequency = frequency+1 where id in " + joinedWords + ";");
        }
        if (!toBeInsert.isEmpty()) {
            // 新增新的词
            log.debug("将要增加的新词: {} ", toBeInsert);
            boolean saved = super.saveBatch(toBeInsert);
            if (!saved) {
                log.warn("新增关键词{}失败", keywords);
            }
        }
    }

    @Override
    public void summaryKeyword(String text) {
        executor.execute(() -> {
            List<String> strings = robotChatService.summaryMessage(text, 0);
            if (strings == null || strings.isEmpty()) {
                log.warn("无法获取关键字");
                return;
            }
            batchInsert(strings);
        });
    }
}