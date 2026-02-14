package data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dal.TFIDFCalculator;

/**
 * White-Box Testing for TFIDFCalculator
 * 
 * TF-IDF (Term Frequency-Inverse Document Frequency) Algorithm Testing
 * 
 * Formula:
 * TF(t) = (Number of times term t appears in a document) / (Total number of terms in the document)
 * IDF(t) = log(Total number of documents / Number of documents with term t in it)
 * TF-IDF(t) = TF(t) × IDF(t)
 * 
 * Test Requirements:
 * - Positive Path: Known document with manual calculation (±0.01 tolerance)
 * - Negative Path: Empty document handling
 * - Negative Path: Special characters handling
 */
@DisplayName("TFIDFCalculator - TF-IDF Algorithm Validation Tests")
public class TFIDFCalculatorTest {

    private TFIDFCalculator calculator;
    
    @BeforeEach
    void setUp() {
        calculator = new TFIDFCalculator();
    }

    // ==================== POSITIVE TESTS ====================
    
    @Test
    @DisplayName("Positive: Manual calculation verification - Simple corpus")
    void testTFIDF_ManualCalculationSimple() {
        // Arrange - Create a simple, predictable corpus
        calculator.addDocumentToCorpus("the cat sat");
        calculator.addDocumentToCorpus("the dog sat");
        calculator.addDocumentToCorpus("the cat ran");
        
        // Calculate for a test document
        String testDoc = "the cat";
        
        /*
         * Manual Calculation:
         * Document: "the cat"
         * Corpus: ["the cat sat", "the dog sat", "the cat ran"]
         * 
         * TF("the") = 1/2 = 0.5
         * TF("cat") = 1/2 = 0.5
         * 
         * IDF("the") = log(3/3) = log(1) = 0 (appears in all docs)
         * IDF("cat") = log(3/2) = log(1.5) ≈ 0.405
         * 
         * TF-IDF("the") = 0.5 * 0 = 0
         * TF-IDF("cat") = 0.5 * 0.405 = 0.2025
         * 
         * Average TF-IDF = (0 + 0.2025) / 2 = 0.10125
         */
        
        // Act
        double result = calculator.calculateDocumentTfIdf(testDoc);
        
        // Assert (±0.01 tolerance as per requirements)
        double expectedMin = 0.09125; // 0.10125 - 0.01
        double expectedMax = 0.11125; // 0.10125 + 0.01
        
        assertTrue(result >= expectedMin && result <= expectedMax,
            String.format("Expected TF-IDF between %.5f and %.5f, but got %.5f", 
                expectedMin, expectedMax, result));
    }
    
    @Test
    @DisplayName("Positive: Single unique word document")
    void testTFIDF_SingleWord() {
        // Arrange
        calculator.addDocumentToCorpus("hello");
        calculator.addDocumentToCorpus("world");
        calculator.addDocumentToCorpus("test");
        
        String testDoc = "hello";
        
        // Act
        double result = calculator.calculateDocumentTfIdf(testDoc);
        
        // Assert
        assertTrue(result > 0, "Single unique word should have positive TF-IDF");
    }
    
    @Test
    @DisplayName("Positive: Common words have lower TF-IDF")
    void testTFIDF_CommonWordsLowerScore() {
        // Arrange - "the" appears in all documents
        calculator.addDocumentToCorpus("the cat");
        calculator.addDocumentToCorpus("the dog");
        calculator.addDocumentToCorpus("the bird");
        
        double commonWordScore = calculator.calculateDocumentTfIdf("the");
        
        // Now add docs without "rare" and calculate
        calculator.addDocumentToCorpus("rare word");
        
        double rareWordScore = calculator.calculateDocumentTfIdf("rare");
        
        // Assert - rare words should have higher TF-IDF
        assertTrue(rareWordScore > commonWordScore,
            "Rare words should have higher TF-IDF than common words");
    }
    
    @Test
    @DisplayName("Positive: Repeated words increase TF component")
    void testTFIDF_RepeatedWords() {
        // Arrange
        calculator.addDocumentToCorpus("word word word");
        calculator.addDocumentToCorpus("other text here");
        
        String singleOccurrence = "word";
        String multipleOccurrence = "word word word";
        
        // Act
        double singleResult = calculator.calculateDocumentTfIdf(singleOccurrence);
        double multipleResult = calculator.calculateDocumentTfIdf(multipleOccurrence);
        
        // Assert
        // Multiple occurrences should have higher TF component
        assertNotEquals(singleResult, multipleResult,
            "Different word frequencies should yield different TF-IDF scores");
    }
    
    @Test
    @DisplayName("Positive: Arabic text TF-IDF calculation")
    void testTFIDF_ArabicText() {
        // Arrange
        calculator.addDocumentToCorpus("مرحبا بك");
        calculator.addDocumentToCorpus("كيف حالك");
        calculator.addDocumentToCorpus("مرحبا صديقي");
        
        String testDoc = "مرحبا";
        
        // Act
        double result = calculator.calculateDocumentTfIdf(testDoc);
        
        // Assert
        assertTrue(result > 0, "Arabic text should be processed correctly");
        assertFalse(Double.isNaN(result), "Result should not be NaN");
        assertFalse(Double.isInfinite(result), "Result should not be infinite");
    }

    // ==================== NEGATIVE TESTS ====================
    
    @Test
    @DisplayName("Negative: Empty document returns 0 or handles gracefully")
    void testTFIDF_EmptyDocument() {
        // Arrange
        calculator.addDocumentToCorpus("some content");
        String emptyDoc = "";
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            double result = calculator.calculateDocumentTfIdf(emptyDoc);
            // Should return 0 or handle gracefully without exception
            assertTrue(result == 0.0 || !Double.isNaN(result),
                "Empty document should return 0 or a valid number");
        });
    }
    
    @Test
    @DisplayName("Negative: Whitespace-only document")
    void testTFIDF_WhitespaceOnly() {
        // Arrange
        calculator.addDocumentToCorpus("real content");
        String whitespaceDoc = "     ";
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            double result = calculator.calculateDocumentTfIdf(whitespaceDoc);
            assertTrue(!Double.isNaN(result), "Should handle whitespace gracefully");
        });
    }
    
    @Test
    @DisplayName("Negative: Special characters only")
    void testTFIDF_SpecialCharactersOnly() {
        // Arrange
        calculator.addDocumentToCorpus("normal text");
        String specialCharsDoc = "@#$%^&*()";
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            double result = calculator.calculateDocumentTfIdf(specialCharsDoc);
            // Should handle special characters without throwing exception
            assertFalse(Double.isNaN(result), "Should not return NaN");
        });
    }
    
    @Test
    @DisplayName("Negative: Document with no matching words in corpus")
    void testTFIDF_NoMatchingWords() {
        // Arrange
        calculator.addDocumentToCorpus("apple orange banana");
        calculator.addDocumentToCorpus("grape mango peach");
        
        String testDoc = "car bike train"; // Completely different words
        
        // Act
        double result = calculator.calculateDocumentTfIdf(testDoc);
        
        // Assert
        assertTrue(result > 0, "New words should still have TF-IDF score");
        assertFalse(Double.isNaN(result), "Result should be a valid number");
    }
    
    @Test
    @DisplayName("Negative: Null handling (if applicable)")
    void testTFIDF_NullDocument() {
        // Arrange
        calculator.addDocumentToCorpus("content");
        
        // Act & Assert
        // This might throw NullPointerException depending on implementation
        // If it doesn't handle null, we expect an exception
        assertThrows(NullPointerException.class, () -> {
            calculator.calculateDocumentTfIdf(null);
        }, "Null document should throw exception or be handled");
    }

    // ==================== BOUNDARY TESTS ====================
    
    @Test
    @DisplayName("Boundary: Empty corpus with document")
    void testTFIDF_EmptyCorpus() {
        // Arrange - No documents added to corpus
        String testDoc = "test word";
        
        // Act
        double result = calculator.calculateDocumentTfIdf(testDoc);
        
        // Assert
        // With empty corpus, IDF calculation should handle division by zero
        assertFalse(Double.isNaN(result), "Empty corpus should be handled");
        assertFalse(Double.isInfinite(result), "Should not produce infinity");
    }
    
    @Test
    @DisplayName("Boundary: Single document in corpus")
    void testTFIDF_SingleDocumentCorpus() {
        // Arrange
        calculator.addDocumentToCorpus("single document");
        
        // Act
        double result = calculator.calculateDocumentTfIdf("single");
        
        // Assert
        assertTrue(result > 0, "Single document corpus should work");
        assertFalse(Double.isNaN(result));
    }
    
    @Test
    @DisplayName("Boundary: Very large corpus")
    void testTFIDF_LargeCorpus() {
        // Arrange - Add 100 documents
        for (int i = 0; i < 100; i++) {
            calculator.addDocumentToCorpus("document number " + i);
        }
        
        // Act
        double result = calculator.calculateDocumentTfIdf("document");
        
        // Assert
        assertTrue(result > 0, "Large corpus should be handled");
        assertFalse(Double.isNaN(result));
    }
    
    @Test
    @DisplayName("Boundary: Single character words")
    void testTFIDF_SingleCharacterWords() {
        // Arrange
        calculator.addDocumentToCorpus("a b c");
        calculator.addDocumentToCorpus("d e f");
        
        // Act
        double result = calculator.calculateDocumentTfIdf("a");
        
        // Assert
        assertTrue(result >= 0, "Single character words should be valid");
    }

    // ==================== MATHEMATICAL ACCURACY TESTS ====================
    
    @Test
    @DisplayName("Math: TF-IDF should be non-negative")
    void testTFIDF_NonNegative() {
        // Arrange
        calculator.addDocumentToCorpus("positive test");
        calculator.addDocumentToCorpus("another document");
        
        // Act
        double result = calculator.calculateDocumentTfIdf("test");
        
        // Assert
        assertTrue(result >= 0, "TF-IDF score should always be non-negative");
    }
    
    @Test
    @DisplayName("Math: Identical documents should have same TF-IDF")
    void testTFIDF_IdenticalDocuments() {
        // Arrange
        calculator.addDocumentToCorpus("test document");
        calculator.addDocumentToCorpus("other content");
        
        // Act
        double result1 = calculator.calculateDocumentTfIdf("test document");
        double result2 = calculator.calculateDocumentTfIdf("test document");
        
        // Assert
        assertEquals(result1, result2, 0.0001,
            "Identical documents should have identical TF-IDF scores");
    }
    
    @Test
    @DisplayName("Math: Known calculation with precise expected value")
    void testTFIDF_PreciseCalculation() {
        // Arrange - Very simple corpus for exact calculation
        calculator.addDocumentToCorpus("word");
        calculator.addDocumentToCorpus("text");
        
        String testDoc = "word";
        
        /*
         * Manual Calculation:
         * TF("word") = 1/1 = 1.0
         * IDF("word") = log(2/(1+1)) = log(1) = 0
         * TF-IDF = 1.0 * 0 = 0.0
         * Average = 0.0 / 1 = 0.0
         */
        
        // Act
        double result = calculator.calculateDocumentTfIdf(testDoc);
        
        // Assert
        assertEquals(0.0, result, 0.01,
            "TF-IDF for word appearing in all documents should be close to 0");
    }
}
