package cn.edu.nwafu.nexus.domain.service;

import cn.edu.nwafu.nexus.infrastructure.model.dto.chat.SimpleChatRequest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface ChatService {
    /**
     * 创建流式聊天请求
     *
     * @param request 简化的聊天请求参数
     * @return SSE发射器
     */
    SseEmitter chatStream(SimpleChatRequest request);
} 