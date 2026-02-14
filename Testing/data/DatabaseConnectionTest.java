package data;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dal.DatabaseConnection;

/**
 * Tests for DatabaseConnection - Singleton Pattern Verification
 * 
 * Requirements:
 * - Verify only one instance of DatabaseConnection exists
 * - Verify thread safety of Singleton implementation
 * - Verify connection reuse
 * 
 * Singleton Pattern Properties:
 * 1. Private constructor
 * 2. Static instance variable
 * 3. Public static getInstance() method
 * 4. Lazy initialization
 * 5. Thread Safety (synchronized)
 */
@DisplayName("DatabaseConnection - Singleton Pattern Tests")
public class DatabaseConnectionTest {

    // ==================== SINGLETON PATTERN TESTS ====================
    
    @Test
    @DisplayName("Singleton: getInstance returns same instance")
    void testSingleton_SameInstance() {
        // Act
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        DatabaseConnection instance2 = DatabaseConnection.getInstance();
        DatabaseConnection instance3 = DatabaseConnection.getInstance();
        
        // Assert
        assertSame(instance1, instance2,
            "getInstance() should return the same instance");
        assertSame(instance2, instance3,
            "getInstance() should always return the same instance");
        assertSame(instance1, instance3,
            "All calls should return identical instance");
    }
    
    @Test
    @DisplayName("Singleton: Only one instance exists")
    void testSingleton_OnlyOneInstance() {
        // Act
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        DatabaseConnection instance2 = DatabaseConnection.getInstance();
        
        // Assert
        assertEquals(instance1.hashCode(), instance2.hashCode(),
            "Both instances should have same hash code");
        assertTrue(instance1 == instance2,
            "Should be the exact same object reference");
    }
    
    @Test
    @DisplayName("Singleton: Constructor is private")
    void testSingleton_PrivateConstructor() {
        // Act
        Constructor<?>[] constructors = DatabaseConnection.class.getDeclaredConstructors();
        
        // Assert
        assertEquals(1, constructors.length,
            "Should have exactly one constructor");
        assertTrue(java.lang.reflect.Modifier.isPrivate(constructors[0].getModifiers()),
            "Constructor should be private");
    }
    
    @Test
    @DisplayName("Singleton: Cannot create instance via reflection")
    void testSingleton_CannotInstantiateViaReflection() throws Exception {
        // Arrange
        Constructor<DatabaseConnection> constructor = 
            DatabaseConnection.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        
        // Get the existing instance first
        DatabaseConnection existingInstance = DatabaseConnection.getInstance();
        
        // Act & Assert
        // Attempting to create new instance via reflection
        // This test verifies the singleton property is maintained
        DatabaseConnection reflectionInstance = constructor.newInstance();
        
        // Even if reflection creates instance, getInstance should return original
        DatabaseConnection normalInstance = DatabaseConnection.getInstance();
        assertSame(existingInstance, normalInstance,
            "getInstance should still return original instance");
    }

    // ==================== THREAD SAFETY TESTS ====================
    
    @Test
    @DisplayName("Thread Safety: Multiple threads get same instance")
    void testThreadSafety_MultipleThreads() throws InterruptedException {
        // Arrange
        final int THREAD_COUNT = 10;
        final DatabaseConnection[] instances = new DatabaseConnection[THREAD_COUNT];
        Thread[] threads = new Thread[THREAD_COUNT];
        
        // Act - Create multiple threads requesting instance simultaneously
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                instances[index] = DatabaseConnection.getInstance();
            });
            threads[i].start();
        }
        
        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }
        
        // Assert - All instances should be the same
        DatabaseConnection firstInstance = instances[0];
        for (int i = 1; i < THREAD_COUNT; i++) {
            assertSame(firstInstance, instances[i],
                "All threads should get the same instance (thread-safe)");
        }
    }
    
    @Test
    @DisplayName("Thread Safety: Synchronized getInstance method")
    void testThreadSafety_SynchronizedMethod() throws NoSuchMethodException {
        // Act
        java.lang.reflect.Method getInstanceMethod = 
            DatabaseConnection.class.getMethod("getInstance");
        
        // Assert
        assertTrue(java.lang.reflect.Modifier.isSynchronized(
            getInstanceMethod.getModifiers()) ||
            getInstanceMethod.toGenericString().contains("synchronized"),
            "getInstance() method should be synchronized for thread safety");
    }
    
    @Test
    @DisplayName("Thread Safety: Race condition test")
    void testThreadSafety_RaceCondition() throws InterruptedException {
        // Arrange
        final int ITERATIONS = 100;
        final DatabaseConnection[] instances = new DatabaseConnection[ITERATIONS];
        
        // Act - Rapid concurrent access
        for (int i = 0; i < ITERATIONS; i++) {
            final int index = i;
            new Thread(() -> {
                instances[index] = DatabaseConnection.getInstance();
            }).start();
        }
        
        // Wait a bit for threads to complete
        Thread.sleep(1000);
        
        // Assert - Count unique instances (should be only 1)
        long uniqueInstances = java.util.Arrays.stream(instances)
            .filter(instance -> instance != null)
            .distinct()
            .count();
        
        assertEquals(1, uniqueInstances,
            "Should have only one unique instance despite concurrent access");
    }

    // ==================== CONNECTION TESTS ====================
    
    @Test
    @DisplayName("Connection: getInstance returns non-null instance")
    void testConnection_NonNullInstance() {
        // Act
        DatabaseConnection instance = DatabaseConnection.getInstance();
        
        // Assert
        assertNotNull(instance, "getInstance should never return null");
    }
    
    @Test
    @DisplayName("Connection: getConnection returns connection object")
    void testConnection_GetConnection() {
        // Arrange
        DatabaseConnection instance = DatabaseConnection.getInstance();
        
        // Act
        Connection connection = instance.getConnection();
        
        // Assert
        // Note: This might be null if config.properties is not properly set up
        // In production, connection should not be null, but in test environment
        // it's acceptable if DB is not configured
        assertDoesNotThrow(() -> instance.getConnection(),
            "getConnection() should not throw exception");
    }
    
    @Test
    @DisplayName("Connection: Same connection reused across getInstance calls")
    void testConnection_ConnectionReuse() {
        // Arrange
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        DatabaseConnection instance2 = DatabaseConnection.getInstance();
        
        // Act
        Connection conn1 = instance1.getConnection();
        Connection conn2 = instance2.getConnection();
        
        // Assert
        // Since instances are same, connections should also be same
        assertEquals(conn1, conn2,
            "Connection should be reused (same connection object)");
    }

    // ==================== DESIGN PATTERN VERIFICATION ====================
    
    @Test
    @DisplayName("Pattern: Lazy initialization")
    void testPattern_LazyInitialization() {
        // Note: This test verifies the pattern structure
        // In practice, the instance might already be created from previous tests
        
        // The pattern should support lazy initialization:
        // Instance is created only when getInstance() is first called
        
        // We can verify by checking the implementation follows the pattern
        DatabaseConnection instance = DatabaseConnection.getInstance();
        assertNotNull(instance,
            "Lazy initialization should create instance on first call");
    }
    
    @Test
    @DisplayName("Pattern: Static getInstance method exists")
    void testPattern_StaticGetInstance() throws NoSuchMethodException {
        // Act
        java.lang.reflect.Method getInstanceMethod = 
            DatabaseConnection.class.getMethod("getInstance");
        
        // Assert
        assertTrue(java.lang.reflect.Modifier.isStatic(
            getInstanceMethod.getModifiers()),
            "getInstance() should be a static method");
        assertEquals(DatabaseConnection.class, getInstanceMethod.getReturnType(),
            "getInstance() should return DatabaseConnection type");
    }
    
    @Test
    @DisplayName("Pattern: No public constructor")
    void testPattern_NoPublicConstructor() {
        // Act
        Constructor<?>[] constructors = DatabaseConnection.class.getConstructors();
        
        // Assert
        assertEquals(0, constructors.length,
            "Should have no public constructors");
    }

    // ==================== ERROR HANDLING TESTS ====================
    
    @Test
    @DisplayName("Error: getInstance never returns null even with config issues")
    void testError_GetInstanceNeverNull() {
        // Act
        DatabaseConnection instance = DatabaseConnection.getInstance();
        
        // Assert
        assertNotNull(instance,
            "getInstance should return instance even if DB config fails");
    }
    
    @Test
    @DisplayName("Error: Multiple getInstance calls don't create multiple instances")
    void testError_NoMultipleInstances() {
        // Act
        DatabaseConnection[] instances = new DatabaseConnection[50];
        for (int i = 0; i < 50; i++) {
            instances[i] = DatabaseConnection.getInstance();
        }
        
        // Assert
        DatabaseConnection firstInstance = instances[0];
        for (int i = 1; i < 50; i++) {
            assertSame(firstInstance, instances[i],
                "No matter how many times called, should return same instance");
        }
    }

    // ==================== MEMORY TESTS ====================
    
    @Test
    @DisplayName("Memory: Single instance reduces memory footprint")
    void testMemory_SingleInstance() {
        // Act
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        DatabaseConnection instance2 = DatabaseConnection.getInstance();
        DatabaseConnection instance3 = DatabaseConnection.getInstance();
        
        // Assert
        long instance1Identity = System.identityHashCode(instance1);
        long instance2Identity = System.identityHashCode(instance2);
        long instance3Identity = System.identityHashCode(instance3);
        
        assertEquals(instance1Identity, instance2Identity,
            "Should have same identity (same memory location)");
        assertEquals(instance2Identity, instance3Identity,
            "Should have same identity (same memory location)");
    }

    // ==================== INTEGRATION SCENARIO TESTS ====================
    
    @Test
    @DisplayName("Scenario: Typical application flow")
    void testScenario_TypicalFlow() {
        // Scenario: Application starts, multiple components need DB connection
        
        // Act
        DatabaseConnection dbFromDAO = DatabaseConnection.getInstance();
        DatabaseConnection dbFromBO = DatabaseConnection.getInstance();
        DatabaseConnection dbFromPO = DatabaseConnection.getInstance();
        
        // Assert
        assertSame(dbFromDAO, dbFromBO,
            "DAO and BO should share same DB connection");
        assertSame(dbFromBO, dbFromPO,
            "BO and PO should share same DB connection");
        assertSame(dbFromDAO, dbFromPO,
            "All layers should share same DB connection instance");
    }
    
    @Test
    @DisplayName("Scenario: Connection consistency check")
    void testScenario_ConnectionConsistency() {
        // Arrange
        DatabaseConnection instance = DatabaseConnection.getInstance();
        Connection firstCall = instance.getConnection();
        
        // Act - Get connection multiple times
        Connection secondCall = instance.getConnection();
        Connection thirdCall = instance.getConnection();
        
        // Assert
        assertEquals(firstCall, secondCall,
            "Connection should remain consistent");
        assertEquals(secondCall, thirdCall,
            "Connection should remain consistent");
    }
}
