package xyz.funnyboy.aicodehelper.rag;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.annotation.Resource;

@Configuration
public class RagConfig
{
    @Resource
    private EmbeddingModel embeddingModel;

    @Resource
    private EmbeddingStore<TextSegment> embeddingStore;

    @Bean
    public ContentRetriever contentRetriever() {
        // 1. 文档加载
        final List<Document> documents = FileSystemDocumentLoader.loadDocuments("src/main/resources/docs");
        // 2. 文档摄取
        final EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                // 文档切割：将每个文档按每段进行分割，最大 1000 字符，每次重叠最多 200 个字符
                .documentSplitter(new DocumentByParagraphSplitter(1000, 200))
                // 为了提高搜索质量，为每个 TextSegment 添加文档名称
                .textSegmentTransformer(textSegment -> TextSegment.from(
                        textSegment.metadata().getString("file_name") + "\n" + textSegment.text(),
                        textSegment.metadata()))
                .embeddingModel(embeddingModel).embeddingStore(embeddingStore).build();
        ingestor.ingest(documents);
        // 3. 内容检索
        return EmbeddingStoreContentRetriever.builder().embeddingModel(embeddingModel).embeddingStore(embeddingStore)
                // 最多 5 个检索结果
                .maxResults(5)
                // 过滤掉分数小于 0.75 的结果
                .minScore(0.75).build();
    }
}
