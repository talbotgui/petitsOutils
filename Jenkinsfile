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
				sh "mvn clean install -f angular-maven-plugin/pom.xml"
				junit '**/TEST-*Test.xml'
			}
		}

		// Déploiement dans le repository exposé par le serveur Apache
		stage ('Deploy') {
			agent any
			when { branch 'master' }
			steps {
				script {
					def pom = readMavenPom file: 'angular-maven-plugin/pom.xml'
					sh "mvn org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file -Dfile=./angular-maven-plugin/target/angular-maven-plugin-${pom.version}.jar -DgroupId=com.guillaumetalbot -DartifactId=angular-maven-plugin -Dversion=${pom.version} -Dpackaging=jar -DlocalRepositoryPath=/var/www/html/mavenrepository"
				}
			}
		}
	}
}
