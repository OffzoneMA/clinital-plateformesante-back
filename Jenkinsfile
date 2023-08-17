import jenkins.model.*

def jenkins = Jenkins.getInstance()

node {
    stage('Build') {
        sh 'docker-compose -f /var/lib/jenkins/Clinital_pipeline/docker-compose.yml up --build'
    }
}
