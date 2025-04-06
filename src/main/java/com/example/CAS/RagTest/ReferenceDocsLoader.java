package com.example.CAS.RagTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
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
                log.info("✅ PDF embeddings already exist. Skipping reloading and chunking.");
                return;
            }

            log.info("📄 Loading and embedding Spring Boot PDF into Qdrant...");

            // Configure PDF reader with optional text cleaning
            var config = PdfDocumentReaderConfig.builder()
    .withPageExtractedTextFormatter(new ExtractedTextFormatter.Builder()
        .withNumberOfBottomTextLinesToDelete(2)
        .withNumberOfTopPagesToSkipBeforeDelete(2)
        .build())
    .withPagesPerDocument(1)
    .build();


            PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(pdfResource, config);
            List<Document> allDocs = pdfReader.get();

            int limitPages = 50;
            List<Document> docs = allDocs.subList(0, Math.min(limitPages, allDocs.size()));

            var textSplitter = new TokenTextSplitter(1500, 200, 200, Integer.MAX_VALUE, false);
            List<Document> chunks = textSplitter.apply(docs);

            vectorStore.accept(chunks);
            chunks.forEach(doc -> log.info("🔹 Chunk: " + doc.getFormattedContent()));

            log.info("✅ PDF successfully embedded into Qdrant. Total chunks: {}", chunks.size());

        } catch (Exception e) {
            log.error("❌ Error loading PDF into Qdrant", e);
        }
    }

    private boolean isPdfAlreadyEmbedded() {
        try {
            List<Document> vectors = vectorStore.similaritySearch("Spring Boot");
            return !vectors.isEmpty();
        } catch (Exception e) {
            log.warn("⚠️ Error checking Qdrant for existing embeddings. Assuming it’s empty.", e);
            return false;
        }
    }
}
