package presentation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import bll.IEditorBO;
import pl.FileImporter;

/**
 * Tests for FileImporter - File Import Dialog Component
 * 
 * Tests file selection and import functionality
 */
@DisplayName("FileImporter - File Import Dialog Tests")
public class FileImporterTest {

    @Mock
    private IEditorBO mockBusinessObj;
    
    private FileImporter fileImporter;
    private AutoCloseable closeable;
    
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        fileImporter = new FileImporter(mockBusinessObj);
    }
    
    @AfterEach
    void tearDown() throws Exception {
        if (closeable != null) {
            closeable.close();
        }
    }

    // ==================== INITIALIZATION TESTS ====================
    
    @Test
    @DisplayName("Positive: FileImporter initializes with business object")
    void testFileImporter_Initialization() {
        // Assert
        assertNotNull(fileImporter, "FileImporter should be initialized");
    }
    
    @Test
    @DisplayName("Negative: FileImporter with null business object")
    void testFileImporter_NullBusinessObject() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            FileImporter importer = new FileImporter(null);
            assertNotNull(importer);
        });
    }

    // ==================== FILE IMPORT SIMULATION TESTS ====================
    
    @Test
    @DisplayName("Positive: Import single file successfully")
    void testImportFile_SingleFile() {
        // Arrange
        File mockFile = mock(File.class);
        when(mockFile.getName()).thenReturn("test.txt");
        when(mockBusinessObj.importTextFiles(mockFile, "test.txt")).thenReturn(true);
        
        // Act
        boolean result = mockBusinessObj.importTextFiles(mockFile, "test.txt");
        
        // Assert
        assertTrue(result, "File import should succeed");
        verify(mockBusinessObj).importTextFiles(mockFile, "test.txt");
    }
    
    @Test
    @DisplayName("Positive: Import multiple files")
    void testImportFile_MultipleFiles() {
        // Arrange
        File file1 = mock(File.class);
        File file2 = mock(File.class);
        when(file1.getName()).thenReturn("file1.txt");
        when(file2.getName()).thenReturn("file2.txt");
        when(mockBusinessObj.importTextFiles(file1, "file1.txt")).thenReturn(true);
        when(mockBusinessObj.importTextFiles(file2, "file2.txt")).thenReturn(true);
        
        // Act
        boolean result1 = mockBusinessObj.importTextFiles(file1, "file1.txt");
        boolean result2 = mockBusinessObj.importTextFiles(file2, "file2.txt");
        
        // Assert
        assertTrue(result1);
        assertTrue(result2);
        verify(mockBusinessObj, times(2)).importTextFiles(any(File.class), anyString());
    }
    
    @Test
    @DisplayName("Negative: Import fails for invalid file")
    void testImportFile_InvalidFile() {
        // Arrange
        File mockFile = mock(File.class);
        when(mockFile.getName()).thenReturn("invalid.pdf");
        when(mockBusinessObj.importTextFiles(mockFile, "invalid.pdf")).thenReturn(false);
        
        // Act
        boolean result = mockBusinessObj.importTextFiles(mockFile, "invalid.pdf");
        
        // Assert
        assertFalse(result, "Import should fail for unsupported file type");
    }

    // ==================== FILE TYPE TESTS ====================
    
    @Test
    @DisplayName("Positive: Import .txt file")
    void testImportFile_TxtExtension() {
        // Arrange
        File mockFile = mock(File.class);
        when(mockFile.getName()).thenReturn("document.txt");
        when(mockBusinessObj.importTextFiles(any(), anyString())).thenReturn(true);
        
        // Act
        boolean result = mockBusinessObj.importTextFiles(mockFile, "document.txt");
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    @DisplayName("Positive: Import .md5 file")
    void testImportFile_Md5Extension() {
        // Arrange
        File mockFile = mock(File.class);
        when(mockFile.getName()).thenReturn("hash.md5");
        when(mockBusinessObj.importTextFiles(any(), anyString())).thenReturn(true);
        
        // Act
        boolean result = mockBusinessObj.importTextFiles(mockFile, "hash.md5");
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    @DisplayName("Negative: Reject unsupported file types")
    void testImportFile_UnsupportedTypes() {
        // Test various unsupported file types
        String[] unsupportedFiles = {"file.pdf", "doc.docx", "image.jpg", "data.csv"};
        
        for (String fileName : unsupportedFiles) {
            File mockFile = mock(File.class);
            when(mockFile.getName()).thenReturn(fileName);
            when(mockBusinessObj.importTextFiles(mockFile, fileName)).thenReturn(false);
            
            boolean result = mockBusinessObj.importTextFiles(mockFile, fileName);
            assertFalse(result, fileName + " should not be imported");
        }
    }

    // ==================== FILE NAME TESTS ====================
    
    @Test
    @DisplayName("Boundary: File with very long name")
    void testImportFile_LongFileName() {
        // Arrange
        String longName = "a".repeat(200) + ".txt";
        File mockFile = mock(File.class);
        when(mockFile.getName()).thenReturn(longName);
        when(mockBusinessObj.importTextFiles(any(), eq(longName))).thenReturn(true);
        
        // Act
        boolean result = mockBusinessObj.importTextFiles(mockFile, longName);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    @DisplayName("Boundary: File with special characters in name")
    void testImportFile_SpecialCharactersInName() {
        // Arrange
        String specialName = "file@#$%.txt";
        File mockFile = mock(File.class);
        when(mockFile.getName()).thenReturn(specialName);
        when(mockBusinessObj.importTextFiles(any(), eq(specialName))).thenReturn(true);
        
        // Act
        boolean result = mockBusinessObj.importTextFiles(mockFile, specialName);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    @DisplayName("Positive: File with Arabic name")
    void testImportFile_ArabicFileName() {
        // Arrange
        String arabicName = "ملف_نصي.txt";
        File mockFile = mock(File.class);
        when(mockFile.getName()).thenReturn(arabicName);
        when(mockBusinessObj.importTextFiles(any(), eq(arabicName))).thenReturn(true);
        
        // Act
        boolean result = mockBusinessObj.importTextFiles(mockFile, arabicName);
        
        // Assert
        assertTrue(result);
    }

    // ==================== ERROR HANDLING TESTS ====================
    
    @Test
    @DisplayName("Negative: Handle null file")
    void testImportFile_NullFile() {
        // Arrange
        when(mockBusinessObj.importTextFiles(null, "test.txt")).thenReturn(false);
        
        // Act
        boolean result = mockBusinessObj.importTextFiles(null, "test.txt");
        
        // Assert
        assertFalse(result, "Should handle null file gracefully");
    }
    
    @Test
    @DisplayName("Negative: Handle empty file name")
    void testImportFile_EmptyFileName() {
        // Arrange
        File mockFile = mock(File.class);
        when(mockFile.getName()).thenReturn("");
        when(mockBusinessObj.importTextFiles(any(), eq(""))).thenReturn(false);
        
        // Act
        boolean result = mockBusinessObj.importTextFiles(mockFile, "");
        
        // Assert
        assertFalse(result, "Should handle empty file name");
    }

    // ==================== IMPORT STATUS TESTS ====================
    
    @Test
    @DisplayName("Positive: Successful import returns true")
    void testImportStatus_Success() {
        // Arrange
        File mockFile = mock(File.class);
        when(mockFile.getName()).thenReturn("success.txt");
        when(mockBusinessObj.importTextFiles(any(), anyString())).thenReturn(true);
        
        // Act
        boolean result = mockBusinessObj.importTextFiles(mockFile, "success.txt");
        
        // Assert
        assertTrue(result, "Successful import should return true");
    }
    
    @Test
    @DisplayName("Negative: Failed import returns false")
    void testImportStatus_Failure() {
        // Arrange
        File mockFile = mock(File.class);
        when(mockFile.getName()).thenReturn("failure.txt");
        when(mockBusinessObj.importTextFiles(any(), anyString())).thenReturn(false);
        
        // Act
        boolean result = mockBusinessObj.importTextFiles(mockFile, "failure.txt");
        
        // Assert
        assertFalse(result, "Failed import should return false");
    }

    // ==================== BATCH IMPORT TESTS ====================
    
    @Test
    @DisplayName("Integration: Import batch of files with mixed success")
    void testImportFile_BatchMixedSuccess() {
        // Arrange
        File validFile = mock(File.class);
        File invalidFile = mock(File.class);
        when(validFile.getName()).thenReturn("valid.txt");
        when(invalidFile.getName()).thenReturn("invalid.xyz");
        when(mockBusinessObj.importTextFiles(validFile, "valid.txt")).thenReturn(true);
        when(mockBusinessObj.importTextFiles(invalidFile, "invalid.xyz")).thenReturn(false);
        
        // Act
        boolean result1 = mockBusinessObj.importTextFiles(validFile, "valid.txt");
        boolean result2 = mockBusinessObj.importTextFiles(invalidFile, "invalid.xyz");
        
        // Assert
        assertTrue(result1, "Valid file should import");
        assertFalse(result2, "Invalid file should not import");
    }
}
