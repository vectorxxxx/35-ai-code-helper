package xyz.funnyboy.aicodehelper.ai;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
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

    @Resource
    private EmbeddingStore<TextSegment> embeddingStore;

    @Resource
    private ContentRetriever contentRetriever;

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
        return AiServices.builder(AiCodeHelperService.class).chatModel(qwenChatModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10)).contentRetriever(contentRetriever)
                .tools(new InterviewQuestionTool()).build();
    }
}
