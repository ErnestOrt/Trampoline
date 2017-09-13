package org.ernest.applications.trampoline.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.ernest.applications.trampoline.entities.MicroserviceConfiguration;
import org.ernest.applications.trampoline.services.ConfigurationLoader;
import org.mockito.internal.util.collections.Sets;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ArrayUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static org.ernest.applications.trampoline.entities.FileExtension.YML;

@Service
public class ConfigurationLoaderImpl implements ConfigurationLoader {
    private static final Logger logger = Logger.getLogger(ConfigurationLoaderImpl.class);
    private static final String DEFAULT_PROPERTIES_FOLDER = "/src/main/resources";
    private static final Set<String> CONFIGURATION_FILES = Sets.newSet(
            "application.yml", "bootstrap.yml"
    );

    private ObjectMapper mapper;

    @PostConstruct
    public void init() {
        mapper = new ObjectMapper(new YAMLFactory());
    }

    @Override
    public Optional<MicroserviceConfiguration> load(String root) {
        String resources = root + DEFAULT_PROPERTIES_FOLDER;
        String[] list = new File(resources).list();
        if (ArrayUtils.isEmpty(list)) {
            return Optional.empty();
        }

        Optional<String> configurationFile = Arrays.stream(list)
                .filter(CONFIGURATION_FILES::contains)
                .findFirst();
        return configurationFile.flatMap(file -> parseMicroserviceConfiguration(resources, file));
    }

    private Optional<MicroserviceConfiguration> parseMicroserviceConfiguration(String resourcesPath, String resources) {
        String extension = FilenameUtils.getExtension(resources);
        if (YML.getValue().equals(extension)) {
            String path = resourcesPath + "/" + resources;
            return parseYml(path);
        }
        return Optional.empty();
    }


    private Optional<MicroserviceConfiguration> parseYml(String path) {
        try {
            MicroserviceConfiguration configuration = mapper.readValue(new File(path), MicroserviceConfiguration.class);
            return Optional.of(configuration);
        } catch (Exception e) {
            logger.error("Error occurred during parsing 'yml' properties file with path=" + path, e);
        }
        return Optional.empty();
    }

}
