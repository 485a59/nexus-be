package cn.edu.nwafu.nexus.infrastructure.model.dto.chat;

import lombok.Data;

import java.util.List;

@Data
public class ChatCompletionRequest {
    private String model = "deepseek-ai/DeepSeek-V3";
    private List<Message> messages;
    private boolean stream = false;
    private Integer maxTokens = 512;
    private List<String> stop;
    private Double temperature = 0.7;
    private Double topP = 0.7;
    private Integer topK = 50;
    private Double frequencyPenalty = 0.5;
    private Integer n = 1;
    private ResponseFormat responseFormat;
    private List<Tool> tools;

    @Data
    public static class Message {
        private String role;
        private String content;
    }

    @Data
    public static class ResponseFormat {
        private String type;
    }

    @Data
    public static class Tool {
        private String type;
        private Function function;
    }

    @Data
    public static class Function {
        private String description;
        private String name;
        private Object parameters;
        private boolean strict = false;
    }
} 