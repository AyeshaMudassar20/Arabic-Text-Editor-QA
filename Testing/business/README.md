# Business Logic Layer Tests

This directory contains JUnit tests for the Business Logic Layer.

## Components Under Test

- **FacadeBO.java** - Facade pattern implementation
- **EditorBO.java** - Core editor business logic
- **SearchWord.java** - Search and replace functionality
- Command Pattern implementations

## Test Classes

- [ ] FacadeBOTest.java
- [ ] EditorBOTest.java
- [ ] SearchWordTest.java
- [ ] ImportCommandTest.java
- [ ] ExportCommandTest.java
- [ ] AutoSaveTest.java
- [ ] PaginationLogicTest.java

## White-Box Analysis

### Selected Features for CFG Analysis

1. **Search & Replace Word** - Logic involving find/replace success flags
2. **Pagination Logic** - Logic splitting content based on word limits
3. **Auto-Save Trigger** - Logic checking word count > 500

### Deliverables

- Control Flow Graphs (CFG)
- Cyclomatic Complexity calculations: V(G) = E - N + 2P
- Test Path Sets using Set Notation

## Test Scenarios

### Positive Tests

- Successful command execution
- Valid business logic workflows
- Correct algorithm outputs

### Negative Tests

- Command execution failures
- Invalid input to business logic
- Edge case handling

### Boundary Tests

- 500-word threshold for auto-save
- Page size limits for pagination
- Empty document handling
