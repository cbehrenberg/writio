import jenkins.model.*
import hudson.security.*

def jenkins = Jenkins.getInstance()
if(!(jenkins.getSecurityRealm() instanceof HudsonPrivateSecurityRealm))
    jenkins.setSecurityRealm(new HudsonPrivateSecurityRealm(false))

if(!(jenkins.getAuthorizationStrategy() instanceof GlobalMatrixAuthorizationStrategy))
    jenkins.setAuthorizationStrategy(new GlobalMatrixAuthorizationStrategy())
	
def adminUsername = new File("/run/secrets/writio-ci-jenkins-username").text.trim()
def adminSecret = new File("/run/secrets/writio-ci-jenkins-secret").text.trim()

def user = jenkins.getSecurityRealm().createAccount(adminUsername, adminSecret)
user.save()

jenkins.getAuthorizationStrategy().add(Jenkins.ADMINISTER, adminUsername)
jenkins.save()