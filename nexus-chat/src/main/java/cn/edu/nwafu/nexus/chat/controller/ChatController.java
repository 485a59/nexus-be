package cn.edu.nwafu.nexus.chat.controller;

import cn.edu.nwafu.nexus.domain.service.ChatService;
import cn.edu.nwafu.nexus.infrastructure.model.dto.chat.SimpleChatRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;

/**
 * @author Huang Z.Y.
 */
@Slf4j
@Api(tags = "聊天接口")
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Resource
    private ChatService chatService;

    @ApiOperation("创建流式聊天请求")
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter chatStream(@RequestBody SimpleChatRequest request) {
        return chatService.chatStream(request);
    }
}
