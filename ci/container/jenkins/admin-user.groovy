import jenkins.model.*
def jenkins = Jenkins.getInstance()

import hudson.security.*
def realm = new HudsonPrivateSecurityRealm(false)
jenkins.setSecurityRealm(realm)

def strategy = new hudson.security.GlobalMatrixAuthorizationStrategy()

def adminUsername = new File("/run/secrets/writio-ci-jenkins-username").text.trim()
def adminSecret = new File("/run/secrets/writio-ci-jenkins-secret").text.trim()

def admin = realm.createAccount(adminUsername, adminSecret)
admin.save()

strategy.add(Jenkins.ADMINISTER, adminUsername)
jenkins.setAuthorizationStrategy(strategy)

jenkins.save()

