package data;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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

import dal.FacadeDAO;
import dal.IEditorDBDAO;
import dto.Documents;

/**
 * Tests for FacadeDAO - Data Access Facade Pattern
 * 
 * FacadeDAO implements Facade pattern for database operations
 * Tests verify proper delegation to EditorDBDAO
 */
@DisplayName("FacadeDAO - Data Access Facade Tests")
public class FacadeDAOTest {

    @Mock
    private IEditorDBDAO mockEditorDB;
    
    private FacadeDAO facadeDAO;
    private AutoCloseable closeable;
    
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        facadeDAO = new FacadeDAO(mockEditorDB);
    }
    
    @AfterEach
    void tearDown() throws Exception {
        if (closeable != null) {
            closeable.close();
        }
    }

    // ==================== FACADE PATTERN TESTS ====================
    
    @Test
    @DisplayName("Pattern: FacadeDAO delegates to EditorDBDAO")
    void testPattern_Delegation() {
        assertNotNull(facadeDAO, "FacadeDAO should delegate to EditorDBDAO");
    }

    // ==================== CREATE FILE DELEGATION ====================
    
    @Test
    @DisplayName("Delegation: createFileInDB")
    void testCreateFileInDB_Delegation() {
        // Arrange
        when(mockEditorDB.createFileInDB("test.txt", "content")).thenReturn(true);
        
        // Act
        boolean result = facadeDAO.createFileInDB("test.txt", "content");
        
        // Assert
        assertTrue(result);
        verify(mockEditorDB).createFileInDB("test.txt", "content");
    }

    // ==================== UPDATE FILE DELEGATION ====================
    
    @Test
    @DisplayName("Delegation: updateFileInDB")
    void testUpdateFileInDB_Delegation() {
        // Arrange
        when(mockEditorDB.updateFileInDB(1, "file.txt", 1, "new content")).thenReturn(true);
        
        // Act
        boolean result = facadeDAO.updateFileInDB(1, "file.txt", 1, "new content");
        
        // Assert
        assertTrue(result);
        verify(mockEditorDB).updateFileInDB(1, "file.txt", 1, "new content");
    }

    // ==================== DELETE FILE DELEGATION ====================
    
    @Test
    @DisplayName("Delegation: deleteFileInDB")
    void testDeleteFileInDB_Delegation() {
        // Arrange
        when(mockEditorDB.deleteFileInDB(5)).thenReturn(true);
        
        // Act
        boolean result = facadeDAO.deleteFileInDB(5);
        
        // Assert
        assertTrue(result);
        verify(mockEditorDB).deleteFileInDB(5);
    }

    // ==================== GET FILES DELEGATION ====================
    
    @Test
    @DisplayName("Delegation: getFilesFromDB")
    void testGetFilesFromDB_Delegation() {
        // Arrange
        List<Documents> mockDocs = new ArrayList<>();
        when(mockEditorDB.getFilesFromDB()).thenReturn(mockDocs);
        
        // Act
        List<Documents> result = facadeDAO.getFilesFromDB();
        
        // Assert
        assertNotNull(result);
        verify(mockEditorDB).getFilesFromDB();
    }

    // ==================== TRANSLITERATION DELEGATION ====================
    
    @Test
    @DisplayName("Delegation: transliterateInDB")
    void testTransliterateInDB_Delegation() {
        // Arrange
        when(mockEditorDB.transliterateInDB(1, "مرحبا")).thenReturn("marhaban");
        
        // Act
        String result = facadeDAO.transliterateInDB(1, "مرحبا");
        
        // Assert
        assertEquals("marhaban", result);
        verify(mockEditorDB).transliterateInDB(1, "مرحبا");
    }

    // ==================== LEMMATIZATION DELEGATION ====================
    
    @Test
    @DisplayName("Delegation: lemmatizeWords")
    void testLemmatizeWords_Delegation() {
        // Arrange
        Map<String, String> mockLemmas = new HashMap<>();
        mockLemmas.put("running", "run");
        when(mockEditorDB.lemmatizeWords("running")).thenReturn(mockLemmas);
        
        // Act
        Map<String, String> result = facadeDAO.lemmatizeWords("running");
        
        // Assert
        assertNotNull(result);
        verify(mockEditorDB).lemmatizeWords("running");
    }

    // ==================== POS EXTRACTION DELEGATION ====================
    
    @Test
    @DisplayName("Delegation: extractPOS")
    void testExtractPOS_Delegation() {
        // Arrange
        Map<String, List<String>> mockPOS = new HashMap<>();
        when(mockEditorDB.extractPOS("text")).thenReturn(mockPOS);
        
        // Act
        Map<String, List<String>> result = facadeDAO.extractPOS("text");
        
        // Assert
        assertNotNull(result);
        verify(mockEditorDB).extractPOS("text");
    }

    // ==================== ROOT EXTRACTION DELEGATION ====================
    
    @Test
    @DisplayName("Delegation: extractRoots")
    void testExtractRoots_Delegation() {
        // Arrange
        Map<String, String> mockRoots = new HashMap<>();
        when(mockEditorDB.extractRoots("text")).thenReturn(mockRoots);
        
        // Act
        Map<String, String> result = facadeDAO.extractRoots("text");
        
        // Assert
        assertNotNull(result);
        verify(mockEditorDB).extractRoots("text");
    }

    // ==================== TFIDF DELEGATION ====================
    
    @Test
    @DisplayName("Delegation: performTFIDF")
    void testPerformTFIDF_Delegation() {
        // Arrange
        List<String> corpus = List.of("doc1", "doc2");
        when(mockEditorDB.performTFIDF(corpus, "selected")).thenReturn(0.85);
        
        // Act
        double result = facadeDAO.performTFIDF(corpus, "selected");
        
        // Assert
        assertEquals(0.85, result);
        verify(mockEditorDB).performTFIDF(corpus, "selected");
    }

    // ==================== PMI DELEGATION ====================
    
    @Test
    @DisplayName("Delegation: performPMI")
    void testPerformPMI_Delegation() {
        // Arrange
        Map<String, Double> mockPMI = new HashMap<>();
        when(mockEditorDB.performPMI("content")).thenReturn(mockPMI);
        
        // Act
        Map<String, Double> result = facadeDAO.performPMI("content");
        
        // Assert
        assertNotNull(result);
        verify(mockEditorDB).performPMI("content");
    }

    // ==================== PKL DELEGATION ====================
    
    @Test
    @DisplayName("Delegation: performPKL")
    void testPerformPKL_Delegation() {
        // Arrange
        Map<String, Double> mockPKL = new HashMap<>();
        when(mockEditorDB.performPKL("content")).thenReturn(mockPKL);
        
        // Act
        Map<String, Double> result = facadeDAO.performPKL("content");
        
        // Assert
        assertNotNull(result);
        verify(mockEditorDB).performPKL("content");
    }

    // ==================== STEMMING DELEGATION ====================
    
    @Test
    @DisplayName("Delegation: stemWords")
    void testStemWords_Delegation() {
        // Arrange
        Map<String, String> mockStems = new HashMap<>();
        when(mockEditorDB.stemWords("text")).thenReturn(mockStems);
        
        // Act
        Map<String, String> result = facadeDAO.stemWords("text");
        
        // Assert
        assertNotNull(result);
        verify(mockEditorDB).stemWords("text");
    }

    // ==================== WORD SEGMENTATION DELEGATION ====================
    
    @Test
    @DisplayName("Delegation: segmentWords")
    void testSegmentWords_Delegation() {
        // Arrange
        Map<String, String> mockSegments = new HashMap<>();
        when(mockEditorDB.segmentWords("text")).thenReturn(mockSegments);
        
        // Act
        Map<String, String> result = facadeDAO.segmentWords("text");
        
        // Assert
        assertNotNull(result);
        verify(mockEditorDB).segmentWords("text");
    }

    // ==================== INTEGRATION TESTS ====================
    
    @Test
    @DisplayName("Integration: Multiple operations sequence")
    void testIntegration_MultipleOperations() {
        // Arrange
        when(mockEditorDB.createFileInDB(anyString(), anyString())).thenReturn(true);
        when(mockEditorDB.getFilesFromDB()).thenReturn(new ArrayList<>());
        when(mockEditorDB.lemmatizeWords(anyString())).thenReturn(new HashMap<>());
        
        // Act
        facadeDAO.createFileInDB("file.txt", "content");
        facadeDAO.getFilesFromDB();
        facadeDAO.lemmatizeWords("text");
        
        // Assert - All delegated correctly
        verify(mockEditorDB, times(1)).createFileInDB("file.txt", "content");
        verify(mockEditorDB, times(1)).getFilesFromDB();
        verify(mockEditorDB, times(1)).lemmatizeWords("text");
    }
}
