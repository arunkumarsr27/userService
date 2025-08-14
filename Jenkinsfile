pipeline {
  agent any

  tools {
    jdk 'JDK 21'
    maven 'Maven 3.9.9'
  }

  options {
    timestamps()
    ansiColor('xterm')
  }

  triggers {
    // enable later once webhook is set
    // pollSCM('H/2 * * * *')
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build') {
      steps {
        sh 'mvn -B -q clean package -DskipTests'
      }
    }

    stage('Test') {
      steps {
        sh 'mvn -B -q test'
      }
      post {
        always {
          junit '**/target/surefire-reports/*.xml'
        }
      }
    }

    stage('Archive Artifact') {
      when { expression { fileExists('target/*.jar') } }
      steps {
        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
      }
    }
  }

  post {
    success { echo '✅ CI pipeline passed' }
    failure { echo '❌ CI pipeline failed' }
  }
}
