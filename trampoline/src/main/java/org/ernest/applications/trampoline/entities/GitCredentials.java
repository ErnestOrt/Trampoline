package org.ernest.applications.trampoline.entities;

public class GitCredentials {

    private HttpsSettings httpsSettings;
    private SshSettings sshSettings;

    public GitCredentials() {
    }

    public GitCredentials(String username, String pass, String sshKeyLocation, String sshKeyPassword) {
        httpsSettings = new HttpsSettings(username, pass);
        sshSettings = new SshSettings(sshKeyLocation, sshKeyPassword);
    }

    public GitCredentials(HttpsSettings httpsSettings) {
        this.httpsSettings = httpsSettings;
    }

    public GitCredentials(SshSettings sshSettings) {
        this.sshSettings = sshSettings;
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

    public static class HttpsSettings {
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

    public static class SshSettings {
        private String sshKeyLocation;
        private String sshKeyPassword;

        public SshSettings(String sshKeyLocation, String sshKeyPassword) {
            this.sshKeyLocation = sshKeyLocation;
        }

        public String getSshKeyLocation() {
            return sshKeyLocation;
        }

        public void setSshKeyLocation(String sshKeyLocation) {
            this.sshKeyLocation = sshKeyLocation;
        }

        public String getSshKeyPassword() {
            return sshKeyPassword;
        }

        public void setSshKeyPassword(String sshKeyPassword) {
            this.sshKeyPassword = sshKeyPassword;
        }
    }
}
