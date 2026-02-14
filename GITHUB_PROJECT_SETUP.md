# GitHub Project Board Setup Guide

## Project Creation

1. **Navigate to your repository**: https://github.com/AyeshaMudassar20/Arabic-Text-Editor-QA

2. **Create new Project**:
   - Click "Projects" tab
   - Click "New Project"
   - Choose "Board" template
   - Name: "Arabic Text Editor QA - Sprint Board"

## Board Columns Setup

Create the following columns:

### 1. 📋 Backlog

- **Purpose**: Future tasks and ideas
- **Tasks**: Additional test coverage, optimization ideas

### 2. 📝 To Do

- **Purpose**: Tasks ready to start
- **Tasks to add**:
  - [ ] Complete remaining test coverage (Business Layer)
  - [ ] Set up MariaDB database locally
  - [ ] Create schema.sql with table definitions
  - [ ] Fix compilation errors
  - [ ] Run and test application
  - [ ] Create Overleaf documentation
  - [ ] Add CFG diagrams
  - [ ] Calculate remaining Cyclomatic Complexity

### 3. 🔄 In Progress

- **Purpose**: Currently working on
- **Tasks**: Move here when actively coding

### 4. 👀 In Review

- **Purpose**: Awaiting testing/verification
- **Code Review**: Pull Request review

### 5. ✅ Done

- **Purpose**: Completed tasks
- **Already done**:
  - ✅ Repository setup
  - ✅ Testing structure created
  - ✅ Maven configuration
  - ✅ Business Layer tests (3/7)
  - ✅ Data Layer tests (6/8)
  - ✅ Presentation Layer tests (3/3)
  - ✅ White-Box analysis (2 features)

## Issues to Create

### Issue #1: Complete Remaining Business Layer Tests

```markdown
**Labels**: enhancement, testing
**Assignees**: AyeshaMudassar20

**Description**:
Implement JUnit test classes for remaining 4 business logic classes:

- [ ] IEditorBO interface tests
- [ ] IFacadeBO interface tests
- [ ] Additional integration tests
- [ ] Edge case coverage

**Acceptance Criteria**:

- 80%+ coverage on Business Layer
- All tests pass
- Documentation updated
```

### Issue #2: MariaDB Setup and Schema Creation

```markdown
**Labels**: database, setup
**Assignees**: AyeshaMudassar20

**Description**:
Set up MariaDB database for Arabic Text Editor

**Tasks**:

- [ ] Install MariaDB 10.x
- [ ] Create database 'arabic_editor_db'
- [ ] Create schema.sql with all tables
- [ ] Configure config.properties
- [ ] Test DatabaseConnection singleton
- [ ] Verify all DAO operations work

**Acceptance Criteria**:

- Database connection successful
- All tables created
- Sample data inserted and retrieved
```

### Issue #3: Fix Compilation Errors and Run Application

```markdown
**Labels**: bug, priority-high
**Assignees**: AyeshaMudassar20

**Description**:
Resolve all compilation errors and ensure application runs successfully

**Tasks**:

- [ ] Resolve Maven dependency issues
- [ ] Fix import errors
- [ ] Resolve method signature mismatches
- [ ] Test GUI launches correctly
- [ ] Verify all features functional

**Acceptance Criteria**:

- `mvn clean compile` succeeds with 0 errors
- Application launches without crashes
- All major features work (create, edit, save, search)
```

### Issue #4: Create Overleaf Documentation

```markdown
**Labels**: documentation
**Assignees**: AyeshaMudassar20

**Description**:
Create comprehensive LaTeX documentation for assignment submission

**Sections**:

1. Introduction & Project Overview
2. Architecture (3-Layer diagram)
3. White-Box Testing
   - Control Flow Graphs (CFG)
   - Cyclomatic Complexity calculations
   - Test Path sets
4. Test Coverage Report
5. GitHub Collaboration Evidence
6. Bug Fixes Documentation
7. Conclusion

**Acceptance Criteria**:

- Mathematical notation for V(G) = E - N + 2P
- CFG diagrams exported from Draw.io
- Test coverage tables
- GitHub screenshots (commits, issues, PRs)
```

## Branch Strategy

### Main Branches

- `master` - Production ready code
- `develop` - Integration branch for features

### Feature Branches

- `feature/remaining-tests` - Additional test implementation
- `feature/database-setup` - MariaDB configuration
- `bugfix/compilation-errors` - Bug fixes
- `docs/overleaf` - Documentation work

## Pull Request Template

```markdown
## Description

Brief description of changes

## Type of Change

- [ ] Bug fix
- [ ] New feature
- [ ] Tests added
- [ ] Documentation update

## Testing

- [ ] Tests pass locally
- [ ] New tests added for new features
- [ ] Coverage increased/maintained

## Checklist

- [ ] Code follows project style
- [ ] Self-review completed
- [ ] Comments added for complex code
- [ ] Documentation updated
- [ ] No new warnings
```

## Milestones

### Milestone 1: Complete Testing (Due: Feb 16, 2026)

- All 18 classes tested
- 80%+ overall coverage
- White-Box analysis complete

### Milestone 2: Application Running (Due: Feb 17, 2026)

- MariaDB configured
- All compilation errors fixed
- Application functional

### Milestone 3: Documentation Complete (Due: Feb 18, 2026)

- Overleaf document finished
- CFG diagrams created
- Ready for submission

## Automation

Add GitHub Actions workflow (`.github/workflows/maven-test.yml`):

```yaml
name: Java CI with Maven

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 1.8
        uses: actions/setup-java@v3
        with:
          java-version: "8"
          distribution: "temurin"
      - name: Run tests with Maven
        run: mvn clean test
      - name: Generate coverage report
        run: mvn jacoco:report
```
