package xyz.funnyboy.aicodehelper.guardrail;

import java.util.Set;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailResult;

public class SafeInputGuardrail implements InputGuardrail
{
    private static final Set<String> sensitiveWords = Set.of("kill", "evil");

    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        for (final String word : userMessage.singleText().toLowerCase().split("\\W+")) {
            if (sensitiveWords.contains(word)) {
                return fatal("Sensitive word detected: " + word);
            }
        }
        return success();
    }
}
