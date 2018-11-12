#!groovy

// Define job properties
properties([buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '5')), pipelineTriggers([]), disableConcurrentBuilds()])

pipeline {
	
	agent none

	stages {
		
		stage ('Checkout') {
			agent any
			steps {
				checkout scm
			}
		}

		stage ('Build & Tests') {
			agent any
			environment { JAVA_HOME = '/usr/lib/jvm/java-8-openjdk-amd64/' }
			steps {
				sh "mvn clean install"
				junit '**/TEST-*Test.xml'
			}
		}

		// Déploiement dans le repository exposé par le serveur Apache
		stage ('Deploy') {
			agent any
			when { branch 'master' }
			steps {
				sh "cp ./angular-maven-plugin/target/angular-maven-plugin-1.0.0.jar /var/www/html/mavenrepository/com/guillaumetalbot/angular-maven-plugin/1.0.0/angular-maven-plugin-1.0.0.jar"
			}
		}
	}
}
