package com.example.CAS.RagTest;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.shell.command.annotation.Command;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.List;

@Command
public class SpringAssistantCommand {

    private final ChatClient chatClient;

    @Value("classpath:/prompts/spring-boot-reference.st")
    private Resource sbPromptTemplate;

    public SpringAssistantCommand(ChatClient.Builder builder, VectorStore vectorStore) {
        SearchRequest searchRequest = SearchRequest.builder()
                .topK(3)
                .build();

        this.chatClient = builder
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore, searchRequest))
                .build();
    }

    @Command(command = "q")
    public String question(@DefaultValue(value = "What is Spring Boot") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
