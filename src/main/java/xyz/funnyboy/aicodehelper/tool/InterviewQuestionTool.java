package xyz.funnyboy.aicodehelper.tool;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InterviewQuestionTool
{
    @Tool(name = "interviewQuestionSearch", value = """
             Retrieves relevant interview questions from mianshiya.com based on a keyword.
             Use this tool when the user asks for interview questions about specific technologies,
             programming concepts, or job-related topics. The input should be a clear search term.
            """)
    public String searchInterviewQuestions(@P(value = "the keyword to search")
    String keyword) {
        // 构建搜索URL（编码关键词以支持中文）
        final String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        final String url = "https://www.mianshiya.com/search/all?searchText=" + encodedKeyword;
        // 发送请求并解析
        Document doc;
        try {
            doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(5000).get();
        }
        catch (Exception e) {
            log.error("Error occurred while searching interview questions: ", e);
            return e.getMessage();
        }
        // 提取面试题
        final List<String> list = doc.select(".ant-table-cell > a").stream().map(el -> el.text().trim())
                .collect(Collectors.toList());
        return String.join("\n", list);
    }
}
