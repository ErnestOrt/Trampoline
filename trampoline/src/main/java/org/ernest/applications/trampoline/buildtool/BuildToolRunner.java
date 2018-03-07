package org.ernest.applications.trampoline.buildtool;

import java.io.IOException;
import java.util.Map;

public interface BuildToolRunner {
    Process executeBuildToolProcess(String buildFile, String port, String arguments) throws IOException;
    Map<String, String> getEnvironment();
}
