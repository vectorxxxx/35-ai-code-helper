package xyz.funnyboy.aicodehelper.mcp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.http.HttpMcpTransport;

@Configuration
public class McpConfig
{
    private static final String URL = "https://open.bigmodel.cn/api/mcp/web_search/sse?Authorization=";

    @Value("${bigmodel.api-key}")
    private String apiKey;

    @Bean
    public McpToolProvider mcpToolProvider() {
        // 和MCP服务通讯
        final McpTransport transport = new HttpMcpTransport.Builder().sseUrl(URL + apiKey).logRequests(true)
                .logResponses(true).build();
        // 创建MCP客户端
        final McpClient mcpClient = new DefaultMcpClient.Builder().key("vectorxMcpClient").transport(transport).build();
        // 从MCP客户端获取工具
        return McpToolProvider.builder().mcpClients(mcpClient).build();
    }
}
