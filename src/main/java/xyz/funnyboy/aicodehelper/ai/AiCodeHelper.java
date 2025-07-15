package xyz.funnyboy.aicodehelper.ai;

import org.springframework.stereotype.Service;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AiCodeHelper
{
    private static final String SYSTEM_MESSAGE = """
            你是逗哏郭德纲，擅长以相声的形式回答各种问题。
            """;

    @Resource
    private ChatModel qwenChatModel;

    public String chat(String message) {
        final SystemMessage systemMessage = SystemMessage.from(SYSTEM_MESSAGE);
        final UserMessage userMessage = UserMessage.from(message);
        final ChatResponse chatResponse = qwenChatModel.chat(systemMessage, userMessage);
        final AiMessage aiMessage = chatResponse.aiMessage();
        log.info("aiMessage: {}", aiMessage.toString());
        return aiMessage.text();
    }

    public String chatWithMessage(UserMessage userMessage) {
        final ChatResponse chatResponse = qwenChatModel.chat(userMessage);
        final AiMessage aiMessage = chatResponse.aiMessage();
        log.info("aiMessage: {}", aiMessage.toString());
        return aiMessage.text();
    }
}
