# QDrant_CAS_Rag

**QDrant_CAS_Rag** is a lightweight, terminal-based Retrieval-Augmented Generation (RAG) application built with **Spring Boot** and **Spring AI**, utilizing **Qdrant** as the vector database for efficient document search and retrieval. This project was optimized to handle large documents with minimal cost by implementing advanced token-based chunking and embedding strategies.

---

## 🌐 Overview

This project demonstrates how a developer can:

- **Embed a large document** (e.g., a 974-page Spring Boot reference guide)
- **Store it efficiently** using **Qdrant**, an open-source vector store
- **Query it intelligently** using **Spring AI** with OpenAI embeddings
- **Reduce API cost by ~45%** through chunk optimization

---

## 🧠 Core Technologies Used

| Layer                  | Technology                        |
|------------------------|-----------------------------------|
| Programming Language   | Java 17                           |
| Framework              | Spring Boot, Spring AI            |
| Vector Store           | Qdrant (Dockerized)               |
| Embedding Model        | OpenAI (`text-embedding-ada-002`) |
| Text Splitting         | TokenTextSplitter (custom logic)  |
| Build Tool             | Maven                             |
| Containerization       | Docker                            |

---

## ⚙️ Features

- **Optimized Token Chunking** using `TokenTextSplitter` to minimize cost and maximize retrieval performance
- **Vector Store Integration** using Qdrant to store and search document chunks
- **Cost-Efficient Embeddings** by reducing token usage by approximately 45%
- **Terminal Interface** for real-time chat with the ingested document
- **Fast, minimal footprint backend** using only necessary Spring dependencies

---

## 🚀 Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/<your-username>/QDrant_CAS_Rag.git
cd QDrant_CAS_Rag
```

### 2. Build the Application

```bash
./mvnw clean install
```

### 3. Start Qdrant via Docker

```bash
docker run -p 6333:6333 -p 6334:6334 qdrant/qdrant
```

### 4. Run the Application

```bash
java -jar target/qdrant-cas-rag-0.0.1-SNAPSHOT.jar
```

### 5. Interact via Terminal

Once running, input your queries in the terminal to receive relevant answers from the embedded Spring Boot documentation.

---

## 📦 Docker Support

You can also run the entire app in a container.

### Build Docker Image

```bash
docker build -t qdrant-rag-app .
```

### Run the Container

```bash
docker run -it qdrant-rag-app
```

---

## 📉 Cost Optimization Summary

### Embedding a 974-page PDF

| Metric                    | Value                          |
|---------------------------|---------------------------------|
| Estimated Tokens          | ~1.1 million                   |
| Raw Cost (OpenAI)         | ~$0.11                         |
| Optimized Final Cost      | **~$0.05 – $0.06**             |
| Cost Reduction Achieved   | **~45%**                       |

> Achieved through:
> - Intelligent page filtering
> - Token-based chunking (~450 tokens per chunk)
> - 100-token overlaps to improve context linking

For detailed cost breakdown and charts:  
📄 [View Full Cost Analysis »](#cost-analysis)

---

## 📊 Cost Analysis

### Experimental Setup

- **Document**: 974-page Spring Boot Manual
- **Text Splitter**: `TokenTextSplitter`
- **Embedding Model**: `text-embedding-ada-002`
- **Chunk Configuration**:
  - `maxTokens = 500`
  - `minTokens = 50`
  - `chunkOverlap = 100`

### Chunking Summary

| Parameter               | Value             |
|------------------------|-------------------|
| Avg. tokens per chunk  | ~450              |
| Total estimated chunks | ~2,300            |
| Total tokens embedded  | ~1.1 million      |

### Cost Comparison

| Metric                    | No Chunking          | With Token Splitter     |
|---------------------------|----------------------|--------------------------|
| Avg. tokens per chunk     | ~800–1200            | ~400–500                |
| Control over chunk size   | ❌                   | ✅                       |
| Retrieval performance     | ⚠️ Low               | ✅ High                  |
| Total estimated cost      | ~$0.10–$0.15         | ✅ ~$0.05–$0.06          |
| Suitable for large docs   | ⚠️ Not Recommended   | ✅ Ideal                 |

---

## Image to describe cost reduction
![Cost Analysis](https://github.com/LAKSHYA1509/QDrant_CAS_Rag/blob/main/src/main/resources/prompts/a4c30c82-b5ed-4e1b-a6f5-40abdb192510.png?raw=true)

## 📌 Use Cases

- **Educational AI Tools**: Embed textbooks, manuals, and allow question-answering.
- **Internal Documentation Assistant**: Quickly query lengthy tech documentation.
- **Legal/Policy Search**: Replace keyword-based search with vector-based intelligent matching.

---

## 🛠️ Future Enhancements

- GUI with Vaadin or React frontend
- Support for multiple documents
- Switchable models (e.g., HuggingFace, Ollama)
- Multi-language document support

---

## 📄 License

This project is open-source and available under the MIT License.

---

