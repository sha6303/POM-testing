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

        

        stage('Delete-deploy from EKS') {
            steps {
                // Assuming you have configured kubectl and authenticated to your EKS cluster
                // Use kubectl to apply your Kubernetes manifests or Helm charts
                sh "aws eks --region us-east-1 update-kubeconfig --name SBX"
                sh "kubectl delete -f  deployment.yaml -n example"
                sh "kubectl delete -f  service.yaml -n example"
            }
        }
    }
}
