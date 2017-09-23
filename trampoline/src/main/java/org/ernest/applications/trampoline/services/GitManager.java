package org.ernest.applications.trampoline.services;

import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.lib.Ref;
import org.ernest.applications.trampoline.entities.Microservice;
import org.ernest.applications.trampoline.entities.MicroserviceGitInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class GitManager {

    @Autowired
    EcosystemManager ecosystemManager;

    public MicroserviceGitInfo getMicroseriviceBranches(String microserviceId) throws IOException, GitAPIException {
        MicroserviceGitInfo microserviceGitInfo = new MicroserviceGitInfo();
        Microservice microservice = ecosystemManager.getEcosystem().getMicroservices().stream().filter(m -> m.getId().equals(microserviceId)).findAny().get();

        Git git = Git.open(new java.io.File(microservice.getGitLocation()));
        microserviceGitInfo.setBranches(git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call().stream().map(Ref::getName).collect(Collectors.toList()));
        microserviceGitInfo.setCurrentBranch(git.getRepository().getBranch());

        return microserviceGitInfo;
    }

    public void checkoutAndPull(String microserviceId, String branchName) throws IOException, GitAPIException {

        MicroserviceGitInfo microserviceGitInfo = new MicroserviceGitInfo();
        Microservice microservice = ecosystemManager.getEcosystem().getMicroservices().stream().filter(m -> m.getId().equals(microserviceId)).findAny().get();

        branchName = branchName.replaceAll("refs/remotes/origin/", "");
        branchName = branchName.replaceAll("refs/heads/", "");
        Git git = Git.open(new java.io.File(microservice.getGitLocation()));

        try {
            git.checkout().setCreateBranch(true).setName(branchName).setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK).setStartPoint("origin/" + branchName).call();
        }catch (RefAlreadyExistsException e){
            git.checkout().setCreateBranch(false).setName(branchName).call();
        }

        git.pull().call();
    }
}
