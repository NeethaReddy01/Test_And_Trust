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
                dir('Backend') {
                    bat 'mvn clean package'
                }
            }
            post {
                success {
                    archiveArtifacts artifacts: 'Backend/target/*.jar', fingerprint: true
                    junit 'Backend/target/surefire-reports/*.xml'
                }
            }
        }
        
        
        
        stage('Build and Pubat Docker Images') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
    bat 'docker login -u %DOCKER_USERNAME% -p %DOCKER_PASSWORD%'
                    
                    dir('Backend') {
                        bat 'docker build -t %DOCKER_USERNAME%/test-and-trust-backend:%BUILD_NUMBER% .'
                        bat 'docker tag %DOCKER_USERNAME%/test-and-trust-backend:%BUILD_NUMBER% %DOCKER_USERNAME%/test-and-trust-backend:latest'
                        bat 'docker push %DOCKER_USERNAME%/test-and-trust-backend:%BUILD_NUMBER%'
                        bat 'docker push %DOCKER_USERNAME%/test-and-trust-backend:latest'
                    }
                }
            }
        }
        
        stage('Deploy to Development') {
            steps {
                bat 'docker-compose down || exit 0'
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
