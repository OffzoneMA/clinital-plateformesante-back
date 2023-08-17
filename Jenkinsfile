import jenkins.model.*
jenkins = Jenkins.instance

node{

    stage(‘Build’) {

      sh ‘docker-compose -f /var/lib/jenkins/Clinital_pipeline/docker-compose.yml up --build’ 
    }

}
