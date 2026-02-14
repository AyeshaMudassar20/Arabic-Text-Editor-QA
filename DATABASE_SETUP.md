# MariaDB Setup Guide for Arabic Text Editor

## Quick Start

### 1. Install MariaDB

**Windows:**

```powershell
# Using Chocolatey
choco install mariadb

# Or download installer from:
# https://mariadb.org/download/?t=mariadb&p=mariadb&r=10.11.6&os=windows&cpu=x86_64&pkg=msi
```

**Linux (Ubuntu/Debian):**

```bash
sudo apt update
sudo apt install mariadb-server
sudo mysql_secure_installation
```

### 2. Start MariaDB Service

**Windows:**

```powershell
# Start service
net start MySQL

# Or via Services applet (services.msc)
```

**Linux:**

```bash
sudo systemctl start mariadb
sudo systemctl enable mariadb  # Auto-start on boot
```

### 3. Create Database

```powershell
# Connect to MariaDB
mysql -u root -p

# Execute in MySQL prompt:
```

```sql
CREATE DATABASE arabic_editor_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE arabic_editor_db;

-- Verify
SHOW DATABASES;
```

### 4. Import Schema

```powershell
# From project root directory
mysql -u root -p arabic_editor_db < schema.sql

# Verify tables created
mysql -u root -p arabic_editor_db -e "SHOW TABLES;"
```

### 5. Configure Application

Copy the example config file:

```powershell
Copy-Item config.properties.example config.properties
```

Edit `config.properties`:

```properties
db.host=localhost
db.port=3306
db.name=arabic_editor_db
db.user=root
db.password=YOUR_PASSWORD_HERE
```

### 6. Test Connection

Run the DatabaseConnection test:

```powershell
mvn test -Dtest=DatabaseConnectionTest
```

Expected output:

```
Tests run: 22, Failures: 0, Errors: 0, Skipped: 0
```

## Database Schema Overview

### Tables Created

| Table                 | Purpose                        | Key Features            |
| --------------------- | ------------------------------ | ----------------------- |
| `files`               | Document metadata              | fileName, fileHash      |
| `pages`               | Paginated content (100 chars)  | pageNumber, pageContent |
| `transliteratedpages` | Arabic → Latin transliteration | transliteratedText      |
| `pos`                 | Part-of-Speech tagging         | word, pos               |
| `lemmatization`       | Word lemmas                    | word, lemma             |
| `rootextraction`      | Arabic root extraction         | word, root              |
| `stemmation`          | Word stems                     | word, stem              |
| `wordsegementation`   | Word segmentation              | word, segment           |
| `tfidf`               | TF-IDF scores                  | fileId, tfidfScore      |
| `pmi`                 | Pointwise Mutual Information   | word, pmiScore          |
| `pkl`                 | Perplexity scores              | word, pklScore          |

### Character Set

All tables use **UTF8MB4** encoding to support:

- Arabic characters (العربية)
- Emojis and special symbols
- Full Unicode range

## Troubleshooting

### Cannot Connect to Database

**Check if MariaDB is running:**

```powershell
# Windows
Get-Service MySQL

# Linux
sudo systemctl status mariadb
```

**Check credentials:**

```powershell
mysql -u root -p
# Enter password when prompted
```

### Access Denied Error

**Reset root password:**

```sql
ALTER USER 'root'@'localhost' IDENTIFIED BY 'new_password';
FLUSH PRIVILEGES;
```

### Character Encoding Issues

**Verify database charset:**

```sql
SELECT default_character_set_name, default_collation_name
FROM information_schema.SCHEMATA
WHERE schema_name = 'arabic_editor_db';
```

Expected: `utf8mb4` and `utf8mb4_unicode_ci`

### Port Already in Use

**Change port in config.properties:**

```properties
db.port=3307  # Use different port
```

**Or stop conflicting service:**

```powershell
# Windows
net stop MySQL
net start MySQL
```

## Sample Queries

### View All Files

```sql
SELECT * FROM files;
```

### View Pages for a File

```sql
SELECT p.pageNumber, p.pageContent
FROM pages p
JOIN files f ON p.fileId = f.id
WHERE f.fileName = 'sample.txt'
ORDER BY p.pageNumber;
```

### Get Lemmatization Results

```sql
SELECT word, lemma
FROM lemmatization
LIMIT 10;
```

### Calculate Storage Usage

```sql
SELECT
    table_name,
    ROUND((data_length + index_length) / 1024 / 1024, 2) AS 'Size (MB)'
FROM information_schema.TABLES
WHERE table_schema = 'arabic_editor_db'
ORDER BY (data_length + index_length) DESC;
```

## Performance Tips

### Add Indexes for Frequent Queries

```sql
-- If searching by fileName frequently
CREATE INDEX idx_fileName_hash ON files(fileName, fileHash);

-- If filtering by word in lemmatization
CREATE INDEX idx_lemma_word ON lemmatization(word, lemma);
```

### Enable Query Caching

Edit `my.ini` (Windows) or `/etc/mysql/my.cnf` (Linux):

```ini
[mysqld]
query_cache_size = 32M
query_cache_type = 1
```

Restart MariaDB after changes.

## Backup and Restore

### Backup Database

```powershell
mysqldump -u root -p arabic_editor_db > backup.sql
```

### Restore Database

```powershell
mysql -u root -p arabic_editor_db < backup.sql
```

## Next Steps

1. ✅ MariaDB installed and running
2. ✅ Database created with schema
3. ✅ Config file updated
4. ⏳ Run application: `mvn clean javafx:run`
5. ⏳ Test file operations (create, edit, save)
6. ⏳ Verify NLP features (lemmatization, POS tagging)

## Support

**Official Documentation:**

- MariaDB: https://mariadb.com/kb/en/
- JDBC Connector: https://mariadb.com/kb/en/about-mariadb-connector-j/

**Assignment Questions:**
Contact: f228761@cfd.nu.edu.pk
