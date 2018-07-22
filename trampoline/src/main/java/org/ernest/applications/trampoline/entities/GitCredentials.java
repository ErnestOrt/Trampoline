package org.ernest.applications.trampoline.entities;

public class GitCredentials {

    private HttpsSettings httpsSettings;
    private SshSettings sshSettings;

    public GitCredentials() {
    }

    public GitCredentials(String username, String pass, String sshKeyLocation) {
        httpsSettings = new HttpsSettings(username, pass);
        sshSettings = new SshSettings(sshKeyLocation);
    }

    public GitCredentials(String username, String pass) {
        httpsSettings = new HttpsSettings(username, pass);
    }

    public GitCredentials(String sshKeyLocation) {
        sshSettings = new SshSettings(sshKeyLocation);
    }

    public HttpsSettings getHttpsSettings() {
        return httpsSettings;
    }

    public void setHttpsSettings(HttpsSettings httpsSettings) {
        this.httpsSettings = httpsSettings;
    }

    public SshSettings getSshSettings() {
        return sshSettings;
    }

    public void setSshSettings(SshSettings sshSettings) {
        this.sshSettings = sshSettings;
    }

    public class HttpsSettings {
        private String username;
        private String pass;

        public HttpsSettings(String username, String pass) {
            this.username = username;
            this.pass = pass;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPass() {
            return pass;
        }

        public void setPass(String pass) {
            this.pass = pass;
        }

    }

    public class SshSettings {
        private String sshKeyLocation;

        public SshSettings(String sshKeyLocation) {
            this.sshKeyLocation = sshKeyLocation;
        }

        public String getSshKeyLocation() {
            return sshKeyLocation;
        }

        public void setSshKeyLocation(String sshKeyLocation) {
            this.sshKeyLocation = sshKeyLocation;
        }

    }
}
