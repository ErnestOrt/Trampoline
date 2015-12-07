package org.ernest.applications.trampoline.services;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.ernest.applications.trampoline.entities.Ecosystem;
import org.ernest.applications.trampoline.entities.Microservice;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

@Component
public class FileManager {

	public Ecosystem getEcosystem(String workingDirectory) throws IOException {
		checkIfFileExistsAndCreatedIfNeeded(workingDirectory);
		
		return new Gson().fromJson(FileUtils.readFileToString(new File(workingDirectory + "/trampoline/settings.txt")), Ecosystem.class);
	}

	private void checkIfFileExistsAndCreatedIfNeeded(String workingDirectory) throws IOException {
		File file = new File(workingDirectory + "/trampoline");
		if(!file.exists()){			
			file.mkdirs();
			FileUtils.writeStringToFile(new File(workingDirectory + "/trampoline/settings.txt"), new Gson().toJson(new Ecosystem()));
		}
	}

	public void setNewMicroservice(String workingDirectory, Microservice microservice) throws JsonSyntaxException, IOException {
		Ecosystem ecosystem = new Gson().fromJson(FileUtils.readFileToString(new File(workingDirectory + "/trampoline/settings.txt")), Ecosystem.class);
		ecosystem.getMicroservices().add(microservice);
		
		FileUtils.writeStringToFile(new File(workingDirectory + "/trampoline/settings.txt"), new Gson().toJson(ecosystem));
	}
}
