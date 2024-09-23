pipeline {
    agent any

    environment {
        UNITY_PATH = "/path/to/Unity/Editor/Unity" // or Unity Hub path or Docker path
        BUILD_PATH = "Build/WebGL"
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout source code from Git
                git url: 'https://github.com/TypeScript-Build.git', branch: 'master'
            }
        }

        stage('Unity WebGL Build') {
            steps {
                // Unity command line build for WebGL
                sh """
                ${UNITY_PATH} -batchmode -quit -nographics -logFile -projectPath $(pwd) \
                -buildTarget WebGL -executeMethod BuildScript.PerformBuild -output ${BUILD_PATH}
                """
            }
        }

        stage('Archive WebGL Build') {
            steps {
                // Archive the WebGL build artifacts
                archiveArtifacts artifacts: "${BUILD_PATH}/**", allowEmptyArchive: false
            }
        }
    }

    post {
        always {
            // Clean up workspace after build
            cleanWs()
        }
    }
}
