package org.ernest.applications.trampoline.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GitCredentials {

    private String username;
    private String pass;

    public GitCredentials(String username, String pass) {
        this.username = username;
        this.pass = pass;
    }
}
