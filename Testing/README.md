# Testing Suite for Arabic Text Editor

This directory contains comprehensive JUnit test suites for all three architectural layers of the Arabic Text Editor application.

## Directory Structure

- **presentation/** - Tests for Presentation Layer (UI Components)
- **business/** - Tests for Business Logic Layer (Commands, Algorithms)
- **data/** - Tests for Data Access Layer (Database, Persistence)

## Test Coverage Goals

- White-Box Testing with CFG and Cyclomatic Complexity Analysis
- Positive, Negative, and Boundary Test Cases
- Command Pattern Testing (ImportCommand, ExportCommand, etc.)
- TF-IDF Algorithm Validation
- Database Integrity and Singleton Pattern Testing
- Hash Verification (MD5/SHA1)

## Running Tests

```bash
# Run all tests
mvn test

# Run specific layer tests
mvn test -Dtest=presentation.*
mvn test -Dtest=business.*
mvn test -Dtest=data.*
```

## Team

- Group: [Your Group Name]
- Course: Software Testing (Software Verification & Validation)
- Semester: Spring 2026
