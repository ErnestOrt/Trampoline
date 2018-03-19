package org.ernest.applications.trampoline.utils;

public class ScriptContentsProvider {

    public static String getMavenWindows(String pomLocation){
        return "SET M2_HOME=#mavenHomeLocation&& SET PATH=%PATH%;#mavenBinaryLocation&& cd " + pomLocation + " && mvn spring-boot:run -Dserver.port=#port "
                + "-Dendpoints.shutdown.enabled=true -Dmanagement.security.enabled=false -Dmanagement.info.git.mode=full -Dmanagement.endpoints.web.exposure.include=* -Dmanagement.endpoint.shutdown.enabled=true #vmArguments";
    }

    public static String getGradleWindows(String pomLocation){
        return "SET SERVER_PORT=#port&& SET ENDPOINTS_SHUTDOWN_ENABLED=true&& SET MANAGEMENT_SECURITY_ENABLED=false&& SET MANAGEMENT_INFO_GIT_MODE=full&& SET MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=*&& SET MANAGEMENT_ENDPOINT_SHUTDOWN_ENABLED=true #vmArguments&& cd "+pomLocation+" && gradlew.bat bootRun ";
    }

    public static String getMavenUnix(String pomLocation){
        return "export M2_HOME=$1; export PATH=$PATH:$2; cd " + pomLocation + "; mvn spring-boot:run -Dserver.port=$3 -Dendpoints.shutdown.enabled=true -Dmanagement.security.enabled=false -Dmanagement.info.git.mode=full -Dmanagement.endpoints.web.exposure.include=* -Dmanagement.endpoint.shutdown.enabled=true $4";
    }

    public static String getGradleUnix(String pomLocation){
        return "export SERVER_PORT=$1; export ENDPOINTS_SHUTDOWN_ENABLED=true; export MANAGEMENT_SECURITY_ENABLED=false; export MANAGEMENT_INFO_GIT_MODE=full; export MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=*; export MANAGEMENT_ENDPOINT_SHUTDOWN_ENABLED=true $2; cd " + pomLocation + "; ./gradlew bootRun";
    }
}
