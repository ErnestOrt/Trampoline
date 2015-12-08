package org.ernest.applications.trampoline.services;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.ernest.applications.trampoline.entities.Ecosystem;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class FileManager {

	private static final String TRAMPOLINE_FOLDER = "/Users/"+System.getProperties().getProperty("user.name")+"/Documents/trampoline";
	private static final String TRAMPOLINE_SETTINGS_PATH = TRAMPOLINE_FOLDER + "/settings.txt";
	
	public Ecosystem getEcosystem() throws IOException {
		checkIfFileExistsAndCreatedIfNeeded();
		return new Gson().fromJson(FileUtils.readFileToString(new File(TRAMPOLINE_SETTINGS_PATH)), Ecosystem.class);
	}
	
	public void saveEcosystem(Ecosystem ecosystem) throws IOException {
		FileUtils.writeStringToFile(new File(TRAMPOLINE_SETTINGS_PATH), new Gson().toJson(ecosystem));
	}
	
	private void checkIfFileExistsAndCreatedIfNeeded() throws IOException {
		File file = new File(TRAMPOLINE_FOLDER);
		if(!file.exists()){			
			file.mkdirs();
			FileUtils.writeStringToFile(new File(TRAMPOLINE_SETTINGS_PATH), new Gson().toJson(new Ecosystem()));
		}
	}

	public void runScript(String id, String mavenLocation, String port) throws IOException {
		new ProcessBuilder("sh", TRAMPOLINE_FOLDER + "/" + id + ".sh", mavenLocation, port).start();
	}

	public void createScript(String id, String pomLocation) throws IOException {
		FileUtils.writeStringToFile(new File(TRAMPOLINE_FOLDER +"/"+ id +".sh"), "export M2_HOME=$1; export PATH=$PATH:$M2_HOME/bin; cd " + pomLocation + "; mvn spring-boot:run -Dserver.port=$2;");
	}
}
