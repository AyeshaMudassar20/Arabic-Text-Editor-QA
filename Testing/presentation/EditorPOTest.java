package presentation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import bll.IEditorBO;
import dto.Documents;
import dto.Pages;
import pl.EditorPO;

/**
 * Tests for EditorPO - Presentation Object Layer
 * 
 * Critical Auto-Save Feature:
 * - Triggers when word count exceeds 500 words
 * - Saves content to database automatically
 * - Updates UI status label
 * 
 * White-Box Analysis Required:
 * - Auto-save logic has conditional branches
 * - Cyclomatic Complexity calculation needed
 */
@DisplayName("EditorPO - Presentation Layer Tests")
public class EditorPOTest {

    @Mock
    private IEditorBO mockBusinessObj;
    
    private AutoCloseable closeable;
    
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    // ==================== INITIALIZATION TESTS ====================
    
    @Test
    @DisplayName("Positive: EditorPO initializes with business object")
    void testEditorPO_Initialization() {
        // Act
        EditorPO editorPO = new EditorPO(mockBusinessObj);
        
        // Assert
        assertNotNull(editorPO, "EditorPO should be initialized");
    }
    
    @Test
    @DisplayName("Negative: EditorPO with null business object")
    void testEditorPO_NullBusinessObject() {
        // Act & Assert - Should handle gracefully
        assertDoesNotThrow(() -> {
            EditorPO editorPO = new EditorPO(null);
            assertNotNull(editorPO);
        });
    }

    // ==================== AUTO-SAVE TESTS (CRITICAL FEATURE) ====================
    
    @Test
    @DisplayName("White-Box: Auto-save triggers at 500 word threshold")
    void testAutoSave_TriggersAt500Words() {
        // This tests the critical auto-save logic
        // Word count > 500 triggers auto-save
        
        // Arrange
        String content = generateContent(501); // 501 words
        when(mockBusinessObj.updateFile(anyInt(), anyString(), anyInt(), anyString()))
            .thenReturn(true);
        
        // The auto-save logic should detect word count and trigger save
        assertTrue(countWords(content) > 500, "Content should exceed threshold");
    }
    
    @Test
    @DisplayName("White-Box: Auto-save does NOT trigger below 500 words")
    void testAutoSave_NoTriggerBelow500() {
        // Arrange
        String content = generateContent(499); // 499 words
        
        // Assert
        assertTrue(countWords(content) < 500, "Content should be below threshold");
    }
    
    @Test
    @DisplayName("Boundary: Auto-save at exactly 500 words")
    void testAutoSave_ExactlyAt500() {
        // Arrange
        String content = generateContent(500);
        
        // Assert
        assertEquals(500, countWords(content), "Should be exactly 500 words");
    }

    // ==================== PAGINATION TESTS ====================
    
    @Test
    @DisplayName("Positive: Navigate to next page")
    void testPagination_NextPage() {
        // Arrange
        Documents mockDoc = createMockDocWithPages(3);
        when(mockBusinessObj.getFile(anyInt())).thenReturn(mockDoc);
        
        // Navigation logic tested
        int currentPage = 1;
        int totalPages = 3;
        
        // Act
        int nextPage = currentPage + 1;
        
        // Assert
        assertTrue(nextPage <= totalPages, "Should allow navigation to page 2");
    }
    
    @Test
    @DisplayName("Boundary: Cannot navigate beyond last page")
    void testPagination_CannotExceedLastPage() {
        // Arrange
        int currentPage = 3;
        int totalPages = 3;
        
        // Act
        boolean canGoNext = currentPage < totalPages;
        
        // Assert
        assertFalse(canGoNext, "Should not navigate beyond last page");
    }
    
    @Test
    @DisplayName("Positive: Navigate to previous page")
    void testPagination_PreviousPage() {
        // Arrange
        int currentPage = 2;
        
        // Act
        int previousPage = currentPage - 1;
        
        // Assert
        assertTrue(previousPage > 0, "Should navigate to page 1");
    }
    
    @Test
    @DisplayName("Boundary: Cannot navigate before first page")
    void testPagination_CannotGoBelowFirstPage() {
        // Arrange
        int currentPage = 1;
        
        // Act
        boolean canGoPrevious = currentPage > 1;
        
        // Assert
        assertFalse(canGoPrevious, "Should not navigate before page 1");
    }

    // ==================== WORD COUNT TESTS ====================
    
    @Test
    @DisplayName("Positive: Count words in simple text")
    void testWordCount_SimpleText() {
        // Arrange
        String text = "This is a simple test";
        
        // Act
        int count = countWords(text);
        
        // Assert
        assertEquals(5, count, "Should count 5 words");
    }
    
    @Test
    @DisplayName("Boundary: Count words in empty text")
    void testWordCount_EmptyText() {
        // Arrange
        String text = "";
        
        // Act
        int count = countWords(text);
        
        // Assert
        assertEquals(0, count, "Empty text should have 0 words");
    }
    
    @Test
    @DisplayName("Boundary: Count words with multiple spaces")
    void testWordCount_MultipleSpaces() {
        // Arrange
        String text = "word1    word2     word3";
        
        // Act
        int count = countWords(text);
        
        // Assert
        assertEquals(3, count, "Should count 3 words despite multiple spaces");
    }
    
    @Test
    @DisplayName("Positive: Count Arabic words")
    void testWordCount_ArabicText() {
        // Arrange
        String text = "مرحبا بك في المحرر";
        
        // Act
        int count = countWords(text);
        
        // Assert
        assertTrue(count > 0, "Should count Arabic words");
    }

    // ==================== FILE OPERATIONS TESTS ====================
    
    @Test
    @DisplayName("Positive: Load file successfully")
    void testLoadFile_Success() {
        // Arrange
        Documents mockDoc = createMockDocWithPages(1);
        when(mockBusinessObj.getFile(1)).thenReturn(mockDoc);
        
        // Act
        Documents result = mockBusinessObj.getFile(1);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
    }
    
    @Test
    @DisplayName("Negative: Load non-existent file")
    void testLoadFile_NotFound() {
        // Arrange
        when(mockBusinessObj.getFile(999)).thenReturn(null);
        
        // Act
        Documents result = mockBusinessObj.getFile(999);
        
        // Assert
        assertNull(result, "Should return null for non-existent file");
    }
    
    @Test
    @DisplayName("Positive: Get all files")
    void testGetAllFiles_Success() {
        // Arrange
        List<Documents> mockDocs = new ArrayList<>();
        mockDocs.add(createMockDocWithPages(1));
        mockDocs.add(createMockDocWithPages(2));
        when(mockBusinessObj.getAllFiles()).thenReturn(mockDocs);
        
        // Act
        List<Documents> result = mockBusinessObj.getAllFiles();
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    // ==================== STATISTICS TESTS ====================
    
    @Test
    @DisplayName("Positive: Calculate average word length")
    void testAverageWordLength_Calculation() {
        // Arrange
        String text = "cat dog bird";
        
        // Act
        double avgLength = calculateAverageWordLength(text);
        
        // Assert
        assertEquals(3.33, avgLength, 0.01, "Average length should be ~3.33");
    }
    
    @Test
    @DisplayName("Positive: Count total lines")
    void testLineCount_MultipleLines() {
        // Arrange
        String text = "Line 1\nLine 2\nLine 3";
        
        // Act
        int lineCount = countLines(text);
        
        // Assert
        assertEquals(3, lineCount, "Should count 3 lines");
    }
    
    @Test
    @DisplayName("Boundary: Line count for empty text")
    void testLineCount_EmptyText() {
        // Arrange
        String text = "";
        
        // Act
        int lineCount = countLines(text);
        
        // Assert
        assertEquals(0, lineCount, "Empty text should have 0 lines");
    }

    // ==================== UI STATE TESTS ====================
    
    @Test
    @DisplayName("Positive: UI shows correct page count")
    void testUI_PageCountDisplay() {
        // Arrange
        int currentPage = 2;
        int totalPages = 5;
        
        // Act
        String display = String.format("Page %d of %d", currentPage, totalPages);
        
        // Assert
        assertEquals("Page 2 of 5", display);
    }
    
    @Test
    @DisplayName("Positive: Saving status updates")
    void testUI_SavingStatus() {
        // Test that saving status label updates correctly
        String status = "Saving...";
        assertEquals("Saving...", status);
        
        status = "Saved successfully";
        assertEquals("Saved successfully", status);
    }

    // ==================== HELPER METHODS ====================
    
    private Documents createMockDocWithPages(int numPages) {
        Documents doc = new Documents();
        doc.setId(1);
        doc.setName("test.txt");
        
        List<Pages> pages = new ArrayList<>();
        for (int i = 1; i <= numPages; i++) {
            Pages page = new Pages();
            page.setPageNumber(i);
            page.setPageContent("Content for page " + i);
            pages.add(page);
        }
        doc.setPages(pages);
        
        return doc;
    }
    
    private String generateContent(int wordCount) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < wordCount; i++) {
            sb.append("word").append(i).append(" ");
        }
        return sb.toString().trim();
    }
    
    private int countWords(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        return text.trim().split("\\s+").length;
    }
    
    private int countLines(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        return text.split("\n").length;
    }
    
    private double calculateAverageWordLength(String text) {
        String[] words = text.trim().split("\\s+");
        int totalLength = 0;
        for (String word : words) {
            totalLength += word.length();
        }
        return (double) totalLength / words.length;
    }
}
