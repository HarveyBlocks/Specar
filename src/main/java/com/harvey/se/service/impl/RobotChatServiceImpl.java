package com.harvey.se.service.impl;

import com.alibaba.dashscope.app.Application;
import com.alibaba.dashscope.app.ApplicationParam;
import com.alibaba.dashscope.app.ApplicationResult;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.harvey.se.exception.BadRequestException;
import com.harvey.se.pojo.dto.ChatMessageDto;
import com.harvey.se.pojo.dto.ChatTextPiece;
import com.harvey.se.pojo.dto.ConsultationContentDto;
import com.harvey.se.pojo.dto.UserDto;
import com.harvey.se.properties.RobotChatProperties;
import com.harvey.se.service.ChatMessageService;
import com.harvey.se.service.ConsultationContentService;
import com.harvey.se.service.PointService;
import com.harvey.se.service.RobotChatService;
import com.harvey.se.util.JacksonUtil;
import com.harvey.se.util.RedisConstants;
import com.harvey.se.util.RedisIdWorker;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:harvey.blocks@outlook.com">Harvey Blocks</a>
 * @version 1.0
 * @date 2025-11-09 02:29
 */
@Service
@Slf4j
public class RobotChatServiceImpl implements RobotChatService {
    private final ExecutorService executor = Executors.newCachedThreadPool(task -> new Thread(task, "chat-thread"));
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedisIdWorker redisIdWorker;
    @Resource
    private JacksonUtil jacksonUtil;
    @Resource
    private RedissonClient redissonClient;
    Application application = new Application();
    @Resource
    private RobotChatProperties robotChatProperties;
    @Resource
    private PointService pointService;
    @Resource
    private ChatMessageService chatMessageService;
    @Resource
    private ConsultationContentService consultationContentService;

    @Override
    public Long chat(UserDto user, String message, int chatIndex) {
        if (chatIndex >= robotChatProperties.getAppId().size()) {
            throw new BadRequestException("Error of chat index, not exist!");
        }
        Long chatId = createChatId();
        executor.execute(() -> chatWithLock(chatId, chatIndex, user, message));
        return chatId;
    }

    private void chatWithLock(Long chatId, int chatIndex, UserDto user, String message) {
        RLock lock = redissonClient.getLock(RedisConstants.Chat.LOCK_KEY + user.getId());
        boolean isLock;
        try {
            isLock = lock.tryLock(-1L, -1L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (!isLock) {
            push2Redis(chatId, ChatTextPiece.ofQuestionWhileGenerating("不允许重复提问"));
            return;
        }
        try {
            // 1. 加分
            pointService.add(RedisConstants.Point.CHAT, user, 5, 5, 1, TimeUnit.DAYS);
            // 2. 聊天
            executeChat(chatId, user.getId(), message, chatIndex);
        } catch (NoApiKeyException | InputRequiredException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<String> summaryMessage(String message, int appIndex) {
        ApplicationResult applicationResult;
        try {
            log.info("开始分词");
            applicationResult = summarizeKeywords(message, appIndex);
        } catch (NoApiKeyException | InputRequiredException e) {
            log.error("与AI的沟通失败, 无法总结关键字");
            return null;
        }
        log.info("分词完毕, 进行解析...");
        String text = applicationResult.getOutput().getText();
        log.warn("返回的关键字文本是: {}", text);
        StringTokenizer tokenizer = new StringTokenizer(text, "|");
        List<String> parts = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            token = token.trim();
            if (!token.isEmpty()) {
                parts.add(token);
            }
        }
        return parts;
    }

    private void executeChat(Long chatId, Long userId, String question, int appIndex)
            throws NoApiKeyException, InputRequiredException {
        // 存问题
        chatMessageService.saveMessage(new ChatMessageDto(null, userId, question, true, new Date()));
        // 提问
        Flowable<ApplicationResult> result = flowChat(userId, question, appIndex);
        AtomicInteger pieceIdGenerator = new AtomicInteger(0);
        StringJoiner fullText = new StringJoiner(" ");
        try {
            log.info("Starting chat: {}", chatId);
            result.blockingForEach(data -> {
                String textPiece = data.getOutput().getText();
                if (textPiece == null || textPiece.isEmpty()) {
                    return; // 忽略
                }
                int pieceId = pieceIdGenerator.incrementAndGet();
                fullText.add(textPiece);
                push2Redis(chatId, new ChatTextPiece(pieceId, textPiece));
            });
        } catch (Exception error) {
            push2Redis(chatId, ChatTextPiece.ofServiceError("... ERROR[服务端发生异常!]"));
            log.error("error on chat " + chatId, error);
            throw error;
        } finally {
            // 存回答
            chatMessageService.saveMessage(new ChatMessageDto(null, userId, fullText.toString(), false, new Date()));
        }
        push2Redis(chatId, ChatTextPiece.ofSuccessfullyFinished("文本正常结束"));
        log.info("chat {} is completed, total chunks: {}", chatId, pieceIdGenerator.get());
    }

    public Flowable<ApplicationResult> flowChat(Long userId, String message, int appIndex)
            throws NoApiKeyException, InputRequiredException {
        ApplicationParam param = ApplicationParam.builder()
                .apiKey(robotChatProperties.getApiKey())
                .appId(robotChatProperties.getAppId().get(appIndex)) // ???
                .prompt(supplementConsultationInformation(userId, message))
                .incrementalOutput(true)
                .build();
        return application.streamCall(param);
    }

    public ApplicationResult summarizeKeywords(String message, int appIndex)
            throws NoApiKeyException, InputRequiredException {
        ApplicationParam param = ApplicationParam.builder()
                .apiKey(robotChatProperties.getApiKey())
                .appId(robotChatProperties.getAppId().get(appIndex)) // ???
                .prompt(supplementHotWordInformation(message))
                .build();
        return application.call(param);
    }

    private String supplementHotWordInformation(String message) {
        return "请你总结下面文本的关键字, 每个关键字使用字符`|`分割, 以纯文本格式返回, 不得有任何其他的多余的回复: " +
               message;
    }

    private String supplementConsultationInformation(Long userId, String message) {
        ConsultationContentDto consultationContentDto = consultationContentService.queryByUser(userId);
        return "场景: 用户想想咨询一些有关购车的建议, 用户想要求和背景信息如下: `" +
               jacksonUtil.toJsonStr(consultationContentDto) +
               "`, 请你依据这些信息给用户提出一些建议. 用户的具体问题如下(" +
               "用户具体问题如果和背景信息冲突, 具体问题的优先级更高. " +
               "如果用户的具体问题和购车咨询无关, 请你回答`我只做购车有关咨询, 这方面我并不了解`): " +
               message;
    }


    public long createChatId() {
        return redisIdWorker.nextId(RedisConstants.Chat.ID_GENERATOR);
    }

    public void push2Redis(Long chatId, ChatTextPiece text) {
        String key = RedisConstants.Chat.PIECE_QUEUE_KEY + chatId;
        Long nowSize = stringRedisTemplate.opsForList().rightPush(key, jacksonUtil.toJsonStr(text));
        stringRedisTemplate.expire(key, RedisConstants.Chat.PIECE_QUEUE_EXPIRE_SECOND, TimeUnit.SECONDS);
        log.info("now size of {} is {}", chatId, nowSize);
    }

    /**
     * @param limit 一般情况下result.size == limit, 数据库中不足时尽可能返回
     */
    @Override
    public List<ChatTextPiece> pullPieces(Long chatId, Integer limit) {
        limit = limit == null ? 20 : Math.min(20, limit);
        if (limit <= 0) {
            return List.of();
        }
        String key = RedisConstants.Chat.PIECE_QUEUE_KEY + chatId;
        //noinspection rawtypes
        DefaultRedisScript<List> script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource(RedisConstants.Chat.MULTIPLY_LEFT_POP_LUA));
        script.setResultType(List.class);
        //noinspection unchecked
        List<String> range = (List<String>) stringRedisTemplate.execute(
                script,
                List.of()/*不要传null*/,
                key,
                String.valueOf(limit)
        );
        if (range == null) {
            return List.of();
        }
        List<ChatTextPiece> pieces = range.stream()
                .map(s -> jacksonUtil.toBean(s, ChatTextPiece.class))
                .collect(Collectors.toList());
        if (pieces.isEmpty()) {
            return pieces;
        }
        if (pieces.get(pieces.size() - 1).endSign()) {
            stringRedisTemplate.delete(key);
        }
        return pieces;
    }

}
