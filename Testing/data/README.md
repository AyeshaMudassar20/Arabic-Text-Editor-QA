# Data Access Layer Tests

This directory contains JUnit tests for the Data Access Layer with database mocking.

## Components Under Test

- **DatabaseConnection.java** - Singleton database connection
- **EditorDBDAO.java** - Database operations
- **FacadeDAO.java** - Data access facade
- **HashCalculator.java** - MD5/SHA1 hashing
- **TFIDFCalculator.java** - TF-IDF algorithm
- **PaginationDAO.java** - Pagination data access

## Test Classes

- [ ] DatabaseConnectionTest.java - Singleton pattern verification
- [ ] EditorDBDAOTest.java - CRUD operations with mocking
- [ ] HashCalculatorTest.java - MD5/SHA1 integrity testing
- [ ] TFIDFCalculatorTest.java - Algorithm validation
- [ ] PaginationDAOTest.java - Pagination logic

## Mocking Strategy

- Use Mockito/JUnit mocking for database operations
- Mock MariaDB connections for unit testing
- Verify SQL query generation without actual database

## Test Scenarios

### Hash Integrity Tests

- [ ] Verify MD5 hash generation
- [ ] Verify SHA1 hash generation
- [ ] Verify editing changes current session hash
- [ ] Verify original import hash retention in metadata

### Singleton Tests

- [ ] Verify only one instance of DatabaseConnection
- [ ] Verify thread safety of Singleton
- [ ] Verify connection reuse

### TF-IDF Tests

- [ ] Positive: Known document with manual calculation (±0.01 tolerance)
- [ ] Negative: Empty document handling
- [ ] Negative: Special characters handling

### Database Tests

- [ ] CRUD operations with mock database
- [ ] Transaction rollback scenarios
- [ ] Connection pool management
