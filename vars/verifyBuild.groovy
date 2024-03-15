
import com.build.utils.gerritChanges;

@NonCPS
def call(gerritChanges gc) {
    pipeline {
        agent { label 'ubuntu'}
        environment {
            VERSION = "ttttttttttttttt"
        }

        stages {
            stage('Hello') {
            steps {
                echo 'Hello World'
                script{
                    echo 'hellllo '
                   // echo gc.changes
                }
            }
            }
            stage('test-env') {
                steps {
                    echo 'Hello World'
                    script{
                       def ret = '--------------------------'
                       echo ret
                    }
                }
            }
            stage('verify-env') {
                steps {
                    echo 'Hello World'
                    script{
                        sh'''#!/bin/bash
                        env
                         '''
                    }
                }
            }
        }
    }
}
