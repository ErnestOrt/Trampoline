package org.ernest.applications.trampoline.entities;


public class InstanceGitInfo {

    private String pomLocation;
    private String branch;
    private String commitMessage;
    private String commitOwner;
    private String commitDate;

    public String getPomLocation() {
        return pomLocation;
    }

    public void setPomLocation(String pomLocation) {
        this.pomLocation = pomLocation;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCommitMessage() {
        return commitMessage;
    }

    public void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
    }

    public String getCommitOwner() {
        return commitOwner;
    }

    public void setCommitOwner(String commitOwner) {
        this.commitOwner = commitOwner;
    }

    public String getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(String commitDate) {
        this.commitDate = commitDate;
    }
}

