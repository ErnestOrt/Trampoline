package org.ernest.applications.trampoline.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InstanceGitInfo {

    private String pomLocation;
    private String branch;
    private String commitMessage;
    private String commitOwner;
    private String commitDate;
}

