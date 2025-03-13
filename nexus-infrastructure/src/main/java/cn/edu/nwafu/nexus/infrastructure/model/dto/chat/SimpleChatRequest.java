package cn.edu.nwafu.nexus.infrastructure.model.dto.chat;

import lombok.Data;
import java.util.List;

@Data
public class SimpleChatRequest {
    private String query;
    private String model = "dsr1";
    private List<HistoryMessage> history;

    @Data
    public static class HistoryMessage {
        private String query;
        private List<HistoryResult> results;
    }

    @Data
    public static class HistoryResult {
        private String summary;
    }
} 