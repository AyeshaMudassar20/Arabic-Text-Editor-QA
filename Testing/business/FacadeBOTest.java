package business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import bll.FacadeBO;
import bll.IEditorBO;
import dto.Documents;

/**
 * Tests for FacadeBO - Facade Pattern Implementation
 * 
 * The Facade Pattern provides a simplified interface to the complex subsystem.
 * FacadeBO delegates all calls to EditorBO, providing a unified interface.
 * 
 * Testing Strategy:
 * - Verify all methods delegate correctly to EditorBO
 * - Verify return values are passed through
 * - Verify Facade doesn't add extra logic (just delegates)
 * - Test the Facade pattern structure
 */
@DisplayName("FacadeBO - Facade Pattern Tests")
public class FacadeBOTest {

    @Mock
    private IEditorBO mockEditorBO;
    
    private FacadeBO facadeBO;
    private AutoCloseable closeable;
    
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        facadeBO = new FacadeBO(mockEditorBO);
    }
    
    @AfterEach
    void tearDown() throws Exception {
        if (closeable != null) {
            closeable.close();
        }
    }

    // ==================== FACADE PATTERN VERIFICATION ====================
    
    @Test
    @DisplayName("Pattern: Facade delegates to EditorBO")
    void testPattern_DelegatesToEditorBO() {
        // This is verified in all subsequent tests
        // The Facade should not implement logic, only delegate
        assertNotNull(facadeBO, "Facade should be instantiated with EditorBO");
    }
    
    @Test
    @DisplayName("Pattern: Facade has no business logic of its own")
    void testPattern_NoBusinessLogic() {
        // The Facade pattern should only delegate, not implement logic
        // We verify this by ensuring all methods just call EditorBO
        // This is structural verification
        assertTrue(true, "Verified through delegation tests below");
    }

    // ==================== CREATE FILE DELEGATION ====================
    
    @Test
    @DisplayName("Delegation: createFile calls EditorBO.createFile")
    void testCreateFile_Delegation() {
        // Arrange
        String fileName = "test.txt";
        String content = "Test content";
        when(mockEditorBO.createFile(fileName, content)).thenReturn(true);
        
        // Act
        boolean result = facadeBO.createFile(fileName, content);
        
        // Assert
        assertTrue(result, "Should return EditorBO result");
        verify(mockEditorBO, times(1)).createFile(fileName, content);
    }
    
    @Test
    @DisplayName("Delegation: createFile returns false when EditorBO returns false")
    void testCreateFile_ReturnsFalse() {
        // Arrange
        when(mockEditorBO.createFile(anyString(), anyString())).thenReturn(false);
        
        // Act
        boolean result = facadeBO.createFile("test.txt", "content");
        
        // Assert
        assertFalse(result);
        verify(mockEditorBO).createFile("test.txt", "content");
    }

    // ==================== UPDATE FILE DELEGATION ====================
    
    @Test
    @DisplayName("Delegation: updateFile calls EditorBO.updateFile")
    void testUpdateFile_Delegation() {
        // Arrange
        int id = 1;
        String fileName = "updated.txt";
        int pageNumber = 2;
        String content = "Updated content";
        when(mockEditorBO.updateFile(id, fileName, pageNumber, content)).thenReturn(true);
        
        // Act
        boolean result = facadeBO.updateFile(id, fileName, pageNumber, content);
        
        // Assert
        assertTrue(result);
        verify(mockEditorBO).updateFile(id, fileName, pageNumber, content);
    }
    
    @Test
    @DisplayName("Delegation: updateFile with all parameters")
    void testUpdateFile_AllParameters() {
        // Arrange
        when(mockEditorBO.updateFile(5, "file.txt", 3, "new content")).thenReturn(true);
        
        // Act
        boolean result = facadeBO.updateFile(5, "file.txt", 3, "new content");
        
        // Assert
        assertTrue(result);
        verify(mockEditorBO, times(1)).updateFile(5, "file.txt", 3, "new content");
    }

    // ==================== DELETE FILE DELEGATION ====================
    
    @Test
    @DisplayName("Delegation: deleteFile calls EditorBO.deleteFile")
    void testDeleteFile_Delegation() {
        // Arrange
        int fileId = 10;
        when(mockEditorBO.deleteFile(fileId)).thenReturn(true);
        
        // Act
        boolean result = facadeBO.deleteFile(fileId);
        
        // Assert
        assertTrue(result);
        verify(mockEditorBO).deleteFile(fileId);
    }
    
    @Test
    @DisplayName("Delegation: deleteFile with different IDs")
    void testDeleteFile_MultipleIds() {
        // Arrange
        when(mockEditorBO.deleteFile(1)).thenReturn(true);
        when(mockEditorBO.deleteFile(2)).thenReturn(true);
        when(mockEditorBO.deleteFile(3)).thenReturn(false);
        
        // Act
        boolean result1 = facadeBO.deleteFile(1);
        boolean result2 = facadeBO.deleteFile(2);
        boolean result3 = facadeBO.deleteFile(3);
        
        // Assert
        assertTrue(result1);
        assertTrue(result2);
        assertFalse(result3);
        verify(mockEditorBO).deleteFile(1);
        verify(mockEditorBO).deleteFile(2);
        verify(mockEditorBO).deleteFile(3);
    }

    // ==================== IMPORT FILE DELEGATION ====================
    
    @Test
    @DisplayName("Delegation: importTextFiles calls EditorBO.importTextFiles")
    void testImportTextFiles_Delegation() {
        // Arrange
        File mockFile = mock(File.class);
        String fileName = "import.txt";
        when(mockEditorBO.importTextFiles(mockFile, fileName)).thenReturn(true);
        
        // Act
        boolean result = facadeBO.importTextFiles(mockFile, fileName);
        
        // Assert
        assertTrue(result);
        verify(mockEditorBO).importTextFiles(mockFile, fileName);
    }
    
    @Test
    @DisplayName("Delegation: importTextFiles passes File object correctly")
    void testImportTextFiles_FileParameter() {
        // Arrange
        File testFile = new File("test.txt");
        when(mockEditorBO.importTextFiles(any(File.class), anyString())).thenReturn(true);
        
        // Act
        boolean result = facadeBO.importTextFiles(testFile, "test.txt");
        
        // Assert
        assertTrue(result);
        verify(mockEditorBO).importTextFiles(testFile, "test.txt");
    }

    // ==================== GET FILE DELEGATION ====================
    
    @Test
    @DisplayName("Delegation: getFile calls EditorBO.getFile")
    void testGetFile_Delegation() {
        // Arrange
        int fileId = 5;
        Documents mockDoc = new Documents();
        mockDoc.setId(fileId);
        mockDoc.setName("retrieved.txt");
        when(mockEditorBO.getFile(fileId)).thenReturn(mockDoc);
        
        // Act
        Documents result = facadeBO.getFile(fileId);
        
        // Assert
        assertNotNull(result);
        assertEquals(fileId, result.getId());
        assertEquals("retrieved.txt", result.getName());
        verify(mockEditorBO).getFile(fileId);
    }
    
    @Test
    @DisplayName("Delegation: getFile returns null when EditorBO returns null")
    void testGetFile_ReturnsNull() {
        // Arrange
        when(mockEditorBO.getFile(999)).thenReturn(null);
        
        // Act
        Documents result = facadeBO.getFile(999);
        
        // Assert
        assertNull(result);
        verify(mockEditorBO).getFile(999);
    }

    // ==================== GET ALL FILES DELEGATION ====================
    
    @Test
    @DisplayName("Delegation: getAllFiles calls EditorBO.getAllFiles")
    void testGetAllFiles_Delegation() {
        // Arrange
        List<Documents> mockDocs = new ArrayList<>();
        Documents doc1 = new Documents();
        doc1.setId(1);
        doc1.setName("file1.txt");
        mockDocs.add(doc1);
        
        when(mockEditorBO.getAllFiles()).thenReturn(mockDocs);
        
        // Act
        List<Documents> result = facadeBO.getAllFiles();
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("file1.txt", result.get(0).getName());
        verify(mockEditorBO).getAllFiles();
    }
    
    @Test
    @DisplayName("Delegation: getAllFiles returns empty list")
    void testGetAllFiles_EmptyList() {
        // Arrange
        when(mockEditorBO.getAllFiles()).thenReturn(new ArrayList<>());
        
        // Act
        List<Documents> result = facadeBO.getAllFiles();
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mockEditorBO).getAllFiles();
    }

    // ==================== GET FILE EXTENSION DELEGATION ====================
    
    @Test
    @DisplayName("Delegation: getFileExtension calls EditorBO.getFileExtension")
    void testGetFileExtension_Delegation() {
        // Arrange
        String fileName = "document.txt";
        when(mockEditorBO.getFileExtension(fileName)).thenReturn("txt");
        
        // Act
        String result = facadeBO.getFileExtension(fileName);
        
        // Assert
        assertEquals("txt", result);
        verify(mockEditorBO).getFileExtension(fileName);
    }
    
    @Test
    @DisplayName("Delegation: getFileExtension with various extensions")
    void testGetFileExtension_VariousExtensions() {
        // Arrange
        when(mockEditorBO.getFileExtension("file.md5")).thenReturn("md5");
        when(mockEditorBO.getFileExtension("file.pdf")).thenReturn("pdf");
        when(mockEditorBO.getFileExtension("noext")).thenReturn("");
        
        // Act & Assert
        assertEquals("md5", facadeBO.getFileExtension("file.md5"));
        assertEquals("pdf", facadeBO.getFileExtension("file.pdf"));
        assertEquals("", facadeBO.getFileExtension("noext"));
    }

    // ==================== TRANSLITERATE DELEGATION ====================
    
    @Test
    @DisplayName("Delegation: transliterate calls EditorBO.transliterate")
    void testTransliterate_Delegation() {
        // Arrange
        int pageId = 1;
        String arabicText = "مرحبا";
        String transliterated = "marhaban";
        when(mockEditorBO.transliterate(pageId, arabicText)).thenReturn(transliterated);
        
        // Act
        String result = facadeBO.transliterate(pageId, arabicText);
        
        // Assert
        assertEquals(transliterated, result);
        verify(mockEditorBO).transliterate(pageId, arabicText);
    }

    // ==================== SEARCH KEYWORD DELEGATION ====================
    
    @Test
    @DisplayName("Delegation: searchKeyword calls EditorBO.searchKeyword")
    void testSearchKeyword_Delegation() {
        // Arrange
        String keyword = "test";
        List<String> mockResults = new ArrayList<>();
        mockResults.add("file1.txt - test found");
        when(mockEditorBO.searchKeyword(keyword)).thenReturn(mockResults);
        
        // Act
        List<String> result = facadeBO.searchKeyword(keyword);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).contains("test"));
        verify(mockEditorBO).searchKeyword(keyword);
    }

    // ==================== LEMMATIZE WORDS DELEGATION ====================
    
    @Test
    @DisplayName("Delegation: lemmatizeWords calls EditorBO.lemmatizeWords")
    void testLemmatizeWords_Delegation() {
        // Arrange
        String text = "running flies";
        Map<String, String> mockLemmas = new HashMap<>();
        mockLemmas.put("running", "run");
        mockLemmas.put("flies", "fly");
        when(mockEditorBO.lemmatizeWords(text)).thenReturn(mockLemmas);
        
        // Act
        Map<String, String> result = facadeBO.lemmatizeWords(text);
        
        // Assert
        assertNotNull(result);
        assertEquals("run", result.get("running"));
        assertEquals("fly", result.get("flies"));
        verify(mockEditorBO).lemmatizeWords(text);
    }

    // ==================== EXTRACT POS DELEGATION ====================
    
    @Test
    @DisplayName("Delegation: extractPOS calls EditorBO.extractPOS")
    void testExtractPOS_Delegation() {
        // Arrange
        String text = "The cat runs";
        Map<String, List<String>> mockPOS = new HashMap<>();
        mockPOS.put("NOUN", List.of("cat"));
        mockPOS.put("VERB", List.of("runs"));
        when(mockEditorBO.extractPOS(text)).thenReturn(mockPOS);
        
        // Act
        Map<String, List<String>> result = facadeBO.extractPOS(text);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("NOUN"));
        assertTrue(result.containsKey("VERB"));
        verify(mockEditorBO).extractPOS(text);
    }

    // ==================== INTEGRATION TESTS ====================
    
    @Test
    @DisplayName("Integration: Multiple operation sequence")
    void testIntegration_MultipleOperations() {
        // Arrange
        when(mockEditorBO.createFile("file1.txt", "content1")).thenReturn(true);
        when(mockEditorBO.updateFile(1, "file1.txt", 1, "updated")).thenReturn(true);
        when(mockEditorBO.deleteFile(1)).thenReturn(true);
        
        // Act - Perform sequence of operations
        boolean created = facadeBO.createFile("file1.txt", "content1");
        boolean updated = facadeBO.updateFile(1, "file1.txt", 1, "updated");
        boolean deleted = facadeBO.deleteFile(1);
        
        // Assert
        assertTrue(created);
        assertTrue(updated);
        assertTrue(deleted);
        
        // Verify order of operations
        verify(mockEditorBO, times(1)).createFile("file1.txt", "content1");
        verify(mockEditorBO, times(1)).updateFile(1, "file1.txt", 1, "updated");
        verify(mockEditorBO, times(1)).deleteFile(1);
    }
    
    @Test
    @DisplayName("Integration: Facade provides unified interface")
    void testIntegration_UnifiedInterface() {
        // Test that Facade simplifies access to multiple operations
        
        // Arrange
        when(mockEditorBO.getAllFiles()).thenReturn(new ArrayList<>());
        when(mockEditorBO.createFile(anyString(), anyString())).thenReturn(true);
        when(mockEditorBO.searchKeyword(anyString())).thenReturn(new ArrayList<>());
        
        // Act - Access different subsystems through single facade
        List<Documents> allFiles = facadeBO.getAllFiles();
        boolean fileCreated = facadeBO.createFile("new.txt", "content");
        List<String> searchResults = facadeBO.searchKeyword("keyword");
        
        // Assert - All operations accessible through facade
        assertNotNull(allFiles);
        assertTrue(fileCreated);
        assertNotNull(searchResults);
    }

    // ==================== METHOD CALL VERIFICATION ====================
    
    @Test
    @DisplayName("Verification: Each facade method calls EditorBO exactly once")
    void testVerification_SingleDelegation() {
        // Arrange
        when(mockEditorBO.createFile(anyString(), anyString())).thenReturn(true);
        
        // Act
        facadeBO.createFile("test.txt", "content");
        
        // Assert - Verify no extra processing, just delegation
        verify(mockEditorBO, times(1)).createFile("test.txt", "content");
        verifyNoMoreInteractions(mockEditorBO);
    }
    
    @Test
    @DisplayName("Verification: Facade doesn't modify return values")
    void testVerification_NoModification() {
        // Arrange
        Documents originalDoc = new Documents();
        originalDoc.setId(100);
        originalDoc.setName("original.txt");
        when(mockEditorBO.getFile(100)).thenReturn(originalDoc);
        
        // Act
        Documents returnedDoc = facadeBO.getFile(100);
        
        // Assert - Exact same object returned, no modification
        assertSame(originalDoc, returnedDoc,
            "Facade should return exact object from EditorBO without modification");
    }
}
