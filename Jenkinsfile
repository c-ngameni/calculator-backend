pipeline {
    agent any

    triggers {
        pollSCM('*/30 * * * *')
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/c-ngameni/calculator-backend.git',
                branch: 'main'
            }
        }

        stage('Build') {
            steps {
                bat "mvn clean package"
            }

            post {
                success {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
    }
}
