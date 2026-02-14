# GitHub Workflow Setup Guide

This guide will help you implement the professional GitHub workflow required for the Software Testing assignment.

---

## Step 1: Set Up GitHub Project (Kanban Board)

1. Go to your repository: https://github.com/AyeshaMudassar20/Arabic-Text-Editor-QA
2. Click on the **"Projects"** tab
3. Click **"New project"**
4. Choose **"Board"** template
5. Name it: **"Text Editor QA Testing Suite"**
6. Click **"Create"**

### Configure Columns

Add these columns to your project board:

1. **📝 Backlog** - Future tasks/ideas
2. **📌 To Do** - Ready to work on
3. **🔄 In Progress** - Currently working
4. **👀 In Review** - Awaiting review/testing
5. **✅ Done** - Completed tasks

---

## Step 2: Create GitHub Issues

Go to **Issues** → **New Issue** and create these:

### Issue #1: Implement Business Layer Tests
```markdown
**Title**: feat: Implement FacadeBO and EditorBO JUnit tests

**Description**:
Create comprehensive JUnit test suites for Business Logic Layer components.

**Tasks**:
- [ ] Create FacadeBOTest.java with facade pattern tests
- [ ] Create EditorBOTest.java with business logic tests
- [ ] Include positive, negative, and boundary test cases
- [ ] Document CFG and Cyclomatic Complexity
- [ ] Achieve 80%+ code coverage

**Acceptance Criteria**:
- All tests pass successfully
- Code coverage report generated
- Tests follow AAA pattern
- Proper JavaDoc documentation

**Labels**: `enhancement`, `testing`, `business-layer`
**Assignees**: AyeshaMudassar20
**Projects**: Text Editor QA Testing Suite
**Milestone**: Phase B - Test Implementation
```

### Issue #2: Implement Presentation Layer Tests
```markdown
**Title**: test: Add comprehensive UI and Presentation layer tests

**Description**:
Implement JUnit tests for all Presentation Layer components including EditorPO, FileImporter, and SearchFrame.

**Tasks**:
- [ ] Create EditorPOTest.java with Auto-Save logic tests (>500 words)
- [ ] Create FileImporterTest.java for import functionality
- [ ] Create SearchFrameTest.java for search UI
- [ ] Test user interaction workflows
- [ ] Test exception handling in UI components

**Notes**:
- EditorPO contains Auto-Save trigger logic (word count > 500)
- This is a critical feature for White-Box analysis

**Labels**: `testing`, `presentation-layer`, `ui-tests`
**Assignees**: AyeshaMudassar20
```

### Issue #3: Set Up MariaDB and Database Configuration
```markdown
**Title**: chore: Configure MariaDB database and test connection

**Description**:
Set up local MariaDB instance and configure database connection for the application.

**Tasks**:
- [ ] Install MariaDB 10.x or higher
- [ ] Create `text_editor_db` database
- [ ] Execute SQL schema from `resource/Database/EditorDBQuery.sql`
- [ ] Update `config.properties` with local credentials
- [ ] Test database connection
- [ ] Verify CRUD operations work

**Database Setup Commands**:
```sql
CREATE DATABASE text_editor_db;
USE text_editor_db;
-- Run EditorDBQuery.sql
```

**Labels**: `chore`, `database`, `configuration`
**Priority**: High
```

### Issue #4: White-Box Analysis Documentation
```markdown
**Title**: docs: Complete White-Box analysis documentation

**Description**:
Create comprehensive documentation of CFG, Cyclomatic Complexity, and Test Paths for all analyzed features.

**Tasks**:
- [ ] Draw CFG diagrams for Search & Replace logic
- [ ] Draw CFG diagram for Pagination logic
- [ ] Draw CFG diagram for Auto-Save trigger
- [ ] Calculate Cyclomatic Complexity: V(G) = E - N + 2P
- [ ] Document test paths in Set Notation
- [ ] Export diagrams to PNG/PDF
- [ ] Create Overleaf document
- [ ] Include mathematical proofs

**Tools**:
- Draw.io / Lucidchart for diagrams
- Overleaf for LaTeX documentation

**Labels**: `documentation`, `white-box-testing`, `analysis`
```

### Issue #5: Fix Bugs and Ensure Application Runs
```markdown
**Title**: fix: Debug and fix compilation/runtime errors

**Description**:
Identify and fix all bugs, compilation errors, and runtime issues to ensure the application runs smoothly.

**Tasks**:
- [ ] Fix compilation errors
- [ ] Fix database connection issues
- [ ] Test all CRUD operations
- [ ] Verify TF-IDF calculation works
- [ ] Test pagination functionality
- [ ] Test auto-save trigger
- [ ] Fix any logical errors found during testing

**Testing Checklist**:
- [ ] Application starts without errors
- [ ] Can create new document
- [ ] Can import existing file
- [ ] Can save and update document
- [ ] Pagination works correctly
- [ ] Auto-save triggers after 500 words

**Labels**: `bug`, `critical`, `runtime`
```

---

## Step 3: Ticket Passing Protocol

### Example Workflow

1. **Create Issue**: 
   ```
   Issue #6: "feat: Implement Auto-Save JUnit test for >500 words"
   ```

2. **Assign to yourself**:
   - Click "Assignees" → Select yourself
   - Move to "In Progress" column in Project board

3. **Create Feature Branch**:
   ```bash
   git checkout -b feature/autosave-test-6
   ```

4. **Write code and commit**:
   ```bash
   git add Testing/presentation/AutoSaveTest.java
   git commit -m "test: Add Auto-Save JUnit tests for word count threshold #6"
   git push origin feature/autosave-test-6
   ```

5. **Create Pull Request**:
   - Go to GitHub → Pull Requests → New Pull Request
   - Base: `master`, Compare: `feature/autosave-test-6`
   - Title: "test: Add Auto-Save JUnit tests #6"
   - Description: Reference the issue: "Closes #6"
   - Request review (if group project)

6. **Review & Merge**:
   - Review code changes
   - Run tests locally
   - If approved, merge to master
   - Delete feature branch
   - Move issue to "Done"

---

## Step 4: Commit Message Format

Follow **Conventional Commits** format:

```
<type>(<scope>): <subject>

[optional body]

[optional footer]
```

### Types:
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation
- `test`: Adding tests
- `refactor`: Code refactoring
- `chore`: Maintenance tasks
- `style`: Code formatting

### Examples:
```bash
git commit -m "feat(business): Add FacadeBO test suite #1"
git commit -m "fix(data): Fix database connection timeout issue #5"
git commit -m "test(presentation): Add Auto-Save tests for 500+ words #2"
git commit -m "docs: Add CFG diagrams to Overleaf document #4"
```

**Always reference the issue number with `#<number>`**

---

## Step 5: Pull Request Template

When creating a Pull Request, use this template:

```markdown
## Description
Brief description of what this PR does.

## Related Issue
Closes #<issue-number>

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Tests
- [ ] Documentation

## Testing Done
- [ ] Unit tests pass
- [ ] Manual testing completed
- [ ] No new warnings or errors

## Checklist
- [ ] Code follows project conventions
- [ ] Tests added/updated
- [ ] Documentation updated
- [ ] Commit messages reference issue number
```

---

## Step 6: Branch Protection Rules (Optional)

For teams, set up branch protection:

1. Go to **Settings** → **Branches**
2. Click **Add rule** for `master` branch
3. Enable:
   - ✅ Require pull request reviews before merging
   - ✅ Require status checks to pass
   - ✅ Include administrators

---

## Step 7: Labels for Organization

Create these labels in your repository:

- `bug` (red) - Something isn't working
- `enhancement` (blue) - New feature or request
- `testing` (purple) - Test-related issues
- `documentation` (green) - Documentation improvements
- `business-layer` (yellow)
- `data-layer` (orange)
- `presentation-layer` (pink)
- `critical` (dark red) - High priority
- `good-first-issue` (light green) - Easy to start with

---

## Step 8: Milestones

Create milestones to track progress:

1. **Milestone 1**: Testing Suite Setup
2. **Milestone 2**: White-Box Analysis
3. **Milestone 3**: Test Implementation Complete
4. **Milestone 4**: Bug Fixes & Application Running
5. **Milestone 5**: Documentation Complete

---

## Verification Checklist

✅ GitHub Project board created with 5 columns  
✅ At least 5 issues created  
✅ Issues assigned and labeled properly  
✅ Commit messages reference issue numbers  
✅ Pull Request workflow documented  
✅ Branch naming convention established  

---

**Next Steps**: 
1. Go to GitHub and create the Project board
2. Create all 5 issues listed above
3. Move issues to appropriate columns
4. Start working on highest priority issue

**Remember**: Every code change should:
1. Start with an Issue
2. Be done in a feature branch
3. Have commits referencing the issue
4. Go through Pull Request review
5. Be merged only after approval

This ensures full traceability and professional workflow! 🚀
