pipeline {
    agent any
    
    tools {
        maven 'maven'
        jdk 'JDK17'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build and Test') {
            steps {
                dir('backend') {
                    bat 'mvn clean package'
                }
            }
            post {
                success {
                    archiveArtifacts artifacts: 'backend/target/*.jar', fingerprint: true
                    junit 'backend/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    dir('backend') {
                        bat 'mvn sonar:sonar'
                    }
                }
            }
        }
        
        stage('Build and Pubat Docker Images') {
            steps {
                withCredentials([string(credentialsId: 'docker-hub-credentials', variable: 'DOCKER_HUB_CREDENTIALS')]) {
                    bat 'docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD}'
                    
                    // Build and pubat backend image
                    dir('backend') {
                        bat 'docker build -t ${DOCKER_USERNAME}/test-and-trust-backend:${BUILD_NUMBER} .'
                        bat 'docker tag ${DOCKER_USERNAME}/test-and-trust-backend:${BUILD_NUMBER} ${DOCKER_USERNAME}/test-and-trust-backend:latest'
                        bat 'docker pubat ${DOCKER_USERNAME}/test-and-trust-backend:${BUILD_NUMBER}'
                        bat 'docker pubat ${DOCKER_USERNAME}/test-and-trust-backend:latest'
                    }
                }
            }
        }
        
        stage('Deploy to Development') {
            steps {
                bat 'docker-compose down || true'
                bat 'docker-compose up -d'
            }
        }
    }
    
    post {
        always {
            cleanWs()
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}