package org.ernest.applications.trampoline.buildtool;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MavenRunner extends BuildToolCommandRunner {

    public final static String MAVEN_HOME = "M2_HOME";

    private String mavenPath;

    public String getMavenPath() {
        return mavenPath;
    }

    public void setMavenPath(String mavenPath) {
        this.mavenPath = mavenPath;
    }

    @Override
    protected List<String> getCommand(String port, String[] parsedArguments) {
        String mavenBin = (mavenPath == null) ? "mvn" : new File(mavenPath, "mvn").getAbsolutePath();
        List<String> command = new ArrayList<>(Arrays.asList(mavenBin, "spring-boot:run"));
        command.add("-Dserver.port=" + port);
        command.add("-Dendpoints.shutdown.enabled=true");
        command.add("-Dmanagement.security.enabled=false");
        command.add("-Dmanagement.info.git.mode=full");
        command.addAll(Arrays.asList(parsedArguments));
        return command;
    }

}
