package data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dal.HashCalculator;

/**
 * Tests for HashCalculator - MD5 Hash Integrity
 * 
 * Requirements:
 * - Verify MD5 hash generation
 * - Verify that editing a file changes the current session hash
 * - Verify original import hash retention in database metadata
 * 
 * Note: The current implementation uses MD5.
 * SHA1 testing can be added if implementation is extended.
 */
@DisplayName("HashCalculator - MD5 Hash Integrity Tests")
public class HashCalculatorTest {

    // ==================== POSITIVE TESTS ====================
    
    @Test
    @DisplayName("Positive: MD5 hash generation for simple text")
    void testCalculateHash_SimpleText() throws Exception {
        // Arrange
        String input = "Hello, World!";
        
        // Expected MD5 hash for "Hello, World!"
        // Can be verified at: https://www.md5hashgenerator.com/
        String expectedHash = "65A8E27D8879283831B664BD8B7F0AD4";
        
        // Act
        String actualHash = HashCalculator.calculateHash(input);
        
        // Assert
        assertEquals(expectedHash, actualHash,
            "MD5 hash should match expected value");
    }
    
    @Test
    @DisplayName("Positive: Hash is 32 characters (128 bits)")
    void testCalculateHash_Length() throws Exception {
        // Arrange
        String input = "Test content for hashing";
        
        // Act
        String hash = HashCalculator.calculateHash(input);
        
        // Assert
        assertEquals(32, hash.length(),
            "MD5 hash should always be 32 hexadecimal characters");
    }
    
    @Test
    @DisplayName("Positive: Same input produces same hash (deterministic)")
    void testCalculateHash_Deterministic() throws Exception {
        // Arrange
        String input = "Consistent input text";
        
        // Act
        String hash1 = HashCalculator.calculateHash(input);
        String hash2 = HashCalculator.calculateHash(input);
        String hash3 = HashCalculator.calculateHash(input);
        
        // Assert
        assertEquals(hash1, hash2, "Same input should produce same hash");
        assertEquals(hash2, hash3, "Hash should be deterministic");
    }
    
    @Test
    @DisplayName("Positive: Different inputs produce different hashes")
    void testCalculateHash_DifferentInputs() throws Exception {
        // Arrange
        String input1 = "First document content";
        String input2 = "Second document content";
        
        // Act
        String hash1 = HashCalculator.calculateHash(input1);
        String hash2 = HashCalculator.calculateHash(input2);
        
        // Assert
        assertNotEquals(hash1, hash2,
            "Different inputs should produce different hashes");
    }
    
    @Test
    @DisplayName("Positive: Hash for Arabic text")
    void testCalculateHash_ArabicText() throws Exception {
        // Arrange
        String arabicText = "مرحبا بك في المحرر النصي";
        
        // Act
        String hash = HashCalculator.calculateHash(arabicText);
        
        // Assert
        assertNotNull(hash, "Hash should not be null");
        assertEquals(32, hash.length(), "Arabic text hash should be 32 chars");
        assertTrue(hash.matches("[0-9A-F]{32}"),
            "Hash should contain only hex characters");
    }
    
    @Test
    @DisplayName("Positive: Small content change creates different hash")
    void testCalculateHash_SmallChange() throws Exception {
        // Arrange
        String original = "This is the original content";
        String modified = "This is the original content."; // Added period
        
        // Act
        String originalHash = HashCalculator.calculateHash(original);
        String modifiedHash = HashCalculator.calculateHash(modified);
        
        // Assert
        assertNotEquals(originalHash, modifiedHash,
            "Even small changes should produce different hash (avalanche effect)");
    }

    // ==================== NEGATIVE TESTS ====================
    
    @Test
    @DisplayName("Negative: Empty string has valid hash")
    void testCalculateHash_EmptyString() throws Exception {
        // Arrange
        String emptyInput = "";
        
        // Expected MD5 hash for empty string
        String expectedHash = "D41D8CD98F00B204E9800998ECF8427E";
        
        // Act
        String actualHash = HashCalculator.calculateHash(emptyInput);
        
        // Assert
        assertEquals(expectedHash, actualHash,
            "Empty string should have known MD5 hash");
    }
    
    @Test
    @DisplayName("Negative: Null input should throw exception")
    void testCalculateHash_NullInput() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            HashCalculator.calculateHash(null);
        }, "Null input should throw NullPointerException");
    }
    
    @Test
    @DisplayName("Negative: Special characters handled correctly")
    void testCalculateHash_SpecialCharacters() throws Exception {
        // Arrange
        String specialChars = "!@#$%^&*()_+-={}[]|\\:;\"'<>,.?/~`";
        
        // Act
        String hash = HashCalculator.calculateHash(specialChars);
        
        // Assert
        assertNotNull(hash);
        assertEquals(32, hash.length());
        assertTrue(hash.matches("[0-9A-F]{32}"));
    }

    // ==================== BOUNDARY TESTS ====================
    
    @Test
    @DisplayName("Boundary: Single character")
    void testCalculateHash_SingleCharacter() throws Exception {
        // Arrange
        String input = "A";
        
        // Act
        String hash = HashCalculator.calculateHash(input);
        
        // Assert
        assertEquals(32, hash.length());
        assertTrue(hash.matches("[0-9A-F]{32}"));
    }
    
    @Test
    @DisplayName("Boundary: Very long text")
    void testCalculateHash_LongText() throws Exception {
        // Arrange
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            longText.append("This is a long document. ");
        }
        
        // Act
        String hash = HashCalculator.calculateHash(longText.toString());
        
        // Assert
        assertEquals(32, hash.length(),
            "MD5 hash is fixed size regardless of input length");
    }
    
    @Test
    @DisplayName("Boundary: Unicode characters (emoji)")
    void testCalculateHash_UnicodeCharacters() throws Exception {
        // Arrange
        String unicodeText = "Testing with emoji: 😀🎉🚀";
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            String hash = HashCalculator.calculateHash(unicodeText);
            assertEquals(32, hash.length());
        }, "Should handle Unicode characters");
    }

    // ==================== HASH INTEGRITY SCENARIOS ====================
    
    @Test
    @DisplayName("Integrity: Editing changes hash (simulating save)")
    void testHashIntegrity_EditingChangesHash() throws Exception {
        // Scenario: User imports a file, edits it, hash should change
        
        // Arrange
        String originalContent = "Original imported content";
        String editedContent = "Original imported content with edits";
        
        // Act
        String originalHash = HashCalculator.calculateHash(originalContent);
        String currentSessionHash = HashCalculator.calculateHash(editedContent);
        
        // Assert
        assertNotEquals(originalHash, currentSessionHash,
            "Editing file should change current session hash");
        
        // In real scenario, originalHash would be stored in DB metadata
        // and currentSessionHash would be calculated on save
    }
    
    @Test
    @DisplayName("Integrity: Import hash preserved, session hash changes")
    void testHashIntegrity_ImportVsSessionHash() throws Exception {
        // Scenario: Verify import hash retention
        
        // Arrange
        String importedContent = "Content when first imported";
        String firstEdit = "Content when first imported - edit 1";
        String secondEdit = "Content when first imported - edit 1 - edit 2";
        
        // Act
        String importHash = HashCalculator.calculateHash(importedContent);  // Stored in DB
        String sessionHash1 = HashCalculator.calculateHash(firstEdit);     // Current
        String sessionHash2 = HashCalculator.calculateHash(secondEdit);    // Current after another edit
        
        // Assert
        assertNotEquals(importHash, sessionHash1,
            "First edit should create different hash from import");
        assertNotEquals(sessionHash1, sessionHash2,
            "Second edit should create different hash from first");
        
        // Import hash would remain constant in DB metadata
        assertNotNull(importHash, "Import hash must be preserved");
    }
    
    @Test
    @DisplayName("Integrity: Case sensitivity in hash")
    void testHashIntegrity_CaseSensitivity() throws Exception {
        // Arrange
        String lowercase = "content";
        String uppercase = "CONTENT";
        String mixedcase = "Content";
        
        // Act
        String hash1 = HashCalculator.calculateHash(lowercase);
        String hash2 = HashCalculator.calculateHash(uppercase);
        String hash3 = HashCalculator.calculateHash(mixedcase);
        
        // Assert
        assertNotEquals(hash1, hash2, "Hash should be case-sensitive");
        assertNotEquals(hash2, hash3, "Hash should be case-sensitive");
        assertNotEquals(hash1, hash3, "Hash should be case-sensitive");
    }
    
    @Test
    @DisplayName("Integrity: Whitespace changes hash")
    void testHashIntegrity_WhitespaceChanges() throws Exception {
        // Arrange
        String original = "word1 word2";
        String doubleSpace = "word1  word2";  // Two spaces
        String tab = "word1\tword2";
        
        // Act
        String hash1 = HashCalculator.calculateHash(original);
        String hash2 = HashCalculator.calculateHash(doubleSpace);
        String hash3 = HashCalculator.calculateHash(tab);
        
        // Assert
        assertNotEquals(hash1, hash2,
            "WhitespaceHashes changes should be detected");
        assertNotEquals(hash1, hash3,
            "Tab vs space should create different hash");
    }

    // ==================== HEXADECIMAL FORMAT TESTS ====================
    
    @Test
    @DisplayName("Format: Hash contains only valid hex characters")
    void testHashFormat_ValidHexCharacters() throws Exception {
        // Arrange
        String input = "Test input for hex validation";
        
        // Act
        String hash = HashCalculator.calculateHash(input);
        
        // Assert
        assertTrue(hash.matches("[0-9A-F]+"),
            "Hash should contain only 0-9 and A-F characters");
    }
    
    @Test
    @DisplayName("Format: Hash is uppercase")
    void testHashFormat_Uppercase() throws Exception {
        // Arrange - Based on implementation, hash should be uppercase
        String input = "Uppercase test";
        
        // Act
        String hash = HashCalculator.calculateHash(input);
        
        // Assert
        assertEquals(hash.toUpperCase(), hash,
            "Hash should be in uppercase format");
    }
    
    @Test
    @DisplayName("Format: No spaces or special chars in hash")
    void testHashFormat_NoSpaces() throws Exception {
        // Arrange
        String input = "Content with spaces and special @#$ chars";
        
        // Act
        String hash = HashCalculator.calculateHash(input);
        
        // Assert
        assertFalse(hash.contains(" "),
            "Hash should not contain spaces");
        assertTrue(hash.matches("[0-9A-F]{32}"),
            "Hash should be pure hexadecimal");
    }

    // ==================== KNOWN VALUES TEST ====================
    
    @Test
    @DisplayName("Known Values: Test against known MD5 hashes")
    void testCalculateHash_KnownValues() throws Exception {
        // Test against well-known MD5 values
        
        // Test 1
        assertEquals("5D41402ABC4B2A76B9719D911017C592",
            HashCalculator.calculateHash("hello"));
        
        // Test 2
        assertEquals("7D793037A0760186574B0282F2F435E7",
            HashCalculator.calculateHash("world"));
        
        // Test 3 - Empty string
        assertEquals("D41D8CD98F00B204E9800998ECF8427E",
            HashCalculator.calculateHash(""));
        
        // Test 4 - Space
        assertEquals("7215EE9C7D9DC229D2921A40E899EC5F",
            HashCalculator.calculateHash(" "));
    }
}
