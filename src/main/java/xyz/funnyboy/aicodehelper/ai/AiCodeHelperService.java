package xyz.funnyboy.aicodehelper.ai;

import java.util.List;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

// @AiService
public interface AiCodeHelperService
{

    /**
     * 系统提示词
     *
     * @param userMessage
     *            用户消息
     * @return {@link String }
     */
    @SystemMessage(fromResource = "system-prompt.txt")
    String chat(String userMessage);

    /**
     * 多用户会话记忆
     *
     * @param memoryId
     *            内存 ID
     * @param userMessage
     *            用户消息
     * @return {@link String }
     */
    @SystemMessage(fromResource = "system-prompt.txt")
    String chat(@MemoryId
    int memoryId, @UserMessage
    String userMessage);

    /**
     * 结构化输出
     *
     * @param userMessage
     *            用户消息
     * @return {@link Report }
     */
    @SystemMessage(fromResource = "system-prompt.txt")
    Report chat4Report(String userMessage);

    record Report(String name, List<String> suggestionList) {
    }

    /**
     * 标准版RAG
     *
     * @param userMessage
     *            用户消息
     * @return {@link String }
     */
    @SystemMessage(fromResource = "system-prompt.txt")
    Result<String> chatWithRag(String userMessage);

}
