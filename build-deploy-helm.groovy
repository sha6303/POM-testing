pipeline {
    agent {
        label 'ubuntu'
    }

    stages {
        stage('Git Clone') {
            steps {
                // Get some code from a GitHub repository
                git branch: 'main', url: 'https://github.com/Azim-js/maven-job.git'
            }
        }

        stage('Build') {
            steps {
                // Run Maven to build the jar
                sh "mvn clean install"
            }
            post {
                success {
                    // If Maven was able to run the tests, even if some of the test
                    // failed, record the test results and archive the jar file.
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                }
            }
        }

        stage('Docker Build and Push') {
            steps {
                // Assuming Dockerfile is in the root directory of the project
                script {
                    sh "docker build -t public.ecr.aws/z3e7z4e4/azim:latest ."
                    // Pushing the built Docker image to a Docker registry
                    sh "docker push public.ecr.aws/z3e7z4e4/azim:latest"
                }
            }
        }

        stage('Deploy to EKS') {
            steps {
                // Assuming you have configured kubectl and authenticated to your EKS cluster
                // Use kubectl to apply your Kubernetes manifests or Helm charts
                sh "aws eks --region us-east-1 update-kubeconfig --name sbx-ingress"
                sh "helm install hello-world ./my-app-chart --namespace example-ingress"
            }
        }
    }
}
