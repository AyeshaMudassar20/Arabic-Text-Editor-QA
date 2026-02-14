# Arabic Text Editor - Professional QA & Testing Suite

## Project Overview

This repository contains the **Quality Assurance and Testing Suite** for the Arabic Text Editor application. This is a comprehensive Software Testing project implementing white-box testing, JUnit test suites, and professional GitHub workflow practices.

**Base Application**: Real Editor - A powerful Arabic text editor designed for text processing and storage. Built using Java 1.8, it provides users with robust features to manage and analyze Arabic text. The editor integrates seamlessly with MariaDB for file storage and supports a wide array of text processing functionalities.

**Course**: Software Testing (Software Verification & Validation)  
**Semester**: Spring 2026  
**Student**: AyeshaMudassar20

---

## 🎯 QA Project Goals

1. ✅ Reverse-engineer business logic using White-Box testing techniques
2. ✅ Implement professional GitHub workflow with issue tracking
3. ✅ Write comprehensive test cases covering all 3 architectural layers
4. ✅ Fix bugs and ensure smooth application execution
5. ✅ Deliver professional documentation with CFG, Cyclomatic Complexity analysis

---

## Features

- **File Management**
  - Save files to MariaDB.
  - Create, update, delete files.
  - Import files from your PC.

- **Text Processing Capabilities**
  - Term Frequency-Inverse Document Frequency (**TF-IDF**).
  - Pointwise Mutual Information (**PMI**).
  - PKL computation.
  - Part-of-Speech Tagging (**POS Tagging**).
  - Stemming.
  - Lemmatization.
  - Root extraction.

---

## 🏗️ Architecture

The application implements a strict **3-Layer Architecture**:

- **Presentation Layer (pl/)** - User interface components
- **Business Logic Layer (bll/)** - Core business logic, Facade Pattern, Command Pattern
- **Data Access Layer (dal/)** - Database operations, persistence, algorithms

### Design Patterns Implemented

- **Facade Pattern**: `TextEditor` class
- **Command Pattern**: `ImportCommand`, `SaveCommand`, `ExportCommand`
- **Singleton Pattern**: `DatabaseConnection`
- **Factory Pattern**: DAO Factory implementations

---

## 📁 Project Structure

```
Text-Editor/
├── src/
│   ├── bll/           # Business Logic Layer
│   │   ├── FacadeBO.java
│   │   ├── EditorBO.java
│   │   └── SearchWord.java
│   ├── dal/           # Data Access Layer
│   │   ├── DatabaseConnection.java
│   │   ├── EditorDBDAO.java
│   │   ├── TFIDFCalculator.java
│   │   └── HashCalculator.java
│   ├── pl/            # Presentation Layer
│   │   ├── EditorPO.java
│   │   └── FileImporter.java
│   └── dto/           # Data Transfer Objects
├── Testing/           # ⭐ Test Suite (Root Level)
│   ├── presentation/  # UI Tests
│   ├── business/      # Business Logic Tests
│   └── data/          # Data Layer Tests
├── bin/               # Compiled classes
├── resource/          # Application resources
└── logs/              # Application logs
```

---

## 🧪 Testing Suite

### White-Box Analysis (Phase A)

Selected features for Control Flow Graph analysis:

1. **Search & Replace Word** - Find/replace with success flags
2. **Pagination Logic** - Content splitting based on word limits
3. **Auto-Save Trigger** - Word count threshold (>500 words)

**Deliverables:**

- Control Flow Graphs (CFG) with labeled nodes and edges
- Cyclomatic Complexity: V(G) = E - N + 2P
- Test Paths in Set Notation: P = {p₁, p₂, ..., pₙ}

### JUnit Test Coverage (Phase B)

**Business Layer Tests:**

- ✅ Command Pattern: `execute()` methods
- ✅ TF-IDF Algorithm validation (±0.01 tolerance)
- ✅ Positive and negative test paths

**Data Layer Tests:**

- ✅ Hash Integrity (MD5/SHA1)
- ✅ Singleton Pattern verification
- ✅ Database operations with mocking

**Presentation Layer Tests:**

- ✅ UI component initialization
- ✅ File import workflows
- ✅ Exception handling

---

## Installation Links

Download the appropriate installer for your setup from the links below:

- **Single PC Setup**: [Download](https://drive.google.com/drive/folders/1aAInCbb5Oj6JfZfKciYHTXTgazPWUIy4?usp=sharing)
- **Server PC Setup**: [Download](https://drive.google.com/drive/folders/1w8qyK11KukpnU69mzSEO6ZzbApvQzco_?usp=drive_link)
- **Client PC Setup**: [Download](https://drive.google.com/drive/folders/16gFYIJnO1a_W3SbACUSC_Rz4xTG8jInc?usp=sharing)

## Documentation

For detailed instructions, usage guidelines, and a comprehensive feature report, please refer to the documentation provided:

- **Report**: [View Report](https://drive.google.com/drive/folders/185O5gpF0_EKI380CtnB6A0AK-Tph2Uz-?usp=sharing)

---

## 🚀 Getting Started

### Prerequisites

- Java 1.8 or higher
- MariaDB 10.x or higher
- Maven (for dependency management and testing)
- JUnit 5.x

### Database Setup

1. Install MariaDB and create a database:

```sql
CREATE DATABASE text_editor_db;
```

2. Update database credentials in `config.properties`:

```properties
db.url=jdbc:mariadb://localhost:3306/text_editor_db
db.username=your_username
db.password=your_password
```

### Running the Application

```bash
# Compile the project
javac -d bin -sourcepath src src/**/*.java

# Run the application
java -cp bin Driver
```

### Running Tests

```bash
# Run all tests
mvn test

# Run specific layer tests
mvn test -Dtest=Testing.business.*
mvn test -Dtest=Testing.data.*
mvn test -Dtest=Testing.presentation.*

# Generate coverage report
mvn test jacoco:report
```

---

## 📋 Development Workflow

### GitHub Workflow (Professional)

1. **Issue Creation**: Every task starts with a GitHub Issue
2. **Branch Strategy**: Create feature branches from issues
3. **Pull Requests**: All changes go through PR review
4. **Code Review**: Team members review before merging
5. **Commit Messages**: Reference issue numbers (e.g., `fix: Auto-save logic #14`)

### Kanban Board

Using GitHub Projects with columns:

- 📝 **Backlog** - Upcoming tasks
- 📌 **To Do** - Ready to start
- 🔄 **In Progress** - Currently working
- 👀 **In Review** - Awaiting PR review
- ✅ **Done** - Completed tasks

### Ticket Passing Protocol

Example workflow:

1. Student A creates issue: `feat: Implement JUnit test for AutoSave logic #12`
2. Student B assigns to themselves, moves to "In Progress"
3. Student B creates branch: `feature/autosave-test-12`
4. Student B implements test, commits with message: `test: Add AutoSave JUnit tests #12`
5. Student B creates Pull Request
6. Student A reviews and merges
7. Move ticket to "Done"

---

## 📊 Progress Tracking

### Test Coverage Goals

- [ ] Business Logic Layer: 0/10 test classes
- [ ] Data Access Layer: 0/8 test classes
- [ ] Presentation Layer: 0/3 test classes

### White-Box Analysis

- [ ] Control Flow Graphs for 2 features
- [ ] Cyclomatic Complexity calculations
- [ ] Test Path documentation

### Bug Fixes

- [ ] Initial compilation errors
- [ ] Database connection issues
- [ ] Logical errors in business logic

---

## 📖 Documentation

All documentation is maintained in Overleaf and includes:

- Control Flow Graphs (CFGs)
- Cyclomatic Complexity calculations
- Test Path sets with mathematical notation
- Team collaboration screenshots
- GitHub Network Graph

---

## 👥 Team

**Student**: AyeshaMudassar20  
**Email**: f228761@cfd.nu.edu.pk  
**Course**: Software Testing (Software Verification & Validation)  
**Institution**: FAST-NUCES  
**Semester**: Spring 2026

---

## 📜 License

This project is an academic assignment for educational purposes.

---

## 🔗 Original Repository

**Base Code**: [F223708/Text-Editor](https://github.com/F223708/Text-Editor)

---
