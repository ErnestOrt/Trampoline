package org.ernest.applications.trampoline.services;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.errors.UnsupportedCredentialItem;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.CredentialItem;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.CredentialsProviderUserInfo;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.util.FS;
import org.ernest.applications.trampoline.entities.Ecosystem;
import org.ernest.applications.trampoline.entities.GitCredentials;
import org.ernest.applications.trampoline.entities.Microservice;
import org.ernest.applications.trampoline.entities.MicroserviceGitInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

@Component
public class GitManager {

    private final Logger log = LoggerFactory.getLogger(GitManager.class);

    @Autowired
    EcosystemManager ecosystemManager;

    @Autowired
    EncryptService encryptService;

    public MicroserviceGitInfo getMicroseriviceBranches(String microserviceId) throws IOException, GitAPIException {
        log.info("Reading GIT Branches for microservice id: [{}]", microserviceId);
        MicroserviceGitInfo microserviceGitInfo = new MicroserviceGitInfo();

        Ecosystem ecosystem = ecosystemManager.getEcosystem();
        Microservice microservice = ecosystem.getMicroservices().stream().filter(m -> m.getId().equals(microserviceId)).findAny().get();

        Git git = Git.open(new java.io.File(microservice.getGitLocation()));
        if (ecosystem.getGitCredentials().getHttpsSettings() != null) {
            git.fetch().setCredentialsProvider(buildCredentialsProvider(ecosystem.getGitCredentials().getHttpsSettings())).setRemoveDeletedRefs(true).call();
        } else if (ecosystem.getGitCredentials().getSshSettings() != null) {
            SshSessionFactory sshSessionFactory = getSshSessionFactory(ecosystem);
            git.fetch().setTransportConfigCallback(transport -> {
                SshTransport sshTransport = (SshTransport) transport;
                sshTransport.setSshSessionFactory(sshSessionFactory);
            });
        } else {
            git.fetch().setRemoveDeletedRefs(true).call();
        }

        microserviceGitInfo.setBranches(git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call().stream().map(Ref::getName).collect(Collectors.toList()));
        microserviceGitInfo.setCurrentBranch(git.getRepository().getBranch());

        return microserviceGitInfo;
    }

    public void checkoutAndPull(String microserviceId, String branchName) throws IOException, GitAPIException {
        log.info("Checkout and Pulling code for microservice id: [{}] branchName: [{}]", microserviceId, branchName);
        Ecosystem ecosystem = ecosystemManager.getEcosystem();
        Microservice microservice = ecosystem.getMicroservices().stream().filter(m -> m.getId().equals(microserviceId)).findAny().get();

        branchName = branchName.replaceAll("refs/remotes/origin/", "");
        branchName = branchName.replaceAll("refs/heads/", "");
        Git git = Git.open(new java.io.File(microservice.getGitLocation()));

        try {
            git.checkout().setCreateBranch(true).setName(branchName).setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK).setStartPoint("origin/" + branchName).call();
        } catch (RefAlreadyExistsException e) {
            git.checkout().setCreateBranch(false).setName(branchName).call();
        }

        if (ecosystem.getGitCredentials().getHttpsSettings() != null) {
            git.pull().setCredentialsProvider(buildCredentialsProvider(ecosystem.getGitCredentials().getHttpsSettings())).call();
        } else if (ecosystem.getGitCredentials().getSshSettings() != null) {
            SshSessionFactory sshSessionFactory = getSshSessionFactory(ecosystem);
            git.fetch().setTransportConfigCallback(transport -> {
                SshTransport sshTransport = (SshTransport) transport;
                sshTransport.setSshSessionFactory(sshSessionFactory);
            });
        } else {
            git.pull().call();
        }
    }

    public String getRegisteredUsername(Ecosystem ecosystem) {
        return ecosystem.getGitCredentials() != null
                && ecosystem.getGitCredentials().getHttpsSettings() != null
                ? encryptService.decrypt(ecosystem.getGitCredentials().getHttpsSettings().getUsername()) : "";
    }

    public String getSshKeyLocation(Ecosystem ecosystem) {
        return ecosystem.getGitCredentials() != null
                && ecosystem.getGitCredentials().getSshSettings() != null
                ? ecosystem.getGitCredentials().getSshSettings().getSshKeyLocation() : "~/.ssh/id_rsa";
    }

    public void saveHttpsCred(String user, String pass) {
        ecosystemManager.saveGitHttpsCred(encryptService.encrypt(user), encryptService.encrypt(pass));
    }

    public void saveSshCred(String privateKeyLocation, String sshKeyPassword) {
        ecosystemManager.saveGitSshCred(privateKeyLocation, encryptService.encrypt(sshKeyPassword));
    }

    public void cleanCred() {
        ecosystemManager.cleanGitCred();
    }

    public void cloneRepository(String gitUrl, String destinationFolder) throws GitAPIException {
        log.info("Cloning repository gitUrl: [{}] destinationFolder: [{}]", gitUrl, destinationFolder);

        Ecosystem ecosystem = ecosystemManager.getEcosystem();
        CloneCommand cloneCommand = Git.cloneRepository();

        if (ecosystem.getGitCredentials().getHttpsSettings() != null) {
            cloneCommand.setCredentialsProvider(buildCredentialsProvider(ecosystem.getGitCredentials().getHttpsSettings()));
        }

        if (gitUrl.startsWith("ssh") || gitUrl.startsWith("git@")) {
            SshSessionFactory sshSessionFactory = getSshSessionFactory(ecosystem);
            cloneCommand.setTransportConfigCallback(transport -> {
                SshTransport sshTransport = (SshTransport) transport;
                sshTransport.setSshSessionFactory(sshSessionFactory);
            });
        }
        cloneCommand.setURI(gitUrl).setDirectory(new File(destinationFolder)).call();
    }

    private UsernamePasswordCredentialsProvider buildCredentialsProvider(GitCredentials.HttpsSettings gitCredentials) {
        return new UsernamePasswordCredentialsProvider(encryptService.decrypt(gitCredentials.getUsername()), encryptService.decrypt(gitCredentials.getPass()));
    }

    private SshSessionFactory getSshSessionFactory(Ecosystem ecosystem) {
        return new JschConfigSessionFactory() {
            private final GitCredentials.SshSettings sshSettings = ecosystem.getGitCredentials().getSshSettings();

            @Override
            protected void configure(OpenSshConfig.Host host, Session session) {
                if (sshSettings != null && !sshSettings.getSshKeyPassword().isEmpty()) {
                    CredentialsProvider provider = new CredentialsProvider() {
                        @Override
                        public boolean isInteractive() {
                            return false;
                        }

                        @Override
                        public boolean supports(CredentialItem... items) {
                            return true;
                        }

                        @Override
                        public boolean get(URIish uri, CredentialItem... items) throws UnsupportedCredentialItem {
                            for (CredentialItem item: items) {
                                ((CredentialItem.StringType) item).setValue(encryptService.decrypt(sshSettings.getSshKeyPassword()));
                            }
                            return true;
                        }
                    };
                    UserInfo userInfo = new CredentialsProviderUserInfo(session, provider);
                    session.setUserInfo(userInfo);
                }
            }

            @Override
            protected JSch createDefaultJSch(FS fs) throws JSchException {
                JSch defaultJSch = super.createDefaultJSch(fs);
                defaultJSch.addIdentity(sshSettings.getSshKeyLocation());
                return defaultJSch;
            }
        };
    }
}
