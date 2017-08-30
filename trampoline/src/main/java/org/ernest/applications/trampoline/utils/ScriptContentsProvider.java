package org.ernest.applications.trampoline.utils;

public class ScriptContentsProvider {

    public static String getMavenWindows(String pomLocation){
        return "SET M2_HOME=#mavenHomeLocation&& SET PATH=%PATH%;#mavenBinaryLocation&& cd " + pomLocation + " && mvn spring-boot:run -Dserver.port=#port "
                + "-Dendpoints.shutdown.enabled=true -Dmanagement.security.enabled=false #vmArguments";
    }

    public static String getGradleWindows(String pomLocation){
        return "SET server.port=#port&& SET endpoints.shutdown.enabled=true&& SET management.security.enabled=false #vmArguments&& cd "+pomLocation+" && gradlew.bat bootRun ";
    }

    public static String getMavenUnix(String pomLocation){
        return "export M2_HOME=$1; export PATH=$PATH:$2; cd " + pomLocation + "; mvn spring-boot:run -Dserver.port=$3 -Dendpoints.shutdown.enabled=true -Dmanagement.security.enabled=false $4";
    }

    public static String getGradleUnix(String pomLocation){
        return "export SERVER_PORT=$1; export ENDPOINTS_SHUTDOWN_ENABLED=true; export MANAGEMENT_SECURITY_ENABLED=false $2; cd " + pomLocation + "; ./gradlew bootRun";
    }
}
