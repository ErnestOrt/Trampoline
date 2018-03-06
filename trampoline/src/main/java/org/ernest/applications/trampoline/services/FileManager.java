package org.ernest.applications.trampoline.services;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.ernest.applications.trampoline.buildtool.BuildToolRunner;
import org.ernest.applications.trampoline.buildtool.GradleRunner;
import org.ernest.applications.trampoline.buildtool.MavenRunner;
import org.ernest.applications.trampoline.entities.BuildTools;
import org.ernest.applications.trampoline.entities.Ecosystem;
import org.ernest.applications.trampoline.entities.Microservice;
import org.ernest.applications.trampoline.exceptions.CreatingSettingsFolderException;
import org.ernest.applications.trampoline.exceptions.ReadingEcosystemException;
import org.ernest.applications.trampoline.exceptions.RunningMicroserviceScriptException;
import org.ernest.applications.trampoline.exceptions.SavingEcosystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

@Component
public class FileManager {

	private static final Logger LOG = LoggerFactory.getLogger(FileManager.class);

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

    private GradleRunner gradleRunner;
    private MavenRunner mavenRunner;

    public FileManager() {
    	gradleRunner = new GradleRunner();
    	mavenRunner = new MavenRunner();
	}

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
        if(ecosystem.getMicroservices().stream().anyMatch(m -> m.getActuatorPrefix() == null || m.getVmArguments() == null)){
            ecosystem.getMicroservices().stream().filter(m -> m.getActuatorPrefix() == null || m.getVmArguments() == null).forEach(m ->{
                m.setVmArguments("");
                m.setActuatorPrefix("");
                m.setBuildTool(BuildTools.MAVEN);
            });
			ecosystemChanged = true;
        }

        if(ecosystem.getMicroservices().stream().anyMatch(m -> m.getBuildTool() == null)){
            ecosystem.getMicroservices().stream().filter(m -> m.getBuildTool() == null).forEach(m -> m.setBuildTool(BuildTools.MAVEN));
			ecosystemChanged = true;
        }

        if(ecosystem.getMicroservices().stream().anyMatch(m -> m.getVersion() == null)){
            ecosystem.getMicroservices().stream().filter(m -> m.getVersion() == null).forEach(m -> {
                m.setVersion(currentVersion);
            });
			ecosystemChanged = true;
        }

        if(ecosystemChanged){
			saveEcosystem(ecosystem);
		}
    }

	public void saveEcosystem(Ecosystem ecosystem) throws SavingEcosystemException {
		try {
			FileUtils.writeStringToFile(new File(getSettingsFolder() + "/" + settingsFileName), new Gson().toJson(ecosystem));
		} catch (IOException e) {
			e.printStackTrace();
			throw new SavingEcosystemException();
		}
	}

	public void runMicroservice(Microservice microservice, String mavenBinaryLocation, String mavenHomeLocation, String port, String vmArguments) throws RunningMicroserviceScriptException {
		if (microservice.getBuildTool().equals(BuildTools.MAVEN)) {
			mavenBinaryLocation = (mavenBinaryLocation != null && mavenBinaryLocation.trim().length() > 0) ? mavenBinaryLocation : mavenHomeLocation + "/bin";
			mavenRunner.getEnvironment().put(MavenRunner.MAVEN_HOME, mavenHomeLocation);
			mavenRunner.setMavenPath(mavenBinaryLocation);
			runBuildTool(microservice, port, vmArguments, mavenRunner);
		} else if (microservice.getBuildTool().equals(BuildTools.GRADLE)) {
			runBuildTool(microservice, port, vmArguments, gradleRunner);
		}
	}

	private static void runBuildTool(Microservice microservice, String port, String vmArguments, BuildToolRunner buildToolRunner) throws RunningMicroserviceScriptException {
		try {
            buildToolRunner.executeBuildToolProcess(microservice.getPomLocation(), port, vmArguments);
        } catch (IOException e) {
            LOG.error("Failed to run build tool for microservice [id: {} name: {}]", microservice.getId(), microservice.getName());
            throw new RunningMicroserviceScriptException(e.getMessage());
        }
	}

	private String getSettingsFolder() {
		
		if(System.getProperties().getProperty("os.name").contains("Mac")){
			return settingsFolderPathMac.replaceAll("#userName", System.getProperties().getProperty("user.name"));
		}else if(System.getProperties().getProperty("os.name").contains("Windows")){
			return settingsFolderPathWindows;
		}else{
			return settingsFolderPathLinux.replaceAll("#userName", System.getProperties().getProperty("user.name"));
		}
	}

	private void checkIfFileExistsAndCreatedIfNeeded() throws CreatingSettingsFolderException {
		try{
			File file = new File(getSettingsFolder());
			if(!file.exists()){			
				file.mkdirs();
				FileUtils.writeStringToFile(new File(getSettingsFolder() + "/" + settingsFileName), new Gson().toJson(new Ecosystem()));
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new CreatingSettingsFolderException();
		}
	}


}
