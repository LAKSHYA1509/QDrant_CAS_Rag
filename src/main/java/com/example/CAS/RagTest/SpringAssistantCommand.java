package com.example.CAS.RagTest;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command
public class SpringAssistantCommand {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final PromptTemplate promptTemplate;

    public SpringAssistantCommand(ChatClient.Builder chatClientBuilder,
            VectorStore vectorStore,
            @Value("classpath:/prompts/spring-boot-reference.st") Resource sbPromptTemplate) {
        this.chatClient = chatClientBuilder.build();
        this.vectorStore = vectorStore;
        this.promptTemplate = new PromptTemplate(sbPromptTemplate);
    }

    @Command(command = "ask")
    public String ask(String message) {
        Map<String, Object> promptParams = new HashMap<>();
        promptParams.put("input", message);
        promptParams.put("documents", String.join("\n---\n", findSimilarDocuments(message)));

        Prompt prompt = promptTemplate.create(promptParams);

        return chatClient.prompt(prompt).call().content();
    }

    private List<String> findSimilarDocuments(String message) {
        List<Document> similarDocs = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(message)
                        .topK(3)
                        .build());

        if (similarDocs == null || similarDocs.isEmpty()) {
            return List.of("No related documents found.");
        }

        return similarDocs.stream()
        .map(doc -> doc.getText())
        .toList();

    }

}
