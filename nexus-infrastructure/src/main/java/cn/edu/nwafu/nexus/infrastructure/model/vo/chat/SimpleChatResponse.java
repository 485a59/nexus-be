package cn.edu.nwafu.nexus.infrastructure.model.vo.chat;

import lombok.Data;

@Data
public class SimpleChatResponse {
    private String type;  // 'content' 或 'end'
    private String content;
    private String error;
} 