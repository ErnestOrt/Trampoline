package org.ernest.applications.trampoline.services;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
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

        Ecosystem ecosystem =  ecosystemManager.getEcosystem();
        Microservice microservice = ecosystem.getMicroservices().stream().filter(m -> m.getId().equals(microserviceId)).findAny().get();

        Git git = Git.open(new java.io.File(microservice.getGitLocation()));
        if(ecosystem.getGitCredentials()!=null){
            git.fetch().setCredentialsProvider(buildCredentialsProvider(ecosystem.getGitCredentials())).setRemoveDeletedRefs(true).call();
        }else{
            git.fetch().setRemoveDeletedRefs(true).call();
        }

        microserviceGitInfo.setBranches(git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call().stream().map(Ref::getName).collect(Collectors.toList()));
        microserviceGitInfo.setCurrentBranch(git.getRepository().getBranch());

        return microserviceGitInfo;
    }

    public void checkoutAndPull(String microserviceId, String branchName) throws IOException, GitAPIException {
        log.info("Checkout and Pulling code for microservice id: [{}] branchName: [{}]", microserviceId, branchName);
        Ecosystem ecosystem =  ecosystemManager.getEcosystem();
        Microservice microservice = ecosystem.getMicroservices().stream().filter(m -> m.getId().equals(microserviceId)).findAny().get();

        branchName = branchName.replaceAll("refs/remotes/origin/", "");
        branchName = branchName.replaceAll("refs/heads/", "");
        Git git = Git.open(new java.io.File(microservice.getGitLocation()));

        try {
            git.checkout().setCreateBranch(true).setName(branchName).setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK).setStartPoint("origin/" + branchName).call();
        }catch (RefAlreadyExistsException e){
            git.checkout().setCreateBranch(false).setName(branchName).call();
        }

        if(ecosystem.getGitCredentials()!=null){
            git.pull().setCredentialsProvider(buildCredentialsProvider(ecosystem.getGitCredentials())).call();
        }else{
            git.pull().call();
        }
    }

    public String getRegisteredUsername(Ecosystem ecosystem) {
        return ecosystem.getGitCredentials() != null ? encryptService.decrypt(ecosystem.getGitCredentials().getUsername()) : "";
    }

    public void saveCred(String user, String pass) {
        ecosystemManager.saveGitCred(encryptService.encrypt(user), encryptService.encrypt(pass));
    }

    public void cleanCred() {
        ecosystemManager.cleanGitCred();
    }

    public void cloneRepository(String gitUrl, String destinationFolder) throws GitAPIException {
        log.info("Cloning repository gitUrl: [{}] destinationFolder: [{}]", gitUrl, destinationFolder);

        Ecosystem ecosystem =  ecosystemManager.getEcosystem();
        CloneCommand cloneCommand = Git.cloneRepository();

        if(ecosystem.getGitCredentials()!=null){
            cloneCommand.setCredentialsProvider(buildCredentialsProvider(ecosystem.getGitCredentials()));
        }
        cloneCommand.setURI(gitUrl).setDirectory(new File(destinationFolder)).call();
    }

    private UsernamePasswordCredentialsProvider buildCredentialsProvider(GitCredentials gitCredentials) {
        return new UsernamePasswordCredentialsProvider(encryptService.decrypt(gitCredentials.getUsername()), encryptService.decrypt(gitCredentials.getPass()));
    }
}
