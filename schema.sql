-- Arabic Text Editor Database Schema
-- MariaDB 10.x+
-- Character Set: UTF8MB4 (for Arabic support)

CREATE DATABASE IF NOT EXISTS arabic_editor_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE arabic_editor_db;

-- Files table: Stores document metadata
CREATE TABLE IF NOT EXISTS files (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fileName VARCHAR(255) NOT NULL,
    fileHash VARCHAR(64),
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_fileName (fileName)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Pages table: Stores paginated content (100 chars per page)
CREATE TABLE IF NOT EXISTS pages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fileId INT NOT NULL,
    pageNumber INT NOT NULL,
    pageContent TEXT,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (fileId) REFERENCES files(id) ON DELETE CASCADE,
    INDEX idx_fileId (fileId),
    INDEX idx_pageNumber (pageNumber)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Transliteration table: Arabic to Latin transliteration
CREATE TABLE IF NOT EXISTS transliteratedpages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pageId INT NOT NULL,
    transliteratedText TEXT,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pageId) REFERENCES pages(id) ON DELETE CASCADE,
    INDEX idx_pageId (pageId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- POS (Part-of-Speech) tagging table
CREATE TABLE IF NOT EXISTS pos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pageId INT NOT NULL,
    word VARCHAR(100),
    pos VARCHAR(50),
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pageId) REFERENCES pages(id) ON DELETE CASCADE,
    INDEX idx_pageId (pageId),
    INDEX idx_word (word)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Lemmatization table: Word lemmas
CREATE TABLE IF NOT EXISTS lemmatization (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pageId INT NOT NULL,
    word VARCHAR(100),
    lemma VARCHAR(100),
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pageId) REFERENCES pages(id) ON DELETE CASCADE,
    INDEX idx_pageId (pageId),
    INDEX idx_word (word)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Root extraction table
CREATE TABLE IF NOT EXISTS rootextraction (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pageId INT NOT NULL,
    word VARCHAR(100),
    root VARCHAR(100),
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pageId) REFERENCES pages(id) ON DELETE CASCADE,
    INDEX idx_pageId (pageId),
    INDEX idx_word (word)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Stemming table
CREATE TABLE IF NOT EXISTS stemmation (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pageId INT NOT NULL,
    word VARCHAR(100),
    stem VARCHAR(100),
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pageId) REFERENCES pages(id) ON DELETE CASCADE,
    INDEX idx_pageId (pageId),
    INDEX idx_word (word)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Word segmentation table
CREATE TABLE IF NOT EXISTS wordsegementation (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pageId INT NOT NULL,
    word VARCHAR(100),
    segment VARCHAR(200),
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pageId) REFERENCES pages(id) ON DELETE CASCADE,
    INDEX idx_pageId (pageId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- TF-IDF scores table
CREATE TABLE IF NOT EXISTS tfidf (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fileId INT NOT NULL,
    tfidfScore DOUBLE,
    calculatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (fileId) REFERENCES files(id) ON DELETE CASCADE,
    INDEX idx_fileId (fileId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- PMI (Pointwise Mutual Information) scores
CREATE TABLE IF NOT EXISTS pmi (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pageId INT NOT NULL,
    word VARCHAR(100),
    pmiScore DOUBLE,
    calculatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pageId) REFERENCES pages(id) ON DELETE CASCADE,
    INDEX idx_pageId (pageId),
    INDEX idx_word (word)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- PKL (Perplexity-based Language Model) scores
CREATE TABLE IF NOT EXISTS pkl (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pageId INT NOT NULL,
    word VARCHAR(100),
    pklScore DOUBLE,
    calculatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pageId) REFERENCES pages(id) ON DELETE CASCADE,
    INDEX idx_pageId (pageId),
    INDEX idx_word (word)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert sample data for testing
INSERT INTO files (fileName, fileHash) VALUES 
('sample.txt', 'abc123'),
('test.md5', 'def456');

INSERT INTO pages (fileId, pageNumber, pageContent) VALUES
(1, 1, 'This is the first page content with exactly one hundred characters for pagination testing purposes...'),
(1, 2, 'Second page content'),
(2, 1, 'مرحبا بك في محرر النصوص العربي');

-- Verify installation
SELECT 'Database setup complete!' AS Status;
SELECT COUNT(*) AS TotalFiles FROM files;
SELECT COUNT(*) AS TotalPages FROM pages;
