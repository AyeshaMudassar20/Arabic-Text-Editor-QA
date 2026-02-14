# Control Flow Graph (CFG) Diagrams

This file contains comprehensive Control Flow Graphs for White-Box testing analysis.

## 📊 Diagrams Included

### 1. SearchWord CFG (Page 1)
- **Feature**: Search and Replace Word functionality
- **Nodes**: 11
- **Edges**: 14
- **Cyclomatic Complexity**: V(G) = 5
- **Independent Paths**: 5
- **Test Coverage**: 100% ✅

**Key Decision Points**:
- Keyword length validation (≥3 characters)
- Prefix checking
- StartsWith vs Contains matching

### 2. Pagination CFG (Page 2)
- **Feature**: Content pagination (100-character pages)
- **Nodes**: 10
- **Edges**: 12
- **Cyclomatic Complexity**: V(G) = 4
- **Independent Paths**: 4
- **Test Coverage**: 100% ✅

**Key Decision Points**:
- Empty content check
- Loop through page numbers
- Substring extraction logic

### 3. Auto-Save CFG (Page 3)
- **Feature**: Automatic file saving when word count > 500
- **Nodes**: 7
- **Edges**: 8
- **Cyclomatic Complexity**: V(G) = 3
- **Independent Paths**: 3
- **Test Coverage**: 100% ✅

**Key Decision Points**:
- Thread running status
- Word count threshold (500 words)
- Background thread loop (every 5 seconds)

---

## 🎨 How to Open/Edit

### Option 1: Draw.io Desktop App
1. Download from: https://github.com/jgraph/drawio-desktop/releases
2. Open `CFG_Diagrams.drawio`
3. Select page tab at bottom (SearchWord, Pagination, AutoSave)

### Option 2: Online (app.diagrams.net)
1. Go to: https://app.diagrams.net/
2. Choose "Open Existing Diagram"
3. Upload `CFG_Diagrams.drawio`

### Option 3: VS Code Extension
1. Install: "Draw.io Integration" extension
2. Open `CFG_Diagrams.drawio` in VS Code
3. Edit directly in editor

---

## 📤 Export to Images (for Overleaf)

### Export as PNG/PDF:
1. Open diagram in Draw.io
2. File → Export as → PNG (or PDF)
3. Settings:
   - ✅ Transparent Background: OFF
   - ✅ Include a copy of my diagram: ON
   - ✅ Border Width: 10
   - ✅ Zoom: 100%
4. Save as:
   - `SearchWord_CFG.png`
   - `Pagination_CFG.png`
   - `AutoSave_CFG.png`

### For High-Quality LaTeX:
Export as **PDF** with:
- Crop: ON
- Transparent: OFF
- Quality: 100%

---

## 📐 Cyclomatic Complexity Calculations

### Formula
```
V(G) = E - N + 2P
```
Where:
- **E** = Number of edges
- **N** = Number of nodes
- **P** = Number of connected components (usually 1)

### Calculations

#### SearchWord
```
V(G) = 14 - 11 + 2(1)
V(G) = 14 - 11 + 2
V(G) = 5
```

#### Pagination
```
V(G) = 12 - 10 + 2(1)
V(G) = 12 - 10 + 2
V(G) = 4
```

#### Auto-Save
```
V(G) = 8 - 7 + 2(1)
V(G) = 8 - 7 + 2
V(G) = 3
```

### Total Complexity
```
Total V(G) = 5 + 4 + 3 = 12
```

---

## 🧪 Test Path Coverage

### SearchWord - 5 Independent Paths

| Path | Description | Test Case |
|------|-------------|-----------|
| p1 | Keyword too short → Exception | `testSearchKeyword_TooShort()` |
| p2 | No documents found | `testSearchKeyword_EmptyList()` |
| p3 | Prefix match found | `testSearchKeyword_WithPrefix()` |
| p4 | Contains match found | `testSearchKeyword_NoPrefix()` |
| p5 | No match in any document | `testSearchKeyword_NotFound()` |

### Pagination - 4 Independent Paths

| Path | Description | Test Case |
|------|-------------|-----------|
| p1 | Empty content → return empty | `testPaginate_EmptyContent()` |
| p2 | Single page (≤100 chars) | `testPaginate_LessThanPageSize()` |
| p3 | Multiple complete pages | `testPaginate_MultiplePages()` |
| p4 | Partial last page | `testPaginate_ExactlyPageSize()` |

### Auto-Save - 3 Independent Paths

| Path | Description | Test Case |
|------|-------------|-----------|
| p1 | Thread stopped | `testAutoSave_ThreadStopped()` |
| p2 | Word count > 500 → Save | `testAutoSave_TriggersAt500Words()` |
| p3 | Word count ≤ 500 → No save | `testAutoSave_NoTriggerBelow500()` |

---

## 🎓 For Overleaf Documentation

### LaTeX Code to Include CFGs

```latex
\subsection{Control Flow Graphs}

\subsubsection{SearchWord Feature}
\begin{figure}[h]
\centering
\includegraphics[width=0.9\textwidth]{SearchWord_CFG.png}
\caption{Control Flow Graph for Search \& Replace Word functionality. 
Cyclomatic Complexity V(G) = 5, indicating 5 independent test paths.}
\label{fig:cfg-searchword}
\end{figure}

The SearchWord feature has a cyclomatic complexity of:
$$V(G) = E - N + 2P = 14 - 11 + 2(1) = 5$$

\subsubsection{Pagination Feature}
\begin{figure}[h]
\centering
\includegraphics[width=0.9\textwidth]{Pagination_CFG.png}
\caption{Control Flow Graph for Pagination logic (100-character pages). 
Cyclomatic Complexity V(G) = 4.}
\label{fig:cfg-pagination}
\end{figure}

$$V(G) = 12 - 10 + 2(1) = 4$$

\subsubsection{Auto-Save Feature}
\begin{figure}[h]
\centering
\includegraphics[width=0.9\textwidth]{AutoSave_CFG.png}
\caption{Control Flow Graph for Auto-Save functionality (500-word threshold). 
Cyclomatic Complexity V(G) = 3.}
\label{fig:cfg-autosave}
\end{figure}

$$V(G) = 8 - 7 + 2(1) = 3$$
```

---

## 📊 Diagram Features

### Color Coding
- 🟢 **Green** (Start/End nodes): Entry and exit points
- 🔷 **Blue** (Decision nodes): Conditional branches (if, while, for)
- 🟡 **Yellow** (Process nodes): Standard operations
- 🔴 **Red** (Error nodes): Exception/error handling

### Edge Labels
- **YES/NO**: Decision outcomes
- **LOOP**: Back edges in iteration
- **MORE/DONE**: Loop continuation

### Legend Information
Each diagram includes:
- Node and edge count
- Cyclomatic complexity formula
- Calculated V(G) value
- Test path count
- Coverage percentage

---

## 🔍 Quality Metrics

| Metric | SearchWord | Pagination | Auto-Save | Average |
|--------|------------|------------|-----------|---------|
| Nodes | 11 | 10 | 7 | 9.3 |
| Edges | 14 | 12 | 8 | 11.3 |
| V(G) | 5 | 4 | 3 | 4 |
| Test Paths | 5 | 4 | 3 | 4 |
| Coverage | 100% | 100% | 100% | 100% |

**Complexity Rating**: LOW (V(G) < 10 for all features) ✅

---

## 📝 Notes

- All diagrams created using Draw.io
- Compatible with app.diagrams.net and Draw.io Desktop
- Editable XML format for version control
- Ready for export to PNG/PDF for documentation
- Meets White-Box testing requirements for Software Testing assignment

**Created**: February 14, 2026  
**Author**: AyeshaMudassar20 (f228761@cfd.nu.edu.pk)  
**Repository**: https://github.com/AyeshaMudassar20/Arabic-Text-Editor-QA
