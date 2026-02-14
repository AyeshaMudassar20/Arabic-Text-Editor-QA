package business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import bll.EditorBO;
import dal.IFacadeDAO;
import dto.Documents;
import dto.Pages;

/**
 * Tests for EditorBO - Business Object Layer
 * 
 * Tests the core business logic including:
 * - CRUD operations (Create, Read, Update, Delete)
 * - File import functionality
 * - Text processing commands
 * - Error handling and exception scenarios
 */
@DisplayName("EditorBO - Business Object Core Logic Tests")
public class EditorBOTest {

    @Mock
    private IFacadeDAO mockDao;
    
    private EditorBO editorBO;
    private AutoCloseable closeable;
    
    @TempDir
    File tempDir;
    
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        editorBO = new EditorBO(mockDao);
    }
    
    @AfterEach
    void tearDown() throws Exception {
        if (closeable != null) {
            closeable.close();
        }
    }

    // ==================== CREATE FILE TESTS ====================
    
    @Test
    @DisplayName("Positive: Create file successfully")
    void testCreateFile_Success() throws Exception {
        // Arrange
        String fileName = "test.txt";
        String content = "Test content";
        when(mockDao.createFileInDB(fileName, content)).thenReturn(true);
        
        // Act
        boolean result = editorBO.createFile(fileName, content);
        
        // Assert
        assertTrue(result, "File creation should succeed");
        verify(mockDao, times(1)).createFileInDB(fileName, content);
    }
    
    @Test
    @DisplayName("Negative: Create file fails when DAO throws exception")
    void testCreateFile_DaoException() throws Exception {
        // Arrange
        when(mockDao.createFileInDB(anyString(), anyString()))
            .thenThrow(new RuntimeException("Database error"));
        
        // Act
        boolean result = editorBO.createFile("test.txt", "content");
        
        // Assert
        assertFalse(result, "Should return false when exception occurs");
    }
    
    @Test
    @DisplayName("Boundary: Create file with empty content")
    void testCreateFile_EmptyContent() throws Exception {
        // Arrange
        when(mockDao.createFileInDB(anyString(), eq(""))).thenReturn(true);
        
        // Act
        boolean result = editorBO.createFile("empty.txt", "");
        
        // Assert
        assertTrue(result);
        verify(mockDao).createFileInDB("empty.txt", "");
    }
    
    @Test
    @DisplayName("Boundary: Create file with very long content")
    void testCreateFile_LongContent() throws Exception {
        // Arrange
        String longContent = "A".repeat(10000);
        when(mockDao.createFileInDB(anyString(), eq(longContent))).thenReturn(true);
        
        // Act
        boolean result = editorBO.createFile("large.txt", longContent);
        
        // Assert
        assertTrue(result);
    }

    // ==================== UPDATE FILE TESTS ====================
    
    @Test
    @DisplayName("Positive: Update file successfully")
    void testUpdateFile_Success() throws Exception {
        // Arrange
        int fileId = 1;
        String fileName = "updated.txt";
        int pageNumber = 1;
        String content = "Updated content";
        when(mockDao.updateFileInDB(fileId, fileName, pageNumber, content)).thenReturn(true);
        
        // Act
        boolean result = editorBO.updateFile(fileId, fileName, pageNumber, content);
        
        // Assert
        assertTrue(result);
        verify(mockDao).updateFileInDB(fileId, fileName, pageNumber, content);
    }
    
    @Test
    @DisplayName("Negative: Update non-existent file")
    void testUpdateFile_NonExistent() throws Exception {
        // Arrange
        when(mockDao.updateFileInDB(anyInt(), anyString(), anyInt(), anyString()))
            .thenReturn(false);
        
        // Act
        boolean result = editorBO.updateFile(999, "missing.txt", 1, "content");
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    @DisplayName("Negative: Update file with exception")
    void testUpdateFile_Exception() throws Exception {
        // Arrange
        when(mockDao.updateFileInDB(anyInt(), anyString(), anyInt(), anyString()))
            .thenThrow(new RuntimeException("Update failed"));
        
        // Act
        boolean result = editorBO.updateFile(1, "test.txt", 1, "content");
        
        // Assert
        assertFalse(result, "Should handle exception gracefully");
    }
    
    @Test
    @DisplayName("Boundary: Update with page number 0")
    void testUpdateFile_PageNumberZero() throws Exception {
        // Arrange
        when(mockDao.updateFileInDB(1, "test.txt", 0, "content")).thenReturn(true);
        
        // Act
        boolean result = editorBO.updateFile(1, "test.txt", 0, "content");
        
        // Assert
        assertTrue(result);
    }

    // ==================== DELETE FILE TESTS ====================
    
    @Test
    @DisplayName("Positive: Delete file successfully")
    void testDeleteFile_Success() throws Exception {
        // Arrange
        int fileId = 5;
        when(mockDao.deleteFileInDB(fileId)).thenReturn(true);
        
        // Act
        boolean result = editorBO.deleteFile(fileId);
        
        // Assert
        assertTrue(result);
        verify(mockDao).deleteFileInDB(fileId);
    }
    
    @Test
    @DisplayName("Negative: Delete non-existent file")
    void testDeleteFile_NonExistent() throws Exception {
        // Arrange
        when(mockDao.deleteFileInDB(999)).thenReturn(false);
        
        // Act
        boolean result = editorBO.deleteFile(999);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    @DisplayName("Negative: Delete file with negative ID")
    void testDeleteFile_NegativeId() throws Exception {
        // Arrange
        when(mockDao.deleteFileInDB(-1)).thenReturn(false);
        
        // Act
        boolean result = editorBO.deleteFile(-1);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    @DisplayName("Negative: Delete fails with exception")
    void testDeleteFile_Exception() throws Exception {
        // Arrange
        when(mockDao.deleteFileInDB(anyInt()))
            .thenThrow(new RuntimeException("Delete failed"));
        
        // Act
        boolean result = editorBO.deleteFile(1);
        
        // Assert
        assertFalse(result);
    }

    // ==================== IMPORT FILE TESTS ====================
    
    @Test
    @DisplayName("Positive: Import .txt file successfully")
    void testImportTextFile_TxtSuccess() throws Exception {
        // Arrange
        File testFile = new File(tempDir, "test.txt");
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("Line 1\nLine 2\nLine 3");
        }
        
        when(mockDao.createFileInDB(eq("test.txt"), anyString())).thenReturn(true);
        
        // Act
        boolean result = editorBO.importTextFiles(testFile, "test.txt");
        
        // Assert
        assertTrue(result);
        verify(mockDao).createFileInDB(eq("test.txt"), contains("Line 1"));
    }
    
    @Test
    @DisplayName("Positive: Import .md5 file successfully")
    void testImportTextFile_Md5Success() throws Exception {
        // Arrange
        File testFile = new File(tempDir, "document.md5");
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("# Markdown Content\n## Header");
        }
        
        when(mockDao.createFileInDB(eq("document.md5"), anyString())).thenReturn(true);
        
        // Act
        boolean result = editorBO.importTextFiles(testFile, "document.md5");
        
        // Assert
        assertTrue(result);
        verify(mockDao).createFileInDB(eq("document.md5"), contains("Markdown"));
    }
    
    @Test
    @DisplayName("Negative: Import non-existent file")
    void testImportTextFile_NonExistent() {
        // Arrange
        File nonExistentFile = new File(tempDir, "missing.txt");
        
        // Act
        boolean result = editorBO.importTextFiles(nonExistentFile, "missing.txt");
        
        // Assert
        assertFalse(result, "Should return false for non-existent file");
    }
    
    @Test
    @DisplayName("Negative: Import unsupported file type")
    void testImportTextFile_UnsupportedType() throws IOException {
        // Arrange
        File pdfFile = new File(tempDir, "document.pdf");
        pdfFile.createNewFile();
        
        // Act
        boolean result = editorBO.importTextFiles(pdfFile, "document.pdf");
        
        // Assert
        assertFalse(result, "Should not import unsupported file types");
    }
    
    @Test
    @DisplayName("Boundary: Import empty file")
    void testImportTextFile_Empty() throws Exception {
        // Arrange
        File emptyFile = new File(tempDir, "empty.txt");
        emptyFile.createNewFile();
        
        when(mockDao.createFileInDB(eq("empty.txt"), anyString())).thenReturn(true);
        
        // Act
        boolean result = editorBO.importTextFiles(emptyFile, "empty.txt");
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    @DisplayName("Boundary: Import large file")
    void testImportTextFile_Large() throws Exception {
        // Arrange
        File largeFile = new File(tempDir, "large.txt");
        try (FileWriter writer = new FileWriter(largeFile)) {
            for (int i = 0; i < 1000; i++) {
                writer.write("This is line " + i + " of a large file.\n");
            }
        }
        
        when(mockDao.createFileInDB(eq("large.txt"), anyString())).thenReturn(true);
        
        // Act
        boolean result = editorBO.importTextFiles(largeFile, "large.txt");
        
        // Assert
        assertTrue(result);
    }

    // ==================== GET FILE TESTS ====================
    
    @Test
    @DisplayName("Positive: Get file by ID successfully")
    void testGetFile_Success() throws Exception {
        // Arrange
        int fileId = 1;
        Documents mockDoc = new Documents();
        mockDoc.setId(fileId);
        mockDoc.setName("test.txt");
        
        when(mockDao.getFileFromDB(fileId)).thenReturn(mockDoc);
        
        // Act
        Documents result = editorBO.getFile(fileId);
        
        // Assert
        assertNotNull(result);
        assertEquals(fileId, result.getId());
        assertEquals("test.txt", result.getName());
    }
    
    @Test
    @DisplayName("Negative: Get non-existent file returns null")
    void testGetFile_NotFound() throws Exception {
        // Arrange
        when(mockDao.getFileFromDB(999)).thenReturn(null);
        
        // Act
        Documents result = editorBO.getFile(999);
        
        // Assert
        assertNull(result);
    }

    // ==================== GET ALL FILES TESTS ====================
    
    @Test
    @DisplayName("Positive: Get all files successfully")
    void testGetAllFiles_Success() throws Exception {
        // Arrange
        List<Documents> mockDocs = new ArrayList<>();
        mockDocs.add(createMockDocument(1, "file1.txt"));
        mockDocs.add(createMockDocument(2, "file2.txt"));
        
        when(mockDao.getFilesFromDB()).thenReturn(mockDocs);
        
        // Act
        List<Documents> result = editorBO.getAllFiles();
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("file1.txt", result.get(0).getName());
    }
    
    @Test
    @DisplayName("Negative: Get all files when DB is empty")
    void testGetAllFiles_Empty() throws Exception {
        // Arrange
        when(mockDao.getFilesFromDB()).thenReturn(new ArrayList<>());
        
        // Act
        List<Documents> result = editorBO.getAllFiles();
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ==================== FILE EXTENSION TESTS ====================
    
    @Test
    @DisplayName("Positive: Get file extension from .txt file")
    void testGetFileExtension_Txt() {
        // Act
        String extension = editorBO.getFileExtension("document.txt");
        
        // Assert
        assertEquals("txt", extension);
    }
    
    @Test
    @DisplayName("Positive: Get file extension from .md5 file")
    void testGetFileExtension_Md5() {
        // Act
        String extension = editorBO.getFileExtension("readme.md5");
        
        // Assert
        assertEquals("md5", extension);
    }
    
    @Test
    @DisplayName("Boundary: File with no extension")
    void testGetFileExtension_NoExtension() {
        // Act
        String extension = editorBO.getFileExtension("filename");
        
        // Assert
        assertEquals("", extension);
    }
    
    @Test
    @DisplayName("Boundary: File with multiple dots")
    void testGetFileExtension_MultipleDots() {
        // Act
        String extension = editorBO.getFileExtension("my.file.name.txt");
        
        // Assert
        assertEquals("txt", extension);
    }

    // ==================== SEARCH KEYWORD TESTS ====================
    
    @Test
    @DisplayName("Positive: Search keyword finds results")
    void testSearchKeyword_FoundResults() throws Exception {
        // Arrange
        List<String> mockResults = new ArrayList<>();
        mockResults.add("file1.txt - keyword found");
        mockResults.add("file2.txt - another keyword");
        
        List<Documents> mockDocs = new ArrayList<>();
        mockDocs.add(createMockDocument(1, "file1.txt"));
        
        when(mockDao.getFilesFromDB()).thenReturn(mockDocs);
        
        // Act
        List<String> results = editorBO.searchKeyword("keyword");
        
        // Assert
        assertNotNull(results);
    }
    
    @Test
    @DisplayName("Negative: Search with short keyword throws exception")
    void testSearchKeyword_TooShort() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            editorBO.searchKeyword("ab");
        }, "Should throw exception for keywords < 3 characters");
    }

    // ==================== TEXT PROCESSING TESTS ====================
    
    @Test
    @DisplayName("Positive: Lemmatize words successfully")
    void testLemmatizeWords_Success() throws Exception {
        // Arrange
        Map<String, String> mockLemmas = new HashMap<>();
        mockLemmas.put("running", "run");
        mockLemmas.put("flies", "fly");
        
        when(mockDao.lemmatizeWords(anyString())).thenReturn(mockLemmas);
        
        // Act
        Map<String, String> result = editorBO.lemmatizeWords("running flies");
        
        // Assert
        assertNotNull(result);
        assertEquals("run", result.get("running"));
    }
    
    @Test
    @DisplayName("Positive: Extract POS tags successfully")
    void testExtractPOS_Success() throws Exception {
        // Arrange
        Map<String, List<String>> mockPOS = new HashMap<>();
        mockPOS.put("NOUN", List.of("cat", "dog"));
        mockPOS.put("VERB", List.of("run", "jump"));
        
        when(mockDao.extractPOS(anyString())).thenReturn(mockPOS);
        
        // Act
        Map<String, List<String>> result = editorBO.extractPOS("The cat runs");
        
        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("NOUN"));
    }

    // ==================== HELPER METHODS ====================
    
    private Documents createMockDocument(int id, String name) {
        Documents doc = new Documents();
        doc.setId(id);
        doc.setName(name);
        
        Pages page = new Pages();
        page.setPageNumber(1);
        page.setPageContent("Test content");
        
        List<Pages> pages = new ArrayList<>();
        pages.add(page);
        doc.setPages(pages);
        
        return doc;
    }
}
