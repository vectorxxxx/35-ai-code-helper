package xyz.funnyboy.aicodehelper.ai;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import jakarta.annotation.Resource;
import xyz.funnyboy.aicodehelper.tool.InterviewQuestionTool;

@Configuration
public class AiCodeHelperServiceFactory
{
    @Resource
    private ChatModel qwenChatModel;

    /**
     * 注入自定义模型
     */
    @Resource
    private ChatModel myQwenChatModel;

    /**
     * 流式对话模型
     */
    @Resource
    private StreamingChatModel streamingChatModel;

    @Resource
    private EmbeddingStore<TextSegment> embeddingStore;

    @Resource
    private ContentRetriever contentRetriever;

    @Resource
    private McpToolProvider mcpToolProvider;

    @Bean
    public AiCodeHelperService aiCodeHelperService() {
        // 1. 纯对话
        // return AiServices.create(AiCodeHelperService.class, chatModel);

        // 2. 会话记忆
        // final ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        // return
        // AiServices.builder(AiCodeHelperService.class).chatModel(qwenChatModel).chatMemory(chatMemory).build();

        // 3. 持久化存储
        // final MessageWindowChatMemory chatMemory =
        // MessageWindowChatMemory.builder().maxMessages(10)
        // .chatMemoryStore(new PersistentChatMemoryStore()).build();
        // return
        // AiServices.builder(AiCodeHelperService.class).chatModel(qwenChatModel).chatMemory(chatMemory).build();

        // 4. 多用户会话记忆
        // return AiServices.builder(AiCodeHelperService.class).chatModel(qwenChatModel)
        // .chatMemoryProvider(memoryId ->
        // MessageWindowChatMemory.withMaxMessages(10)).build();

        // 5. 极简版RAG
        // // 1. 加载文档
        // final List<Document> documents =
        // FileSystemDocumentLoader.loadDocuments("src/main/resources/docs");
        // // 2. 向量转换并存储
        // final ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        // EmbeddingStoreIngestor.ingest(documents, embeddingStore);
        // return
        // AiServices.builder(AiCodeHelperService.class).chatModel(qwenChatModel).chatMemory(chatMemory)
        // .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore)).build();

        // 6. 标准版RAG
        // return AiServices.builder(AiCodeHelperService.class).chatModel(qwenChatModel)
        // .chatMemory(MessageWindowChatMemory.withMaxMessages(10)).contentRetriever(contentRetriever).build();

        // 7. 工具调用
        // return AiServices.builder(AiCodeHelperService.class).chatModel(qwenChatModel)
        // .chatMemory(MessageWindowChatMemory.withMaxMessages(10)).contentRetriever(contentRetriever)
        // .tools(new InterviewQuestionTool()).build();

        // 8. MCP工具
        // return
        // AiServices.builder(AiCodeHelperService.class).chatModel(myQwenChatModel)
        // .chatMemory(MessageWindowChatMemory.withMaxMessages(10)).contentRetriever(contentRetriever)
        // .tools(new InterviewQuestionTool()).toolProvider(mcpToolProvider).build();

        // 9. 流式对话
        return AiServices.builder(AiCodeHelperService.class).chatModel(myQwenChatModel)
                // 流式对话模型
                .streamingChatModel(streamingChatModel).chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                // 每个会话独立存储
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                // RAG 检索增强生成
                .contentRetriever(contentRetriever)
                // 工具调用
                .tools(new InterviewQuestionTool())
                // MCP 工具调用
                .toolProvider(mcpToolProvider).build();
    }
}
