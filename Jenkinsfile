pipeline{
    agent any 
    stages{
        stage('Git Clone'){
            steps{
                script{
                    git branch:"development", url: 'https://github.com/Azim-js/maven-job.git'
                }
            }
        }
        stage('MAVEN BUILD'){
            agent{
                docker {
                    image 'maven:3-jdk-11'
                    reuseNode true
                }
            }
            steps{
                script{
                    sh 'ls -l'
                    sh 'mvn clean install -DskipTests'
                }
            }
        }
        stage('MAVEN-TEST'){
            steps{
                script{
		    echo "using a Docker mvn container to run the mvn compiler"	
                    sh 'mvn test'
                    catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                    sh "exit 1"
                }
                }
            }
        }
        stage('JAVA-Deploy'){
            steps{
                script{
                    sh 'java -jar /var/jenkins_home/workspace/maven-job/target/*.jar'
                    catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                    sh "exit 1"
                }
                }
            }
        }
        stage("Delete Docker images from Jenkins server"){
            steps {
			sh "docker image prune -f -a"
                  }
		
		
		
		} 

	}		
 
    
    post {
        always {
            cleanWs()
            echo "Deleted Workspace..."
        }
    }
}
