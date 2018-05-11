package org.ernest.applications.trampoline.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GitCredentials {

    private String username;
    private String pass;

    public GitCredentials(String username, String pass) {
        this.username = username;
        this.pass = pass;
    }
}
