pipeline {
    agent any

    environment {
        Token = credentials('GITHUB_TOKEN')  // Fetch GitHub token from Jenkins credentials
    }

    triggers {
        GenericTrigger(
            genericVariables: [
                [key: 'pull_request_action', value: '$.action'],       // captures the pull request action (opened, closed, etc.)
                [key: 'pull_request_number', value: '$.pull_request.number'], // captures the pull request number
                [key: 'pull_request_merged', value: '$.pull_request.merged'], // captures whether the PR was merged (for closed PRs)
                [key: 'pull_request_title', value: '$.pull_request.title']    // captures the title of the pull request
            ],
            causeString: 'Triggered by Pull Request: $pull_request_number',
            token: 'your-webhook-token',  // Optional: If you set a secret token for the webhook
            printContributedVariables: true,
            printPostContent: true,
            regexpFilterExpression: '',
            regexpFilterText: ''
        )
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
                    // Install dependencies
                    sh 'npm install'
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    // Build the project and handle errors
                    catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                        sh 'npm run build --verbose'
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
                    // Run the compiled project
                    sh 'node dist/index.js' // Update if necessary, based on your output directory
                }
            }
        }

        stage('Push Artifact') {
            when {
                expression { currentBuild.currentResult == 'SUCCESS' }
            }
            steps {
                script {
                    // Configure Git with user details
                    sh 'git config user.email "you@example.com"'
                    sh 'git config user.name "Your Name"'
                    sh 'git remote set-url origin https://${Token}@github.com/Prathm0025/TypeScript-Build.git'
                    sh 'git add dist/*' // Add your build artifacts from the correct folder
                    sh 'git commit -m "Add new build artifacts"'
                    sh 'git push origin master'
                }
            }
        }
/*
        stage('Rollback') {
            when {
                expression { currentBuild.currentResult == 'FAILURE' }
            }
            steps {
                script {
                    // Perform rollback actions here
                    echo 'Rolling back to the previous build...'
                    sh 'git checkout HEAD^' // Chec
