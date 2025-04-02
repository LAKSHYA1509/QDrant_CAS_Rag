package com.example.CAS.RagTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.qdrant.QdrantVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Component
public class ReferenceDocsLoader {

    private static final Logger log = LoggerFactory.getLogger(ReferenceDocsLoader.class);

    @Value("classpath:/docs/spring-boot-reference.pdf")
    private Resource pdfResource;

    private final QdrantVectorStore vectorStore;

    public ReferenceDocsLoader(QdrantVectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void init() {
        try {
            if (isPdfAlreadyEmbedded()) {
                log.info("PDF embeddings already exist. Skipping reloading and chunking.");
                return;
            }

            log.info("Loading PDF and inserting into Qdrant...");

            // Configure PDF reader
            var config = PdfDocumentReaderConfig.builder()
                    .withPageExtractedTextFormatter(new ExtractedTextFormatter.Builder()
                            .withNumberOfBottomTextLinesToDelete(0)
                            .withNumberOfTopPagesToSkipBeforeDelete(0)
                            .build())
                    .withPagesPerDocument(1)
                    .build();

            var pdfReader = new PagePdfDocumentReader(pdfResource, config);

            var textSplitter = new TokenTextSplitter();
            vectorStore.accept(textSplitter.apply(pdfReader.get()));

            log.info("PDF successfully stored in Qdrant!");
        } catch (Exception e) {
            log.error("Error loading PDF into Qdrant", e);
        }
    }

    private boolean isPdfAlreadyEmbedded() {
        try {
            List<?> vectors = vectorStore.similaritySearch("Spring Boot");
            return !vectors.isEmpty();
        } catch (Exception e) {
            log.warn("Error checking Qdrant for existing data, assuming it's empty.", e);
            return false;
        }
    }
}
