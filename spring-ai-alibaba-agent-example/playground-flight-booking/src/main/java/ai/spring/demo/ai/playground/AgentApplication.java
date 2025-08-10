package ai.spring.demo.ai.playground;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.web.client.RestClient;

/**
 * 代理应用程序的启动入口。
 */
@SpringBootApplication
public class AgentApplication  {

	private static final Logger logger = LoggerFactory.getLogger(AgentApplication.class);

	public static void main(String[] args) {
		new SpringApplicationBuilder(AgentApplication.class).run(args);
	}

	// In the real world, ingesting documents would often happen separately, on a CI
	// server or similar.
	@Bean
	CommandLineRunner ingestTermOfServiceToVectorStore(
			VectorStore vectorStore,
			@Value("classpath:rag/terms-of-service.txt") Resource termsOfServiceDocs
	) {

		return args -> {
			// 使用 RAG 增加机票退改签规则
			// Ingest the document into the vector store
			/*
			 * 1、文档读取 TextReader 读取 resources/rag/terms-of-service.txt 文件内容
			 * 2、TokenTextSplitter 按token长度切分文本（避免大文本超出模型限制）
			 * 3、向量化存储 通过 VectorStore.write() 将文本向量存入内存（后续可用于RAG检索）
			 */
			vectorStore.write(new TokenTextSplitter().transform(new TextReader(termsOfServiceDocs).read()));

			// 相似性搜索
			vectorStore.similaritySearch("Cancelling Bookings").forEach(doc -> {
				logger.info("Similar Document: {}", doc.getText());
			});
		};
	}

	/**
	 * 提供基于内存的向量存储（SimpleVectorStore）
	 * <p>
	 * 依赖 EmbeddingModel（自动注入，Alibaba的嵌入模型）
	 * @param embeddingModel
	 * @return
	 */
	@Bean
	public VectorStore vectorStore(EmbeddingModel embeddingModel) {
		// 基于内存的向量存储
		return SimpleVectorStore.builder(embeddingModel).build();
	}

	/**
	 * 存储多轮对话历史（基于内存）
	 * 实现上下文感知的连续对话
	 * @return
	 */
	@Bean
	public ChatMemory chatMemory() {
		// 多轮对话历史
		return MessageWindowChatMemory.builder().build();
	}

	/**
	 * 提供可自定义的HTTP客户端（用于调用外部API）
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public RestClient.Builder restClientBuilder() {
		return RestClient.builder();
	}

}
