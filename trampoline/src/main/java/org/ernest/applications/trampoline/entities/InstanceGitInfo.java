package org.ernest.applications.trampoline.entities;

import lombok.Data;

@Data
public class InstanceGitInfo {

    private String pomLocation;
    private String branch;
    private String commitMessage;
    private String commitOwner;
    private String commitDate;
}
