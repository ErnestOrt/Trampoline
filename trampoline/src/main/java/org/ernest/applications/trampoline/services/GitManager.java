package org.ernest.applications.trampoline.services;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.ernest.applications.trampoline.entities.Microservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GitManager {

    @Autowired
    EcosystemManager ecosystemManager;

    public List<String> getMicroseriviceBranches(String microserviceId) throws IOException, GitAPIException {
        Microservice microservice = ecosystemManager.getEcosystem().getMicroservices().stream().filter(m -> m.getId().equals(microserviceId)).findAny().get();
        Git git = Git.open(new java.io.File(microservice.getGitLocation()));
        return git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call().stream().map(Ref::getName).collect(Collectors.toList());
    }
}
