package org.ernest.applications.trampoline.services;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.ernest.applications.trampoline.entities.BuildTools;
import org.ernest.applications.trampoline.entities.Ecosystem;
import org.ernest.applications.trampoline.entities.Microservice;
import org.ernest.applications.trampoline.exceptions.CreatingMicroserviceScriptException;
import org.ernest.applications.trampoline.exceptions.CreatingSettingsFolderException;
import org.ernest.applications.trampoline.exceptions.ReadingEcosystemException;
import org.ernest.applications.trampoline.exceptions.RunningMicroserviceScriptException;
import org.ernest.applications.trampoline.exceptions.SavingEcosystemException;
import org.ernest.applications.trampoline.utils.ScriptContentsProvider;
import org.ernest.applications.trampoline.utils.VMParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

@Component
public class FileManager {

    private final Logger log = LoggerFactory.getLogger(FileManager.class);

    @Value("${settings.folder.path.mac}")
    private String settingsFolderPathMac;

    @Value("${settings.folder.path.linux}")
    private String settingsFolderPathLinux;

    @Value("${settings.folder.path.windows}")
    private String settingsFolderPathWindows;

    @Value("${settings.file.name}")
    private String settingsFileName;

    @Value("${trampoline.version}")
    private float currentVersion;

    public Ecosystem getEcosystem() throws CreatingSettingsFolderException, ReadingEcosystemException {
        checkIfFileExistsAndCreatedIfNeeded();
        Ecosystem ecosystem = null;

        try {
            ecosystem = new Gson().fromJson(FileUtils.readFileToString(new File(getSettingsFolder() + "/" + settingsFileName)), Ecosystem.class);
        } catch (JsonSyntaxException | IOException e) {
            e.printStackTrace();
            throw new ReadingEcosystemException();
        }

        updateMicroservicesInformationStored(ecosystem);

        return ecosystem;
    }

    private void updateMicroservicesInformationStored(Ecosystem ecosystem) {
        boolean ecosystemChanged = false;
        if (ecosystem.getMicroservices().stream().anyMatch(m -> m.getActuatorPrefix() == null || m.getVmArguments() == null)) {
            ecosystem.getMicroservices().stream().filter(m -> m.getActuatorPrefix() == null || m.getVmArguments() == null).forEach(m -> {
                m.setVmArguments("");
                m.setActuatorPrefix("");
                m.setBuildTool(BuildTools.MAVEN);
                createScript(m);
            });
            ecosystemChanged = true;
        }

        if (ecosystem.getMicroservices().stream().anyMatch(m -> m.getBuildTool() == null)) {
            ecosystem.getMicroservices().stream().filter(m -> m.getBuildTool() == null).forEach(m -> m.setBuildTool(BuildTools.MAVEN));
            ecosystemChanged = true;
        }

        if (ecosystem.getMicroservices().stream().anyMatch(m -> m.getVersion() == null)) {
            ecosystem.getMicroservices().stream().filter(m -> m.getVersion() == null).forEach(m -> {
                m.setVersion(currentVersion);
                createScript(m);
            });
            ecosystemChanged = true;
        }

        if (ecosystemChanged) {
            saveEcosystem(ecosystem);
        }
    }

    public void saveEcosystem(Ecosystem ecosystem) throws SavingEcosystemException {
        log.info("Saving Ecosystem");
        try {
            FileUtils.writeStringToFile(new File(getSettingsFolder() + "/" + settingsFileName), new Gson().toJson(ecosystem));
        } catch (IOException e) {
            e.printStackTrace();
            throw new SavingEcosystemException();
        }
    }

    public void runScript(Microservice microservice, String mavenBinaryLocation, String mavenHomeLocation, String port, String vmArguments) throws RunningMicroserviceScriptException {
        try {
            if (System.getProperties().getProperty("os.name").contains("Windows")) {
                String commands = FileUtils.readFileToString(new File(getSettingsFolder() + "/" + microservice.getId() + ".txt"));
                commands = commands.replace("#port", port);
                if (microservice.getBuildTool().equals(BuildTools.MAVEN)) {
                    mavenBinaryLocation = (mavenBinaryLocation != null && mavenBinaryLocation.trim().length() > 0) ? mavenBinaryLocation : mavenHomeLocation + "/bin";
                    commands = commands.replace("#mavenBinaryLocation", mavenBinaryLocation);
                    commands = commands.replace("#mavenHomeLocation", mavenHomeLocation);
                    commands = commands.replace("#vmArguments", vmArguments);
                } else if (microservice.getBuildTool().equals(BuildTools.JAR)) {
                    commands = commands.replace("#vmArguments", vmArguments);
                } else {
                    commands = commands.replace("#vmArguments", VMParser.toWindowsEnviromentVariables(vmArguments));
                }
                log.info("Starting [" + microservice.getId() + "] with following command [" + commands + "]");
                Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"" + commands + "\"");
            } else {
                if (microservice.getBuildTool().equals(BuildTools.MAVEN)) {
                    mavenBinaryLocation = (mavenBinaryLocation != null && mavenBinaryLocation.trim().length() > 0) ? mavenBinaryLocation : mavenHomeLocation + "/bin";
                    new ProcessBuilder("sh", getSettingsFolder() + "/" + microservice.getId() + ".sh", mavenHomeLocation, mavenBinaryLocation, port, vmArguments).start();
                } else if (microservice.getBuildTool().equals(BuildTools.JAR)) {
                    log.info("Starting jar with : {} serviceId: {} , port: {}, vmArgs: {}" , getSettingsFolder() , microservice.getId() + ".sh", port, VMParser.toUnixEnviromentVariables(vmArguments));
                    new ProcessBuilder("sh", getSettingsFolder() + "/" + microservice.getId() + ".sh", port, vmArguments).start();
                }  else {
                    Runtime.getRuntime().exec("chmod 777 " + microservice.getPomLocation() + "//gradlew");
                    new ProcessBuilder("sh", getSettingsFolder() + "/" + microservice.getId() + ".sh", port, VMParser.toUnixEnviromentVariables(vmArguments)).start();
                }
                log.info("Build Tool: {} " ,microservice.getBuildTool());
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RunningMicroserviceScriptException();
        }
    }

    public void createScript(Microservice microservice) throws CreatingMicroserviceScriptException {
        log.info("Creating deployment script for microservice [{}]", microservice.getId());
        try {
            if (System.getProperties().getProperty("os.name").contains("Windows")) {
                try {
                    FileUtils.forceDelete(new File(getSettingsFolder() + "/" + microservice.getId() + ".txt"));
                } catch (Exception e) {
                }

                if (microservice.getBuildTool().equals(BuildTools.MAVEN)) {
                    FileUtils.writeStringToFile(new File(getSettingsFolder() + "/" + microservice.getId() + ".txt"), ScriptContentsProvider.getMavenWindows(microservice.getPomLocation()));
                } else if (microservice.getBuildTool().equals(BuildTools.JAR)) {
                    FileUtils.writeStringToFile(new File(getSettingsFolder() + "/" + microservice.getId() + ".txt"), ScriptContentsProvider.getJar(microservice.getPomLocation()));
                } else {
                    FileUtils.writeStringToFile(new File(getSettingsFolder() + "/" + microservice.getId() + ".txt"), ScriptContentsProvider.getGradleWindows(microservice.getPomLocation()));
                }
            } else {
                try {
                    FileUtils.forceDelete(new File(getSettingsFolder() + "/" + microservice.getId() + ".sh"));
                } catch (Exception e) {
                }

                if (microservice.getBuildTool().equals(BuildTools.MAVEN)) {
                    FileUtils.writeStringToFile(new File(getSettingsFolder() + "/" + microservice.getId() + ".sh"), ScriptContentsProvider.getMavenUnix(microservice.getPomLocation()));
                } else if (microservice.getBuildTool().equals(BuildTools.JAR)) {
                    FileUtils.writeStringToFile(new File(getSettingsFolder() + "/" + microservice.getId() + ".sh"), ScriptContentsProvider.getJar(microservice.getPomLocation()));
                } else {
                    FileUtils.writeStringToFile(new File(getSettingsFolder() + "/" + microservice.getId() + ".sh"), ScriptContentsProvider.getGradleUnix(microservice.getPomLocation()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new CreatingMicroserviceScriptException();
        }
    }

    public String getSettingsFolder() {
        if (System.getProperties().getProperty("os.name").contains("Mac")) {
            return settingsFolderPathMac.replaceAll("#userName", System.getProperties().getProperty("user.name"));
        } else if (System.getProperties().getProperty("os.name").contains("Windows")) {
            return settingsFolderPathWindows;
        } else {
            return settingsFolderPathLinux.replaceAll("#userName", System.getProperties().getProperty("user.name"));
        }
    }

    private void checkIfFileExistsAndCreatedIfNeeded() throws CreatingSettingsFolderException {
        try {
            File file = new File(getSettingsFolder());
            if (!file.exists()) {
                file.mkdirs();
                FileUtils.writeStringToFile(new File(getSettingsFolder() + "/" + settingsFileName), new Gson().toJson(new Ecosystem()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CreatingSettingsFolderException();
        }
    }


}
