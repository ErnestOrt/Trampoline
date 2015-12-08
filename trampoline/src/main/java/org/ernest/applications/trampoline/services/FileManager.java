package org.ernest.applications.trampoline.services;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.ernest.applications.trampoline.entities.Ecosystem;
import org.ernest.applications.trampoline.exceptions.CreatingMicroserviceScriptException;
import org.ernest.applications.trampoline.exceptions.CreatingSettingsFolderException;
import org.ernest.applications.trampoline.exceptions.ReadingEcosystemException;
import org.ernest.applications.trampoline.exceptions.RunningMicroserviceScriptException;
import org.ernest.applications.trampoline.exceptions.SavingEcosystemException;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

@Component
public class FileManager {

	private static final String TRAMPOLINE_FOLDER = "/Users/"+System.getProperties().getProperty("user.name")+"/Documents/trampoline";
	private static final String TRAMPOLINE_SETTINGS_PATH = TRAMPOLINE_FOLDER + "/settings.txt";
	
	public Ecosystem getEcosystem() throws CreatingSettingsFolderException, ReadingEcosystemException {
		checkIfFileExistsAndCreatedIfNeeded();
		Ecosystem ecosystem = null;
		
		try {
			ecosystem = new Gson().fromJson(FileUtils.readFileToString(new File(TRAMPOLINE_SETTINGS_PATH)), Ecosystem.class);
		} catch (JsonSyntaxException | IOException e) {
			e.printStackTrace();
			throw new ReadingEcosystemException();
		}
		
		return ecosystem;
	}
	
	public void saveEcosystem(Ecosystem ecosystem) throws SavingEcosystemException {
		try {
			FileUtils.writeStringToFile(new File(TRAMPOLINE_SETTINGS_PATH), new Gson().toJson(ecosystem));
		} catch (IOException e) {
			e.printStackTrace();
			throw new SavingEcosystemException();
		}
	}
	
	private void checkIfFileExistsAndCreatedIfNeeded() throws CreatingSettingsFolderException {
		try{
			File file = new File(TRAMPOLINE_FOLDER);
			if(!file.exists()){			
				file.mkdirs();
				FileUtils.writeStringToFile(new File(TRAMPOLINE_SETTINGS_PATH), new Gson().toJson(new Ecosystem()));
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new CreatingSettingsFolderException();
		}
	}

	public void runScript(String id, String mavenLocation, String port) throws RunningMicroserviceScriptException {
		try {
			new ProcessBuilder("sh", TRAMPOLINE_FOLDER + "/" + id + ".sh", mavenLocation, port).start();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RunningMicroserviceScriptException();
		}
	}

	public void createScript(String id, String pomLocation) throws CreatingMicroserviceScriptException {
		try {
			FileUtils.writeStringToFile(new File(TRAMPOLINE_FOLDER +"/"+ id +".sh"), "export M2_HOME=$1; export PATH=$PATH:$M2_HOME/bin; cd " + pomLocation + "; mvn spring-boot:run -Dserver.port=$2;");
		} catch (IOException e) {
			e.printStackTrace();
			throw new CreatingMicroserviceScriptException();
		}
	}
}
