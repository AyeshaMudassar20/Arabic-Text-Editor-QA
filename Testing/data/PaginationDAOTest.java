package data;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import dal.PaginationDAO;
import dto.Pages;

/**
 * White-Box Testing for PaginationDAO.paginate() method
 * 
 * Control Flow Graph Analysis:
 * - Node 1: Entry
 * - Node 2: Check if fileContent is null or empty
 * - Node 3: Return single empty page
 * - Node 4: For loop initialization (i = 0)
 * - Node 5: For loop condition (i < fileContent.length())
 * - Node 6: Append character to pageContent
 * - Node 7: Check if pageContent.length() == pageSize OR i == fileContent.length() - 1
 * - Node 8: Create new page, increment pageNumber, reset pageContent
 * - Node 9: Loop back or exit
 * - Node 10: Return pages list
 * 
 * Cyclomatic Complexity V(G) = E - N + 2P
 * E (edges) = 12, N (nodes) = 10, P (components) = 1
 * V(G) = 12 - 10 + 2(1) = 4
 * 
 * Test Paths:
 * P = {p1, p2, p3, p4}
 * p1 = <1, 2, 3> (null or empty content)
 * p2 = <1, 2, 4, 5, 6, 7, 8, 9, 10> (content exactly pageSize)
 * p3 = <1, 2, 4, 5, 6, 7, 8, 9, 10> (content less than pageSize)
 * p4 = <1, 2, 4, 5, 6, 7, 8, 9, 10> (content multiple pages)
 */
@DisplayName("PaginationDAO - Pagination Logic Tests")
public class PaginationDAOTest {

    private static final int PAGE_SIZE = 100; // Based on source code

    // ==================== POSITIVE TESTS ====================
    
    @Test
    @DisplayName("Positive: Paginate content exactly one page size")
    void testPaginate_ExactlyOnePage() {
        // Arrange
        String content = "A".repeat(PAGE_SIZE);
        
        // Act
        List<Pages> pages = PaginationDAO.paginate(content);
        
        // Assert
        assertNotNull(pages);
        assertEquals(1, pages.size());
        assertEquals(1, pages.get(0).getPageNumber());
        assertEquals(PAGE_SIZE, pages.get(0).getPageContent().length());
    }
    
    @Test
    @DisplayName("Positive: Paginate content less than page size")
    void testPaginate_LessThanOnePage() {
        // Arrange
        String content = "Short content that fits in one page";
        
        // Act
        List<Pages> pages = PaginationDAO.paginate(content);
        
        // Assert
        assertNotNull(pages);
        assertEquals(1, pages.size());
        assertEquals(content, pages.get(0).getPageContent());
        assertEquals(1, pages.get(0).getPageNumber());
    }
    
    @Test
    @DisplayName("Positive: Paginate content requiring multiple pages")
    void testPaginate_MultiplePages() {
        // Arrange
        String content = "A".repeat(250); // Should create 3 pages
        
        // Act
        List<Pages> pages = PaginationDAO.paginate(content);
        
        // Assert
        assertNotNull(pages);
        assertEquals(3, pages.size());
        
        // Verify page numbers are sequential
        assertEquals(1, pages.get(0).getPageNumber());
        assertEquals(2, pages.get(1).getPageNumber());
        assertEquals(3, pages.get(2).getPageNumber());
        
        // Verify page sizes
        assertEquals(PAGE_SIZE, pages.get(0).getPageContent().length());
        assertEquals(PAGE_SIZE, pages.get(1).getPageContent().length());
        assertEquals(50, pages.get(2).getPageContent().length());
    }
    
    @Test
    @DisplayName("Positive: Content exactly 2 pages")
    void testPaginate_ExactlyTwoPages() {
        // Arrange
        String content = "B".repeat(PAGE_SIZE * 2);
        
        // Act
        List<Pages> pages = PaginationDAO.paginate(content);
        
        // Assert
        assertNotNull(pages);
        assertEquals(2, pages.size());
        assertEquals(PAGE_SIZE, pages.get(0).getPageContent().length());
        assertEquals(PAGE_SIZE, pages.get(1).getPageContent().length());
    }

    // ==================== NEGATIVE TESTS ====================
    
    @Test
    @DisplayName("Negative: Null content returns single empty page")
    void testPaginate_NullContent() {
        // Act
        List<Pages> pages = PaginationDAO.paginate(null);
        
        // Assert
        assertNotNull(pages);
        assertEquals(1, pages.size());
        assertEquals("", pages.get(0).getPageContent());
        assertEquals(1, pages.get(0).getPageNumber());
    }
    
    @Test
    @DisplayName("Negative: Empty string returns single empty page")
    void testPaginate_EmptyString() {
        // Act
        List<Pages> pages = PaginationDAO.paginate("");
        
        // Assert
        assertNotNull(pages);
        assertEquals(1, pages.size());
        assertEquals("", pages.get(0).getPageContent());
    }
    
    @Test
    @DisplayName("Negative: Whitespace-only content")
    void testPaginate_WhitespaceOnly() {
        // Arrange
        String content = "     ";
        
        // Act
        List<Pages> pages = PaginationDAO.paginate(content);
        
        // Assert
        assertNotNull(pages);
        assertEquals(1, pages.size());
        assertEquals(content, pages.get(0).getPageContent());
    }

    // ==================== BOUNDARY TESTS ====================
    
    @Test
    @DisplayName("Boundary: Exactly PAGE_SIZE characters")
    void testPaginate_ExactlyPageSize() {
        // Arrange
        String content = "X".repeat(PAGE_SIZE);
        
        // Act
        List<Pages> pages = PaginationDAO.paginate(content);
        
        // Assert
        assertEquals(1, pages.size());
        assertEquals(PAGE_SIZE, pages.get(0).getPageContent().length());
    }
    
    @Test
    @DisplayName("Boundary: PAGE_SIZE + 1 characters")
    void testPaginate_PageSizePlusOne() {
        // Arrange
        String content = "Y".repeat(PAGE_SIZE + 1);
        
        // Act
        List<Pages> pages = PaginationDAO.paginate(content);
        
        // Assert
        assertEquals(2, pages.size());
        assertEquals(PAGE_SIZE, pages.get(0).getPageContent().length());
        assertEquals(1, pages.get(1).getPageContent().length());
    }
    
    @Test
    @DisplayName("Boundary: PAGE_SIZE - 1 characters")
    void testPaginate_PageSizeMinusOne() {
        // Arrange
        String content = "Z".repeat(PAGE_SIZE - 1);
        
        // Act
        List<Pages> pages = PaginationDAO.paginate(content);
        
        // Assert
        assertEquals(1, pages.size());
        assertEquals(PAGE_SIZE - 1, pages.get(0).getPageContent().length());
    }
    
    @Test
    @DisplayName("Boundary: Single character content")
    void testPaginate_SingleCharacter() {
        // Act
        List<Pages> pages = PaginationDAO.paginate("A");
        
        // Assert
        assertEquals(1, pages.size());
        assertEquals("A", pages.get(0).getPageContent());
    }
    
    @ParameterizedTest
    @ValueSource(ints = {1, 10, 50, 99, 100, 101, 200, 999, 1000})
    @DisplayName("Boundary: Various content lengths")
    void testPaginate_VariousLengths(int length) {
        // Arrange
        String content = "T".repeat(length);
        
        // Act
        List<Pages> pages = PaginationDAO.paginate(content);
        
        // Assert
        assertNotNull(pages);
        int expectedPages = (int) Math.ceil((double) length / PAGE_SIZE);
        assertEquals(expectedPages, pages.size());
        
        // Verify total content length
        int totalLength = pages.stream()
            .mapToInt(p -> p.getPageContent().length())
            .sum();
        assertEquals(length, totalLength);
    }

    // ==================== SPECIAL CHARACTER TESTS ====================
    
    @Test
    @DisplayName("Special: Content with Arabic characters")
    void testPaginate_ArabicCharacters() {
        // Arrange
        String arabicContent = "مرحبا بك في المحرر النصي العربي ".repeat(10);
        
        // Act
        List<Pages> pages = PaginationDAO.paginate(arabicContent);
        
        // Assert
        assertNotNull(pages);
        assertTrue(pages.size() > 0);
        
        // Verify content is preserved
        String reconstructed = pages.stream()
            .map(Pages::getPageContent)
            .reduce("", (a, b) -> a + b);
        assertEquals(arabicContent, reconstructed);
    }
    
    @Test
    @DisplayName("Special: Content with mixed languages")
    void testPaginate_MixedLanguages() {
        // Arrange
        String mixedContent = "English اردو عربی Farsi فارسی ".repeat(15);
        
        // Act
        List<Pages> pages = PaginationDAO.paginate(mixedContent);
        
        // Assert
        assertNotNull(pages);
        
        // Verify all content is paginated
        String reconstructed = pages.stream()
            .map(Pages::getPageContent)
            .reduce("", String::concat);
        assertEquals(mixedContent, reconstructed);
    }
    
    @Test
    @DisplayName("Special: Content with newlines and special chars")
    void testPaginate_SpecialCharacters() {
        // Arrange
        String specialContent = "Line1\nLine2\tTabbed\r\nNewlines!@#$%^&*()";
        
        // Act
        List<Pages> pages = PaginationDAO.paginate(specialContent);
        
        // Assert
        assertNotNull(pages);
        assertEquals(1, pages.size());
        assertEquals(specialContent, pages.get(0).getPageContent());
    }

    // ==================== DATA INTEGRITY TESTS ====================
    
    @Test
    @DisplayName("Integrity: All pages have sequential page numbers")
    void testPaginate_SequentialPageNumbers() {
        // Arrange
        String content = "Content ".repeat(50);
        
        // Act
        List<Pages> pages = PaginationDAO.paginate(content);
        
        // Assert
        for (int i = 0; i < pages.size(); i++) {
            assertEquals(i + 1, pages.get(i).getPageNumber(),
                "Page number should be sequential starting from 1");
        }
    }
    
    @Test
    @DisplayName("Integrity: Total content length preserved")
    void testPaginate_ContentLengthPreserved() {
        // Arrange
        String originalContent = "Test content for pagination ".repeat(20);
        
        // Act
        List<Pages> pages = PaginationDAO.paginate(originalContent);
        
        // Assert
        int totalPaginatedLength = pages.stream()
            .mapToInt(p -> p.getPageContent().length())
            .sum();
        
        assertEquals(originalContent.length(), totalPaginatedLength,
            "Total paginated content length should equal original");
    }
    
    @Test
    @DisplayName("Integrity: Content can be reconstructed correctly")
    void testPaginate_ContentReconstruction() {
        // Arrange
        String originalContent = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".repeat(10);
        
        // Act
        List<Pages> pages = PaginationDAO.paginate(originalContent);
        
        // Reconstruct
        StringBuilder reconstructed = new StringBuilder();
        for (Pages page : pages) {
            reconstructed.append(page.getPageContent());
        }
        
        // Assert
        assertEquals(originalContent, reconstructed.toString(),
            "Reconstructed content should match original exactly");
    }
}
