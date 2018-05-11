package org.ernest.applications.trampoline.entities;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MicroserviceGitInfo {

    private List<String> branches = new ArrayList<>();
    private String currentBranch;
}
