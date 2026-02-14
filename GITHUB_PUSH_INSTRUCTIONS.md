# GitHub Push Instructions

## After creating your GitHub repository, run these commands:

# Add GitHub as remote
git remote add origin https://github.com/AyeshaMudassar20/Arabic-Text-Editor-QA.git

# Push to GitHub
git push -u origin master

# Verify
git remote -v

## Troubleshooting

If you encounter authentication issues:
1. GitHub now requires Personal Access Token instead of password
2. Create token: GitHub Settings → Developer Settings → Personal Access Tokens → Generate New Token
3. Use token as password when pushing

## Alternative: Push using SSH

# Generate SSH key (if you don't have one)
ssh-keygen -t rsa -b 4096 -C "f228761@cfd.nu.edu.pk"

# Add to GitHub: Settings → SSH and GPG keys → New SSH key

# Change remote to SSH
git remote set-url origin git@github.com:AyeshaMudassar20/Arabic-Text-Editor-QA.git

# Push
git push -u origin master
