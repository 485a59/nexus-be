package cn.edu.nwafu.nexus.domain.service.impl;

import cn.edu.nwafu.nexus.domain.service.ChatService;
import cn.edu.nwafu.nexus.infrastructure.model.dto.chat.ChatCompletionRequest;
import cn.edu.nwafu.nexus.infrastructure.model.dto.chat.SimpleChatRequest;
import cn.edu.nwafu.nexus.infrastructure.model.vo.chat.SimpleChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    @Value("${chat.api.url}")
    private String apiUrl;

    @Value("${chat.api.token}")
    private String apiToken;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public SseEmitter chatStream(SimpleChatRequest request) {
        SseEmitter emitter = new SseEmitter(-1L); // 无超时
        
        executorService.execute(() -> {
            try {
                // 转换为 API 所需的请求格式
                ChatCompletionRequest apiRequest = new ChatCompletionRequest();
                apiRequest.setModel(request.getModel());
                apiRequest.setStream(true); // 启用流式响应
                
                // 构建消息历史
                List<ChatCompletionRequest.Message> messages = new ArrayList<>();
                if (request.getHistory() != null) {
                    for (SimpleChatRequest.HistoryMessage history : request.getHistory()) {
                        ChatCompletionRequest.Message userMessage = new ChatCompletionRequest.Message();
                        userMessage.setRole("user");
                        userMessage.setContent(history.getQuery());
                        messages.add(userMessage);

                        ChatCompletionRequest.Message assistantMessage = new ChatCompletionRequest.Message();
                        assistantMessage.setRole("assistant");
                        assistantMessage.setContent(history.getResults().get(0).getSummary());
                        messages.add(assistantMessage);
                    }
                }
                
                // 添加当前查询
                ChatCompletionRequest.Message currentMessage = new ChatCompletionRequest.Message();
                currentMessage.setRole("user");
                currentMessage.setContent(request.getQuery());
                messages.add(currentMessage);
                apiRequest.setMessages(messages);

                // 设置请求头
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setBearerAuth(apiToken);
                headers.set("Accept", "text/event-stream");
                
                HttpEntity<ChatCompletionRequest> entity = new HttpEntity<>(apiRequest, headers);

                // 发送流式请求
                ResponseEntity<byte[]> response = restTemplate.postForEntity(apiUrl, entity, byte[].class);
                
                // 处理响应流
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getBody() != null ? 
                                new java.io.ByteArrayInputStream(response.getBody()) : 
                                new java.io.ByteArrayInputStream(new byte[0])))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("data:")) {
                            String data = line.substring(5).trim();
                            if (!"[DONE]".equals(data)) {
                                SimpleChatResponse chatResponse = new SimpleChatResponse();
                                chatResponse.setType("content");
                                chatResponse.setContent(data);
                                emitter.send(chatResponse);
                            }
                        }
                    }
                    
                    // 发送结束消息
                    SimpleChatResponse endResponse = new SimpleChatResponse();
                    endResponse.setType("end");
                    emitter.send(endResponse);
                    emitter.complete();
                }
            } catch (Exception e) {
                log.error("Stream chat failed", e);
                try {
                    SimpleChatResponse errorResponse = new SimpleChatResponse();
                    errorResponse.setType("end");
                    errorResponse.setError(e.getMessage());
                    emitter.send(errorResponse);
                    emitter.complete();
                } catch (IOException ex) {
                    emitter.completeWithError(ex);
                }
            }
        });
        
        return emitter;
    }
}