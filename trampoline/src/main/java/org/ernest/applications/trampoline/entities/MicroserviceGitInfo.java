package org.ernest.applications.trampoline.entities;

import java.util.ArrayList;
import java.util.List;


public class MicroserviceGitInfo {

    private List<String> branches = new ArrayList<>();
    private String currentBranch;

    public List<String> getBranches() {
        return branches;
    }

    public void setBranches(List<String> branches) {
        this.branches = branches;
    }

    public String getCurrentBranch() {
        return currentBranch;
    }

    public void setCurrentBranch(String currentBranch) {
        this.currentBranch = currentBranch;
    }
}
