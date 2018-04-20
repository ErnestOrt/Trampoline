package org.ernest.applications.trampoline.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MicroserviceGitInfo {

    private List<String> branches = new ArrayList<>();
    private String currentBranch;
}
