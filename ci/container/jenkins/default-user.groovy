import jenkins.model.*
import hudson.security.*

def jenkins = Jenkins.getInstance()
if(!(jenkins.getSecurityRealm() instanceof HudsonPrivateSecurityRealm))
    jenkins.setSecurityRealm(new HudsonPrivateSecurityRealm(false))

if(!(jenkins.getAuthorizationStrategy() instanceof GlobalMatrixAuthorizationStrategy))
    jenkins.setAuthorizationStrategy(new GlobalMatrixAuthorizationStrategy())

String jenkins_username = new File('/run/secrets/writio-ci-jenkins-username').text
String jenkins_secret = new File('/run/secrets/writio-ci-jenkins-secret').text

def user = jenkins.getSecurityRealm().createAccount(jenkins_username, jenkins_secret)
user.save()

jenkins.getAuthorizationStrategy().add(Jenkins.ADMINISTER, jenkins_username)
jenkins.save()
