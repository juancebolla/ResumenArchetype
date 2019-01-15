pipeline {
    agent {
        node {
            label 'maven'
        }
    }

    options {
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '10'))
        ansiColor('xterm')
    }

    environment {
        // MODIFICAR: nombre de aplicacion.
        // ** no puede contener mayúsculas o caracteres especiales (Solo letras minúsculas y numeros). **
        APPLICATION_NAME = 'devunartec'

        // MODIFICAR: Directorio de subversion donde se encuentra el proyecto maven
        // (Relativo a la raiz del repositorio git)
        APPLICATION_PATH = 'ResumenOperFccActivos'

        // MODIFICAR:: Version de la aplicacion. Se obtiene desde el pom.xml
        APPLICATION_VERSION = 'latest'

        // NO MODIFICAR:: Nombre del proyecto donde se instala la aplicacion.
        // Debe variar dependiendo del ambiente donde se este instalando.
        OPENSHIFT_PROJECT = 'dev'

        // NO MODIFICAR:: ID que identifica el archivo settings.xml configurado en jenkins.
        MAVEN_SETTINGS_ID = 'e04af6c5-0732-4d21-9268-90067aca9168'
    }

    stages {
        stage('Maven build') {
            steps {
                dir(APPLICATION_PATH) {
                    configFileProvider([configFile(fileId: "${MAVEN_SETTINGS_ID}", variable: 'MAVEN_SETTINGS')]) {
                        sh 'mvn -s $MAVEN_SETTINGS clean package -DskipTests'
                    }
                }
            }
            post {
                success {
                    dir(APPLICATION_PATH) {
                        echo 'Almacenando jar en Jenkins.'
                        archiveArtifacts(artifacts: '**/target/*.jar', allowEmptyArchive: false)
                    }
                }
            }
        }

        stage('JUnit') {
            steps {
                dir(APPLICATION_PATH) {
                    configFileProvider([configFile(fileId: "${MAVEN_SETTINGS_ID}", variable: 'MAVEN_SETTINGS')]) {
                        echo 'Ejecutando pruebas unitarias.'
                        sh 'mvn -s $MAVEN_SETTINGS surefire:test'
                    }
                }
            }
            post {
                always {
                    dir(APPLICATION_PATH) {
                        junit 'target/surefire-reports/*.xml'
                    }
                }
            }
        }

        stage('Test Coverage') {
            steps {
                dir(APPLICATION_PATH) {
                    configFileProvider([configFile(fileId: "${MAVEN_SETTINGS_ID}", variable: 'MAVEN_SETTINGS')]) {
                        echo 'Verificando covertura de pruebas unitarias.'
                        sh 'mvn -s $MAVEN_SETTINGS verify'
                    }
                }
            }
            post {
                always {
                    dir(APPLICATION_PATH) {
                        publishHTML target: [
                            allowMissing: false,
                            alwaysLinkToLastBuild: false,
                            keepAll: true,
                            reportDir: 'target/jacoco',
                            reportFiles: 'index.html',
                            reportName: 'Jacoco Coverage Report'
                        ]
                    }
                }
            }
        }

        stage('Sonar Report') {
            steps {
                dir(APPLICATION_PATH) {
                    withSonarQubeEnv('Sonar') {
                        configFileProvider([configFile(fileId: "${MAVEN_SETTINGS_ID}", variable: 'MAVEN_SETTINGS')]) {
                            sh "mvn -s $MAVEN_SETTINGS sonar:sonar -DskipTests -Dsonar.branch=${env.BRANCH_NAME}"
                        }
                    }
                }
            }
        }

        stage("Sonar Quality Gate") {
            options {
                timeout(time: 5, unit: 'MINUTES')
            }
            steps {
                sh 'sleep 10'
                waitForQualityGate abortPipeline: true
            }
        }

        stage('develop Installation') {
            when {
                branch 'develop'
            }
            steps {
                script {
                    openshift.withCluster() {
                        openshift.withProject("${OPENSHIFT_PROJECT}") {
                            echo "Inicio Construccion. Cluster: ${openshift.cluster()} >> Proyecto: ${openshift.project()}"

                            dir(APPLICATION_PATH) {
                                echo 'Eliminando proyecto anterior'
                                openshift.selector( 'all', [ app: "${APPLICATION_NAME}", version: "${APPLICATION_VERSION}" ] ).delete( '--ignore-not-found=true' )
                                echo 'Eliminacion Completa'

                                echo 'Generando BUILD'
                                def build = openshift.apply(
                                    openshift.process(
                                        readFile("01-template-build.yaml"),
                                        "-p", "APPLICATION_NAME=${APPLICATION_NAME}",
                                        "-p", "APPLICATION_VERSION=${APPLICATION_VERSION}",
                                        "-p", "SOURCE_REPOSITORY=${env.GIT_URL}",
                                        "-p", "SOURCE_REF=${env.BRANCH_NAME}",
                                        "-p", "SOURCE_PATH=${APPLICATION_PATH}",
                                        "-p", "OPENSHIFT_NAMESPACE=${OPENSHIFT_PROJECT}"
                                    )
                                )
                                build.startBuild().logs('-f')

                                echo 'Generando POD'
                                def construccion = openshift.apply(
                                    openshift.process(
                                        readFile("02-template-pod.yaml"),
                                        "-p", "APPLICATION_NAME=${APPLICATION_NAME}",
                                        "-p", "APPLICATION_VERSION=${APPLICATION_VERSION}",
                                        "-p", "OPENSHIFT_NAMESPACE=${OPENSHIFT_PROJECT}"
                                    )
                                )

                                echo 'Generando NETWORKING'
                                def networking = openshift.apply(
                                    openshift.process(
                                        readFile("03-template-network.yaml"),
                                        "-p", "APPLICATION_NAME=${APPLICATION_NAME}",
                                        "-p", "APPLICATION_VERSION=${APPLICATION_VERSION}",
                                        "-p", "OPENSHIFT_NAMESPACE=${OPENSHIFT_PROJECT}"
                                    )
                                )

                                echo 'Verificando logs creacion de POD'
                                openshift.selector( 'dc', [ app: "${APPLICATION_NAME}", version: "${APPLICATION_VERSION}" ] ).logs('-f')

                                echo 'Termino Construccion'
                            }
                        }
                    }
                }
            }
        }
    }
}
