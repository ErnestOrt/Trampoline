package org.ernest.applications.trampoline.buildtool;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BuildToolCommandRunner implements BuildToolRunner {

    private Map<String, String> environment = new HashMap<>();

    @Override
    public Process executeBuildToolProcess(String buildFile, String port, String arguments) throws IOException {
        String[] parsedArguments = getParsedArguments(arguments);

        ProcessBuilder processBuilder = new ProcessBuilder(getCommand(port, parsedArguments)).directory(new File(buildFile));
        processBuilder.environment().putAll(getEnvironment());

        return processBuilder.start();
    }

    private static String[] getParsedArguments(String arguments) {
        return (arguments.trim().length() > 0) ? arguments.trim().split("\\s+") : new String[0];
    }

    @Override
    public Map<String, String> getEnvironment() {
        return environment;
    }

    protected abstract List<String> getCommand(String port, String[] parsedArguments);

    protected static boolean isWindows() {
        return System.getProperties().getProperty("os.name").contains("Windows");
    }
}
