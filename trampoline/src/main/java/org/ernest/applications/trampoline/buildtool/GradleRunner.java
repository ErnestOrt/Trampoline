package org.ernest.applications.trampoline.buildtool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GradleRunner extends BuildToolCommandRunner {

    @Override
    protected List<String> getCommand(String port, String[] parsedArguments) {
        String wrapper = (isWindows()) ? "gradlew.bat" : "./gradlew";
        List<String> command = new ArrayList<>(Arrays.asList(wrapper, "bootRun"));
        command.addAll(Arrays.asList(parsedArguments));
        getEnvironment().put("endpoints.shutdown.enabled", "true");
        getEnvironment().put("management.security.enabled", "false");
        getEnvironment().put("management.info.git.mode", "full");
        getEnvironment().put("server.port", port);
        return command;
    }

}
