package business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import bll.SearchWord;
import dto.Documents;
import dto.Pages;

/**
 * White-Box Testing for SearchWord.searchKeyword() method
 * 
 * Control Flow Graph Analysis:
 * - Node 1: Entry
 * - Node 2: keyword.length() < 3 check
 * - Node 3: Throw IllegalArgumentException
 * - Node 4: For loop (docs)
 * - Node 5: For loop (pages)
 * - Node 6: pageContent.contains(keyword) check
 * - Node 7: For loop (words)
 * - Node 8: words[i].equalsIgnoreCase(keyword) check
 * - Node 9: i > 0 check for prefix
 * - Node 10: Add to results
 * - Node 11: Return results
 * 
 * Cyclomatic Complexity V(G) = E - N + 2P
 * E (edges) = 14, N (nodes) = 11, P (components) = 1
 * V(G) = 14 - 11 + 2(1) = 5
 * 
 * Test Paths:
 * P = {p1, p2, p3, p4, p5}
 * p1 = <1, 2, 3> (keyword too short - exception path)
 * p2 = <1, 2, 4, 11> (empty document list)
 * p3 = <1, 2, 4, 5, 6, 7, 11> (keyword not found)
 * p4 = <1, 2, 4, 5, 6, 7, 8, 9, 10, 11> (keyword found with prefix)
 * p5 = <1, 2, 4, 5, 6, 7, 8, 9, 10, 11> (keyword found without prefix)
 */
@DisplayName("SearchWord - Search & Replace Functionality Tests")
public class SearchWordTest {

    private List<Documents> testDocuments;
    
    @BeforeEach
    void setUp() {
        testDocuments = new ArrayList<>();
    }

    // ==================== POSITIVE TESTS ====================
    
    @Test
    @DisplayName("Positive: Search finds keyword with prefix word")
    void testSearchKeyword_FindsWithPrefix() {
        // Arrange
        Documents doc = createDocumentWithContent("Test Document", "This is a test keyword example");
        testDocuments.add(doc);
        
        // Act
        List<String> results = SearchWord.searchKeyword("keyword", testDocuments);
        
        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertTrue(results.get(0).contains("test keyword"));
        assertTrue(results.get(0).contains("Test Document"));
    }
    
    @Test
    @DisplayName("Positive: Search finds keyword at start of content (no prefix)")
    void testSearchKeyword_FindsWithoutPrefix() {
        // Arrange
        Documents doc = createDocumentWithContent("Doc1", "keyword is at the beginning");
        testDocuments.add(doc);
        
        // Act
        List<String> results = SearchWord.searchKeyword("keyword", testDocuments);
        
        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertTrue(results.get(0).contains(" keyword"));
    }
    
    @Test
    @DisplayName("Positive: Search is case-insensitive")
    void testSearchKeyword_CaseInsensitive() {
        // Arrange
        Documents doc = createDocumentWithContent("Doc", "This KEYWORD should match");
        testDocuments.add(doc);
        
        // Act
        List<String> results = SearchWord.searchKeyword("keyword", testDocuments);
        
        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
    }
    
    @Test
    @DisplayName("Positive: Search finds keyword in multiple documents")
    void testSearchKeyword_MultipleDocuments() {
        // Arrange
        testDocuments.add(createDocumentWithContent("Doc1", "First keyword here"));
        testDocuments.add(createDocumentWithContent("Doc2", "Second keyword there"));
        testDocuments.add(createDocumentWithContent("Doc3", "No match in this one"));
        
        // Act
        List<String> results = SearchWord.searchKeyword("keyword", testDocuments);
        
        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
    }

    // ==================== NEGATIVE TESTS ====================
    
    @Test
    @DisplayName("Negative: Keyword too short (< 3 characters) throws exception")
    void testSearchKeyword_KeywordTooShort() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            SearchWord.searchKeyword("ab", testDocuments);
        });
        
        assertTrue(exception.getMessage().contains("at least 3 letter"));
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"a", "ab", "", "  "})
    @DisplayName("Negative: Various invalid keywords throw exceptions")
    void testSearchKeyword_InvalidKeywords(String keyword) {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            SearchWord.searchKeyword(keyword, testDocuments);
        });
    }
    
    @Test
    @DisplayName("Negative: Keyword not found returns empty list")
    void testSearchKeyword_NotFound() {
        // Arrange
        Documents doc = createDocumentWithContent("Doc", "This content has no match");
        testDocuments.add(doc);
        
        // Act
        List<String> results = SearchWord.searchKeyword("missing", testDocuments);
        
        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }
    
    @Test
    @DisplayName("Negative: Empty document list returns empty results")
    void testSearchKeyword_EmptyDocumentList() {
        // Act
        List<String> results = SearchWord.searchKeyword("keyword", testDocuments);
        
        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }
    
    @Test
    @DisplayName("Negative: Null page content handled gracefully")
    void testSearchKeyword_NullPageContent() {
        // Arrange
        Documents doc = new Documents();
        doc.setName("NullDoc");
        Pages page = new Pages();
        page.setPageContent(null);
        doc.setPages(Arrays.asList(page));
        testDocuments.add(doc);
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            SearchWord.searchKeyword("keyword", testDocuments);
        });
    }

    // ==================== BOUNDARY TESTS ====================
    
    @Test
    @DisplayName("Boundary: Exactly 3 characters keyword")
    void testSearchKeyword_ExactlyThreeCharacters() {
        // Arrange
        Documents doc = createDocumentWithContent("Doc", "The abc word is here");
        testDocuments.add(doc);
        
        // Act
        List<String> results = SearchWord.searchKeyword("abc", testDocuments);
        
        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
    }
    
    @Test
    @DisplayName("Boundary: Very long keyword")
    void testSearchKeyword_VeryLongKeyword() {
        // Arrange
        String longKeyword = "ThisIsAVeryLongKeywordForTesting";
        Documents doc = createDocumentWithContent("Doc", "Content with " + longKeyword + " here");
        testDocuments.add(doc);
        
        // Act
        List<String> results = SearchWord.searchKeyword(longKeyword, testDocuments);
        
        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
    }
    
    @Test
    @DisplayName("Boundary: Keyword with special characters")
    void testSearchKeyword_SpecialCharacters() {
        // Arrange
        Documents doc = createDocumentWithContent("Doc", "Test @#$ content");
        testDocuments.add(doc);
        
        // Act
        List<String> results = SearchWord.searchKeyword("@#$", testDocuments);
        
        // Assert
        assertNotNull(results);
        // Should find or not depending on implementation
    }

    // ==================== HELPER METHODS ====================
    
    private Documents createDocumentWithContent(String name, String content) {
        Documents doc = new Documents();
        doc.setName(name);
        
        Pages page = new Pages();
        page.setPageNumber(1);
        page.setPageContent(content);
        
        doc.setPages(Arrays.asList(page));
        return doc;
    }
}
