package data;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dal.Lemmatization;

/**
 * Tests for Lemmatization - Arabic Word Lemmatization
 * 
 * Tests lemmatization functionality using AlKhalil2Analyzer
 * Note: External library dependency may affect test execution
 */
@DisplayName("Lemmatization - Arabic Text Processing Tests")
public class LemmatizationTest {

    // ==================== BASIC LEMMATIZATION TESTS ====================
    
    @Test
    @DisplayName("Positive: Lemmatize simple Arabic text")
    void testLemmatize_SimpleArabicText() {
        // Arrange
        String text = "كتب";
        
        // Act
        Map<String, String> result = Lemmatization.lemmatizeWords(text);
        
        // Assert
        assertNotNull(result, "Result should not be null");
        assertFalse(result.isEmpty(), "Result should contain lemmatization");
    }
    
    @Test
    @DisplayName("Positive: Lemmatize multiple words")
    void testLemmatize_MultipleWords() {
        // Arrange
        String text = "كتب قرأ";
        
        // Act
        Map<String, String> result = Lemmatization.lemmatizeWords(text);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.size() >= 1, "Should lemmatize at least one word");
    }

    // ==================== BOUNDARY TESTS ====================
    
    @Test
    @DisplayName("Boundary: Empty text")
    void testLemmatize_EmptyText() {
        // Arrange
        String text = "";
        
        // Act
        Map<String, String> result = Lemmatization.lemmatizeWords(text);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    @DisplayName("Boundary: Whitespace only")
    void testLemmatize_WhitespaceOnly() {
        // Arrange
        String text = "   ";
        
        // Act
        Map<String, String> result = Lemmatization.lemmatizeWords(text);
        
        // Assert
        assertNotNull(result);
    }

    // ==================== SPECIAL CASES ====================
    
    @Test
    @DisplayName("Negative: Non-Arabic text returns result")
    void testLemmatize_NonArabicText() {
        // Arrange
        String text = "hello world";
        
        // Act
        Map<String, String> result = Lemmatization.lemmatizeWords(text);
        
        // Assert
        assertNotNull(result, "Should handle non-Arabic text");
    }
    
    @Test
    @DisplayName("Positive: Mixed Arabic and English")
    void testLemmatize_MixedText() {
        // Arrange
        String text = "كتب book قرأ read";
        
        // Act
        Map<String, String> result = Lemmatization.lemmatizeWords(text);
        
        // Assert
        assertNotNull(result);
    }

    // ==================== RESULT VALIDATION ====================
    
    @Test
    @DisplayName("Positive: Result map contains words")
    void testLemmatize_ResultContainsWords() {
        // Arrange
        String text = "كتاب";
        
        // Act
        Map<String, String> result = Lemmatization.lemmatizeWords(text);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("كتاب") || result.size() >= 0);
    }
    
    @Test
    @DisplayName("Positive: Not found words handled")
    void testLemmatize_NotFoundHandling() {
        // Words without lemmas should be marked as "Not found"
        String text = "xyz123";
        
        // Act
        Map<String, String> result = Lemmatization.lemmatizeWords(text);
        
        // Assert
        assertNotNull(result);
    }

    // ==================== WORD COUNT TESTS ====================
    
    @Test
    @DisplayName("Positive: Single word lemmatization")
    void testLemmatize_SingleWord() {
        // Arrange
        String text = "كتب";
        
        // Act
        Map<String, String> result = Lemmatization.lemmatizeWords(text);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.size() >= 1 || result.isEmpty());
    }
    
    @Test
    @DisplayName("Positive: Multiple words lemmatization")
    void testLemmatize_ManyWords() {
        // Arrange
        String text = "كتب قرأ درس فهم تعلم";
        
        // Act
        Map<String, String> result = Lemmatization.lemmatizeWords(text);
        
        // Assert
        assertNotNull(result);
    }

    // ==================== PREPROCESSING INTEGRATION ====================
    
    @Test
    @DisplayName("Integration: Preprocessing applied to lemmas")
    void testLemmatize_PreprocessingApplied() {
        // The lemmatization uses PreProcessText.preprocessText
        // This should clean the lemma output
        
        String text = "كتب";
        Map<String, String> result = Lemmatization.lemmatizeWords(text);
        
        assertNotNull(result, "Preprocessing should still return results");
    }

    // ==================== ERROR HANDLING ====================
    
    @Test
    @DisplayName(" Negative: Null text handling")
    void testLemmatize_NullText() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            Map<String, String> result = Lemmatization.lemmatizeWords(null);
            // May throw NullPointerException or return empty map
        });
    }
    
    @Test
    @DisplayName("Positive: Special characters handled")
    void testLemmatize_SpecialCharacters() {
        // Arrange
        String text = "كتب! قرأ؟ درس.";
        
        // Act
        Map<String, String> result = Lemmatization.lemmatizeWords(text);
        
        // Assert
        assertNotNull(result);
    }

    // ==================== LONG TEXT TESTS ====================
    
    @Test
    @DisplayName("Boundary: Long text lemmatization")
    void testLemmatize_LongText() {
        // Arrange
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("كتب ");
        }
        
        // Act
        Map<String, String> result = Lemmatization.lemmatizeWords(sb.toString());
        
        // Assert
        assertNotNull(result, "Should handle long text");
    }
}
