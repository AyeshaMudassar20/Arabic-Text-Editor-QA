Arabic Text Editor - Professional QA & Testing Suite
======================================================

Project Overview
----------------

This repository contains a Quality Assurance and Testing Suite for an Arabic text editor application: a white-box testing project implementing Control-Flow-Graph analysis, JUnit test suites across a 3-layer architecture, and a professional GitHub workflow.

Base Application: Real Editor - a powerful Arabic text editor designed for text processing and storage. Built using Java 1.8, it provides robust features to manage and analyze Arabic text, integrating with MariaDB for file storage and supporting a wide range of text processing functionalities.

QA Project Goals
-----------------

- Reverse-engineer business logic using white-box testing techniques
- Implement a professional GitHub workflow with issue tracking
- Write comprehensive test cases covering all 3 architectural layers
- Fix bugs and ensure smooth application execution
- Deliver professional documentation with CFG and Cyclomatic Complexity analysis

Features
--------

File Management
- Save files to MariaDB
- Create, update, delete files
- Import files from your PC

Text Processing Capabilities
- Term Frequency-Inverse Document Frequency (TF-IDF)
- Pointwise Mutual Information (PMI)
- PKL computation
- Part-of-Speech Tagging (POS Tagging)
- Stemming
- Lemmatization
- Root extraction

Architecture
------------

The application implements a strict 3-layer architecture:

- Presentation Layer (`pl/`) - user interface components
- Business Logic Layer (`bll/`) - core business logic, Facade Pattern, Command Pattern
- Data Access Layer (`dal/`) - database operations, persistence, algorithms

Design Patterns Implemented
- Facade Pattern: `TextEditor` class
- Command Pattern: `ImportCommand`, `SaveCommand`, `ExportCommand`
- Singleton Pattern: `DatabaseConnection`
- Factory Pattern: DAO Factory implementations

Project Structure
------------------

    Text-Editor/
    ├── src/
    │   ├── bll/            # Business Logic Layer
    │   │   ├── FacadeBO.java
    │   │   ├── EditorBO.java
    │   │   └── SearchWord.java
    │   ├── dal/            # Data Access Layer
    │   │   ├── DatabaseConnection.java
    │   │   ├── EditorDBDAO.java
    │   │   ├── TFIDFCalculator.java
    │   │   └── HashCalculator.java
    │   ├── pl/             # Presentation Layer
    │   │   ├── EditorPO.java
    │   │   └── FileImporter.java
    │   └── dto/            # Data Transfer Objects
    ├── Testing/            # Test Suite (Root Level)
    │   ├── presentation/   # UI Tests
    │   ├── business/       # Business Logic Tests
    │   └── data/           # Data Layer Tests
    ├── bin/                # Compiled classes
    ├── resource/           # Application resources
    └── logs/               # Application logs

Testing Suite
-------------

White-Box Analysis (Phase A)

Selected features for Control Flow Graph analysis:
- Search & Replace Word - find/replace with success flags
- Pagination Logic - content splitting based on word limits
- Auto-Save Trigger - word count threshold (>500 words)

Deliverables:
- Control Flow Graphs (CFG) with labeled nodes and edges
- Cyclomatic Complexity: V(G) = E - N + 2P
- Test Paths in Set Notation: P = {p1, p2, ..., pn}

JUnit Test Coverage (Phase B)

Business Layer Tests:
- Command Pattern: execute() methods
- TF-IDF Algorithm validation (±0.01 tolerance)
- Positive and negative test paths

Data Layer Tests:
- Hash Integrity (MD5/SHA1)
- Singleton Pattern verification
- Database operations with mocking

Presentation Layer Tests:
- UI component initialization
- File import workflows
- Exception handling

Documentation
-------------

For detailed instructions, usage guidelines, and a comprehensive feature report, see the [Report](Report/CFG_README.md) folder, which includes:
- Control Flow Graphs (CFGs)
- Cyclomatic Complexity calculations
- Test Path sets with mathematical notation

Getting Started
----------------

Prerequisites
- Java 1.8 or higher
- MariaDB 10.x or higher
- Maven (for dependency management and testing)
- JUnit 5.x

Database Setup

Install MariaDB and create a database:

    CREATE DATABASE text_editor_db;

Update database credentials in `config.properties`:

    db.url=jdbc:mariadb://localhost:3306/text_editor_db
    db.username=your_username
    db.password=your_password

Running the Application

    # Compile the project
    javac -d bin -sourcepath src src/**/*.java

    # Run the application
    java -cp bin Driver

Running Tests

    # Run all tests
    mvn test

    # Run specific layer tests
    mvn test -Dtest=Testing.business.*
    mvn test -Dtest=Testing.data.*
    mvn test -Dtest=Testing.presentation.*

    # Generate coverage report
    mvn test jacoco:report

Development Workflow
----------------------

GitHub Workflow
- Issue Creation: every task starts with a GitHub Issue
- Branch Strategy: feature branches created from issues
- Pull Requests: all changes go through PR review
- Code Review: reviewed before merging
- Commit Messages: reference issue numbers (e.g., `fix: Auto-save logic #14`)

Kanban Board

Using GitHub Projects with columns: Backlog, To Do, In Progress, In Review, Done.

Progress Tracking
-------------------

Test Coverage Goals
- Business Logic Layer: 0/10 test classes
- Data Access Layer: 0/8 test classes
- Presentation Layer: 0/3 test classes

White-Box Analysis
- Control Flow Graphs for 2 features
- Cyclomatic Complexity calculations
- Test Path documentation

Author
------

Ayesha Mudassar — [github.com/AyeshaMudassar20](https://github.com/AyeshaMudassar20)

License
-------

MIT License — see `LICENSE`.

Original Repository
----------------------

Base Code: [F223708/Text-Editor](https://github.com/F223708/Text-Editor)
