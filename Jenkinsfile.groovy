pipeline {
    agent any

    environment {
        Token = credentials('GITHUB_TOKEN')  // Fetch GitHub token from Jenkins credentials
    }

    stages {
        stage('Clone Repository') {
            steps {
                git url: 'https://github.com/Prathm0025/TypeScript-Build.git', branch: 'master'
            }
        }

        stage('Setup Environment') {
            steps {
                script {
                    // Install dependencies using npm on Windows
                    bat 'npm install'
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    // Build the project and handle errors
                    catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                        bat 'npm run build --verbose'
                    }
                }
            }
        }

        stage('Run Compiled Output') {
            when {
                expression { currentBuild.currentResult == 'SUCCESS' }
            }
            steps {
                script {
                    // Run the compiled project using node on Windows
                    bat 'node dist\\index.js' // Update if necessary, based on your output directory
                }
            }
        }

        stage('Push Artifact') {
            when {
                expression { currentBuild.currentResult == 'SUCCESS' }
            }
            steps {
                script {
                    // Configure Git with user details on Windows
                    bat 'git config user.email "you@example.com"'
                    bat 'git config user.name "Your Name"'
                    bat 'git remote set-url origin https://${Token}@github.com/Prathm0025/TypeScript-Build.git'
                    bat 'git add dist\\*' // Add your build artifacts from the correct folder
                    bat 'git commit -m "Add new build artifacts"'
                    bat 'git push origin master'
                }
            }
        }
    }

    triggers {
        pollSCM('H/5 * * * *') // Poll SCM every 5 minutes
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '2')) // Keep the last 2 builds
    }
}
