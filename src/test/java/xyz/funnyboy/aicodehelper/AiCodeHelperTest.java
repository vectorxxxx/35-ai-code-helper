package xyz.funnyboy.aicodehelper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import jakarta.annotation.Resource;
import xyz.funnyboy.aicodehelper.ai.AiCodeHelper;

@SpringBootTest(classes = AiCodeHelperApplication.class)
class AiCodeHelperTest
{

    @Resource
    private AiCodeHelper aiCodeHelper;

    @Test
    void chat() {
        aiCodeHelper.chat("我想出去看看");
    }

    @Test
    void chatWithMessage() {
        // 多模态
        final TextContent textContent = TextContent.from("解释这张图片");
        final ImageContent imageContent = ImageContent
                .from("https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png");
        final UserMessage userMessage = UserMessage.from(textContent, imageContent);
        aiCodeHelper.chatWithMessage(userMessage);
    }
}
