package xyz.funnyboy.aicodehelper;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import dev.langchain4j.rag.content.Content;
import dev.langchain4j.service.Result;
import jakarta.annotation.Resource;
import xyz.funnyboy.aicodehelper.ai.AiCodeHelperService;

@SpringBootTest
class AiCodeHelperServiceTest
{
    @Resource
    private AiCodeHelperService aiCodeHelperService;

    /**
     * 聊天
     */
    @Test
    void chat() {
        final String result = aiCodeHelperService.chat("请写一个java程序，打印1到1000的偶数");
        System.out.println(result);
    }

    /**
     * 会话记忆
     */
    @Test
    void chatWithMemory() {
        final String result = aiCodeHelperService.chat("你好，我是VectorX");
        System.out.println(result);
        System.out.println("----------------------------------------------");
        final String result2 = aiCodeHelperService.chat("我是谁");
        System.out.println(result2);
    }

    /**
     * 多用户会话记忆
     */
    @Test
    void chatWithMemoryId() {
        final String result = aiCodeHelperService.chat(1, "你好，我是VectorX");
        System.out.println(result);
        System.out.println("----------------------------------------------");
        final String result2 = aiCodeHelperService.chat(2, "我是谁");
        System.out.println(result2);
    }

    /**
     * 结构化输出
     */
    @Test
    void chat4Report() {
        String userMessage = "你好，我是VectorX，学编程两年半，请帮我制定学习报告";
        final AiCodeHelperService.Report report = aiCodeHelperService.chat4Report(userMessage);
        System.out.println(report);
    }

    /**
     * 检索增强生成
     */
    @Test
    void chatWithRAG() {
        String userMessage = "怎么学习 Java？有哪些常见面试题？";
        final Result<String> result = aiCodeHelperService.chatWithRag(userMessage);
        final String content = result.content();
        final List<Content> sources = result.sources();
        System.out.println(content);
        System.out.println(sources);
    }

    /**
     * 工具调用
     */
    @Test
    void chatWithTools() {
        String result = aiCodeHelperService.chat("有哪些常见的计算机网络面试题？");
        System.out.println(result);
    }

    /**
     * 工具调用
     */
    @Test
    void chatWithMcp() {
        String result = aiCodeHelperService.chat("什么是程序员鱼皮的编程导航？");
        System.out.println(result);
    }

    @Test
    void chatWithGuardrail() {
        String result = aiCodeHelperService.chat("kill the game");
        System.out.println(result);
    }
}
