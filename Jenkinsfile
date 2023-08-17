pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                // Add your build steps here
            }
        }

        stage('Run Docker Compose') {
            steps {
                script {
                    // Ensure Docker is available in the environment
                    def docker = tool name: 'Docker', type: 'org.jenkinsci.plugins.docker.commons.tools.DockerTool'
                    
                    // Set up Docker Compose command
                    def dockerComposeCmd = "${docker}/docker-compose"
                    def dockerComposeFile = 'docker-compose.yml'
                    
                    // Run docker-compose up
                    sh "${dockerComposeCmd} -f ${dockerComposeFile} up -d"
                }
            }
        }

        stage('Test') {
            steps {
                // Add your test steps here
            }
        }
        
        stage('Cleanup') {
            steps {
                script {
                    // Run docker-compose down to clean up containers
                    sh "${dockerComposeCmd} -f ${dockerComposeFile} down"
                }
            }
        }
    }

    post {
        always {
            // Clean up in case of pipeline failure
            sh "${dockerComposeCmd} -f ${dockerComposeFile} down"
        }
    }
}
