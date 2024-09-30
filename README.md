# Jenkins CI/CD Setup for Unity WebGL Builds

## Prerequisites

This guide provides a step-by-step process to install Jenkins, Java 18, Node.js 18, and configure a GitHub token in Jenkins credentials on an Ubuntu server.

# Installation and Setup
Run the following commands to install Java 18, Jenkins, Node.js 18, and configure Jenkins with a GitHub token.

```bash
#Make sure your Ubuntu system is updated:
sudo apt update && sudo apt upgrade -y

# Install Java 17
sudo apt install fontconfig openjdk-17-jre

# Verify Java installation
java -version

# Add Jenkins repository and install Jenkins
sudo wget -O /usr/share/keyrings/jenkins-keyring.asc \
  https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key
echo "deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc]" \
  https://pkg.jenkins.io/debian-stable binary/ | sudo tee \
  /etc/apt/sources.list.d/jenkins.list > /dev/null
sudo apt-get update
sudo apt-get install jenkins
# Install Jenkins
sudo apt update
sudo apt install jenkins -y

# Start and enable Jenkins
sudo systemctl start jenkins
sudo systemctl enable jenkins

# Check Jenkins status
sudo systemctl status jenkins

# Install Node.js 18
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt install -y nodejs

# Verify Node.js installation
node -v

# Add GitHub token to Jenkins credentials
# 1. Open Jenkins in the browser at: http://<your-server-ip>:8080
# 2. Go to Manage Jenkins -> Manage Credentials
# 3. Add a new 'Secret Text' credential for your GitHub token

# Use the following in your Jenkins pipeline to set the GitHub token
# Reference the token using 'github_token' in your Jenkins pipeline
withCredentials([string(credentialsId: 'github_token', variable: 'GITHUB_TOKEN')]) {
    sh 'git remote set-url origin https://$GITHUB_TOKEN@github.com/username/repo.git'
}
