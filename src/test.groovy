def call(String imageName, String awsAccountId, String awsRegion) {
    pipeline {
        agent any
      
        stages {
            stage('Authenticate with AWS ECR') {
                steps {
                    script {
                        withCredentials([[
                            $class: 'AmazonWebServicesCredentialsBinding',
                            accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                            credentialsId: 'vinodh',
                            secretKeyVariable: 'AWS_SECRET_ACCESS_KEY'
                        ]]) {
                            bat 'aws ecr get-login-password --region us-east-2 | docker login --username AWS --password-stdin 810678507647.dkr.ecr.us-east-2.amazonaws.com'
                        }
                    }
                }
            }
            stage('mvn ') {
                steps {
                    script {
                        bat 'mvn clean package'
                    }
                }
            }
            stage('Build Docker Image') {
                steps {
                    script {
                        docker.build('${imageName}')
                    }
                }
            }
            stage('Push Docker Image to AWS ECR') {
                steps {
                    script {
                        docker.withRegistry('https://${awsAccountId}.dkr.ecr.${awsRegion}.amazonaws.com/${imageName}') {
                            docker.image('${imageName}').push('latest')
                        }
                    }
                }
            }
            // stage('Pull and Run docker image') {
            //     steps {
            //         script {
            //             bat 'docker pull ${awsAccountId}.dkr.ecr.${awsRegion}.amazonaws.com/${imageName}:latest'
            //             bat 'docker run -d -p 4200:4200 ${awsAccountId}.dkr.ecr.${awsRegion}.amazonaws.com/${imageName}:latest'
            //             bat 'docker ps'
            //             bat 'docker ps -a'
            //         }
            //     }
            // }
        }
    }
}