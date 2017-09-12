package org.ernest.applications.trampoline.services;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.ernest.applications.trampoline.entities.BuildTools;
import org.ernest.applications.trampoline.entities.Ecosystem;
import org.ernest.applications.trampoline.entities.Microservice;
import org.ernest.applications.trampoline.entities.MicroserviceConfiguration;
import org.ernest.applications.trampoline.exceptions.*;
import org.ernest.applications.trampoline.utils.ScriptContentsProvider;
import org.ernest.applications.trampoline.utils.VMParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Component
public class FileManager {

    private static final Logger logger = Logger.getLogger(FileManager.class);
    private static final String POM_XML = "pom.xml";

    private final ConfigurationLoader configurationLoader;

    @Value("${settings.folder.path.mac}")
	private String settingsFolderPathMac;

	@Value("${settings.folder.path.linux}")
	private String settingsFolderPathLinux;

	@Value("${settings.folder.path.windows}")
	private String settingsFolderPathWindows;
	
	@Value("${settings.file.name}")
	private String settingsFileName;

    @Autowired
    public FileManager(ConfigurationLoader configurationLoader) {
        this.configurationLoader = configurationLoader;
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
		if(ecosystem.getMicroservices().stream().anyMatch(m -> m.getActuatorPrefix() == null || m.getVmArguments() == null)){
			ecosystem.getMicroservices().stream().filter(m -> m.getActuatorPrefix() == null || m.getVmArguments() == null).forEach(m ->{
				m.setVmArguments("");
				m.setActuatorPrefix("");
				m.setBuildTool(BuildTools.MAVEN);
				createScript(m);
			});

			saveEcosystem(ecosystem);
		}

		if(ecosystem.getMicroservices().stream().anyMatch(m -> m.getBuildTool() == null)){
			ecosystem.getMicroservices().stream().filter(m -> m.getBuildTool() == null).forEach(m -> m.setBuildTool(BuildTools.MAVEN));
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

	public void runScript(Microservice microservice, String mavenBinaryLocation, String mavenHomeLocation, String port, String vmArguments) throws RunningMicroserviceScriptException {
		try {
			mavenBinaryLocation = (mavenBinaryLocation != null && mavenBinaryLocation.trim().length() > 0) ? mavenBinaryLocation : mavenHomeLocation + "/bin";
			if(System.getProperties().getProperty("os.name").contains("Windows")){
				String commands = FileUtils.readFileToString(new File(getSettingsFolder() +"/"+ microservice.getId() +".txt"));
				commands = commands.replace("#mavenBinaryLocation", mavenBinaryLocation);
				commands = commands.replace("#mavenHomeLocation", mavenHomeLocation);
				commands = commands.replace("#port", port);

				if(microservice.getBuildTool().equals(BuildTools.MAVEN)){
					commands = commands.replace("#vmArguments", vmArguments);
				}else{
					commands = commands.replace("#vmArguments", VMParser.toWindowsEnviromentVariables(vmArguments));
				}
				Runtime.getRuntime().exec("cmd /c start cmd.exe /K \""+commands+"\"");
			}else{
				if(microservice.getBuildTool().equals(BuildTools.MAVEN)){
					new ProcessBuilder("sh", getSettingsFolder() + "/" + microservice.getId() + ".sh", mavenHomeLocation, mavenBinaryLocation, port, vmArguments).start();
				}else{
					Runtime.getRuntime().exec("chmod 777 "+microservice.getPomLocation()+"//gradlew");
					new ProcessBuilder("sh", getSettingsFolder() + "/" + microservice.getId() + ".sh", port, VMParser.toUnixEnviromentVariables(vmArguments)).start();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw new RunningMicroserviceScriptException();
		}
	}

	public void createScript(Microservice microservice) throws CreatingMicroserviceScriptException {
		try {
			if(System.getProperties().getProperty("os.name").contains("Windows")){
				if(microservice.getBuildTool().equals(BuildTools.MAVEN)) {
					FileUtils.writeStringToFile(new File(getSettingsFolder() + "/" + microservice.getId() + ".txt"), ScriptContentsProvider.getMavenWindows(microservice.getPomLocation()));
				}else{
					FileUtils.writeStringToFile(new File(getSettingsFolder() + "/" + microservice.getId() + ".txt"), ScriptContentsProvider.getGradleWindows(microservice.getPomLocation()));
				}
			}else{
				if(microservice.getBuildTool().equals(BuildTools.MAVEN)) {
					FileUtils.writeStringToFile(new File(getSettingsFolder() + "/" + microservice.getId() + ".sh"),ScriptContentsProvider.getMavenUnix(microservice.getPomLocation()));
				}else{
					FileUtils.writeStringToFile(new File(getSettingsFolder() + "/" + microservice.getId() + ".sh"), ScriptContentsProvider.getGradleUnix(microservice.getPomLocation()));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new CreatingMicroserviceScriptException();
		}
	}

    public Optional<MicroserviceConfiguration> loadConfigurations(String pomLocation) {
        Optional<String> rootProjectLocation = getRootProjectLocation(pomLocation);
        return rootProjectLocation.flatMap(configurationLoader::load);
    }

    private Optional<String> getRootProjectLocation(String pomLocation) {
        File pom = new File(pomLocation);
        if (!pom.exists()) {
            return Optional.empty();
        }
        if (pom.isFile() && pomLocation.endsWith(POM_XML)) {
            return Optional.of(pomLocation.substring(0, pomLocation.length() - POM_XML.length()));
        }
        if (pom.isDirectory()) {
            return Optional.of(pomLocation);
        }
        return Optional.empty();
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
