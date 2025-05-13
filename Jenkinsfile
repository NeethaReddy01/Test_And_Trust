pipeline {
    agent any
    
    tools {
        maven 'Maven'
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
                    sh 'mvn clean package'
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
                        sh 'mvn sonar:sonar'
                    }
                }
            }
        }
        
        stage('Build and Push Docker Images') {
            steps {
                withCredentials([string(credentialsId: 'docker-hub-credentials', variable: 'DOCKER_HUB_CREDENTIALS')]) {
                    sh 'docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD}'
                    
                    // Build and push backend image
                    dir('backend') {
                        sh 'docker build -t ${DOCKER_USERNAME}/test-and-trust-backend:${BUILD_NUMBER} .'
                        sh 'docker tag ${DOCKER_USERNAME}/test-and-trust-backend:${BUILD_NUMBER} ${DOCKER_USERNAME}/test-and-trust-backend:latest'
                        sh 'docker push ${DOCKER_USERNAME}/test-and-trust-backend:${BUILD_NUMBER}'
                        sh 'docker push ${DOCKER_USERNAME}/test-and-trust-backend:latest'
                    }
                }
            }
        }
        
        stage('Deploy to Development') {
            steps {
                sh 'docker-compose down || true'
                sh 'docker-compose up -d'
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