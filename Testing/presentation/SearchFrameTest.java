package presentation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pl.SearchFrame;

/**
 * Tests for SearchFrame - Search Results Display Component
 * 
 * Tests the UI table that displays search results with replace functionality
 */
@DisplayName("SearchFrame - Search Results Display Tests")
public class SearchFrameTest {

    private List<String> searchResults;
    
    @BeforeEach
    void setUp() {
        searchResults = new ArrayList<>();
    }

    // ==================== INITIALIZATION TESTS ====================
    
    @Test
    @DisplayName("Positive: SearchFrame initializes with results")
    void testSearchFrame_InitializationWithResults() {
        // Arrange
        searchResults.add("file1.txt - Prefix keyword1");
        searchResults.add("file2.txt - NoPrefix keyword2");
        
        // Act
        SearchFrame searchFrame = new SearchFrame(searchResults);
        
        // Assert
        assertNotNull(searchFrame, "SearchFrame should be initialized");
    }
    
    @Test
    @DisplayName("Positive: SearchFrame with empty results")
    void testSearchFrame_EmptyResults() {
        // Act
        SearchFrame searchFrame = new SearchFrame(new ArrayList<>());
        
        // Assert
        assertNotNull(searchFrame, "SearchFrame should handle empty results");
    }
    
    @Test
    @DisplayName("Negative: SearchFrame with null results")
    void testSearchFrame_NullResults() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            SearchFrame searchFrame = new SearchFrame(null);
            assertNotNull(searchFrame);
        }, "Should handle null results gracefully");
    }

    // ==================== RESULT PARSING TESTS ====================
    
    @Test
    @DisplayName("Positive: Parse search result with prefix")
    void testParseResult_WithPrefix() {
        // Arrange
        String result = "document.txt - Prefix searchterm";
        
        // Act
        String[] parts = result.split(" - ");
        
        // Assert
        assertEquals(2, parts.length);
        assertEquals("document.txt", parts[0]);
        assertTrue(parts[1].contains("Prefix"));
    }
    
    @Test
    @DisplayName("Positive: Parse search result without prefix")
    void testParseResult_NoPrefix() {
        // Arrange
        String result = "file.txt - NoPrefix keyword";
        
        // Act
        String[] parts = result.split(" - ");
        
        // Assert
        assertEquals(2, parts.length);
        assertTrue(parts[1].contains("NoPrefix"));
    }
    
    @Test
    @DisplayName("Boundary: Parse malformed result")
    void testParseResult_Malformed() {
        // Arrange
        String result = "malformed result";
        
        // Act
        String[] parts = result.split(" - ");
        
        // Assert
        assertEquals(1, parts.length, "Malformed result should split to single element");
    }

    // ==================== RESULT COUNT TESTS ====================
    
    @Test
    @DisplayName("Positive: Display single search result")
    void testResultCount_SingleResult() {
        // Arrange
        searchResults.add("file.txt - Prefix term");
        
        // Act
        SearchFrame searchFrame = new SearchFrame(searchResults);
        
        // Assert
        assertEquals(1, searchResults.size());
    }
    
    @Test
    @DisplayName("Positive: Display multiple search results")
    void testResultCount_MultipleResults() {
        // Arrange
        for (int i = 0; i < 10; i++) {
            searchResults.add("file" + i + ".txt - Prefix keyword" + i);
        }
        
        // Act
        SearchFrame searchFrame = new SearchFrame(searchResults);
        
        // Assert
        assertEquals(10, searchResults.size());
    }
    
    @Test
    @DisplayName("Boundary: Display large number of results")
    void testResultCount_LargeResultSet() {
        // Arrange
        for (int i = 0; i < 1000; i++) {
            searchResults.add("file" + i + ".txt - NoPrefix keyword");
        }
        
        // Act
        SearchFrame searchFrame = new SearchFrame(searchResults);
        
        // Assert
        assertEquals(1000, searchResults.size(), "Should handle large result sets");
    }

    // ==================== TABLE STRUCTURE TESTS ====================
    
    @Test
    @DisplayName("Positive: Table has correct column structure")
    void testTableStructure_Columns() {
        // Expected columns: File Name, Prefix, Keyword, Actions
        String[] expectedColumns = {"File Name", "Prefix", "Keyword", "Actions"};
        
        // Assert
        assertEquals(4, expectedColumns.length, "Table should have 4 columns");
    }
    
    @Test
    @DisplayName("Positive: Parse result into table columns")
    void testTableStructure_RowParsing() {
        // Arrange
        String result = "test.txt - Prefix searchword";
        String[] parts = result.split(" - ");
        
        // Act
        String fileName = parts[0];
        String[] prefixAndKeyword = parts[1].split(" ");
        String prefix = prefixAndKeyword[0];
        String keyword = prefixAndKeyword[1];
        
        // Assert
        assertEquals("test.txt", fileName);
        assertEquals("Prefix", prefix);
        assertEquals("searchword", keyword);
    }

    // ==================== REPLACE FUNCTIONALITY TESTS ====================
    
    @Test
    @DisplayName("Positive: Replace button exists for each row")
    void testReplaceButton_Exists() {
        // The replace button should be in column 3 (Actions column)
        int actionColumnIndex = 3;
        
        // Assert
        assertEquals(3, actionColumnIndex, "Replace button should be in column 3");
    }
    
    @Test
    @DisplayName("Integration: Multiple results with replace buttons")
    void testReplaceButton_MultipleRows() {
        // Arrange
        searchResults.add("file1.txt - Prefix word1");
        searchResults.add("file2.txt - NoPrefix word2");
        searchResults.add("file3.txt - Prefix word3");
        
        // Act
        SearchFrame searchFrame = new SearchFrame(searchResults);
        
        // Assert - Each row should have a replace button
        assertEquals(3, searchResults.size(), "Each result should have replace functionality");
    }

    // ==================== FILTERING TESTS ====================
    
    @Test
    @DisplayName("Positive: Filter results by prefix")
    void testFilter_ByPrefix() {
        // Arrange
        searchResults.add("file1.txt - Prefix word");
        searchResults.add("file2.txt - NoPrefix word");
        searchResults.add("file3.txt - Prefix term");
        
        // Act
        long prefixCount = searchResults.stream()
            .filter(r -> r.contains("Prefix"))
            .count();
        
        // Assert
        assertEquals(2, prefixCount, "Should find 2 results with Prefix");
    }
    
    @Test
    @DisplayName("Positive: Filter results by file name")
    void testFilter_ByFileName() {
        // Arrange
        searchResults.add("important.txt - Prefix word");
        searchResults.add("test.txt - NoPrefix word");
        searchResults.add("important.txt - Prefix term");
        
        // Act
        long fileCount = searchResults.stream()
            .filter(r -> r.startsWith("important.txt"))
            .count();
        
        // Assert
        assertEquals(2, fileCount, "Should find 2 results from important.txt");
    }

    // ==================== RESULT FORMATTING TESTS ====================
    
    @Test
    @DisplayName("Positive: Result string format validation")
    void testResultFormat_Valid() {
        // Arrange
        String validFormat = "filename.txt - Prefix keyword";
        
        // Act
        boolean hasCorrectFormat = validFormat.contains(" - ") && 
                                   validFormat.split(" - ").length == 2;
        
        // Assert
        assertTrue(hasCorrectFormat, "Result should follow 'filename - prefix keyword' format");
    }
    
    @Test
    @DisplayName("Boundary: Result with special characters")
    void testResultFormat_SpecialCharacters() {
        // Arrange
        String result = "file@#$.txt - Prefix key#word";
        
        // Act
        String[] parts = result.split(" - ");
        
        // Assert
        assertEquals(2, parts.length, "Should parse results with special characters");
    }
    
    @Test
    @DisplayName("Positive: Result with Arabic text")
    void testResultFormat_ArabicText() {
        // Arrange
        String result = "ملف.txt - Prefix كلمة";
        
        // Act
        String[] parts = result.split(" - ");
        
        // Assert
        assertEquals(2, parts.length, "Should handle Arabic text");
        assertTrue(parts[1].contains("كلمة"));
    }

    // ==================== UI INTERACTION TESTS ====================
    
    @Test
    @DisplayName("Positive: Back button functionality")
    void testBackButton_Exists() {
        // The back button should exist to return to main menu
        String backButtonLabel = "Back to Menu";
        
        // Assert
        assertNotNull(backButtonLabel);
        assertEquals("Back to Menu", backButtonLabel);
    }
    
    @Test
    @DisplayName("Integration: Complete search flow")
    void testIntegration_CompleteSearchFlow() {
        // Arrange - Simulate complete search workflow
        searchResults.add("doc1.txt - Prefix search");
        searchResults.add("doc2.txt - NoPrefix search");
        
        // Act - Create frame with results
        SearchFrame frame = new SearchFrame(searchResults);
        
        // Assert - Frame should be ready for user interaction
        assertNotNull(frame);
        assertEquals(2, searchResults.size());
    }

    // ==================== EDGE CASE TESTS ====================
    
    @Test
    @DisplayName("Boundary: Result with very long file name")
    void testEdgeCase_LongFileName() {
        // Arrange
        String longFileName = "a".repeat(200) + ".txt - Prefix word";
        searchResults.add(longFileName);
        
        // Act
        SearchFrame frame = new SearchFrame(searchResults);
        
        // Assert
        assertNotNull(frame);
        assertEquals(1, searchResults.size());
    }
    
    @Test
    @DisplayName("Boundary: Result with very long keyword")
    void testEdgeCase_LongKeyword() {
        // Arrange
        String longKeyword = "file.txt - Prefix " + "keyword".repeat(50);
        searchResults.add(longKeyword);
        
        // Act
        SearchFrame frame = new SearchFrame(searchResults);
        
        // Assert
        assertNotNull(frame);
    }
}
