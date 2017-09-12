package org.ernest.applications.trampoline.services.impl;

import org.ernest.applications.trampoline.entities.MicroserviceConfiguration;
import org.ernest.applications.trampoline.services.ConfigurationLoader;
import org.mockito.internal.util.collections.Sets;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ArrayUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

@Service
public class ConfigurationLoaderImpl implements ConfigurationLoader {

    private static final String DEFAULT_PROPERTIES_FOLDER = "/src/main/resources";
    private static final Set<String> CONFIGURATION_FILES = Sets.newSet(
            "application.properties", "application.yml", "boostrap.yml"
    );

    @Override
    public Optional<MicroserviceConfiguration> load(String root) {
        String[] list = new File(root + DEFAULT_PROPERTIES_FOLDER).list();
        if (ArrayUtils.isEmpty(list)) {
            return Optional.empty();
        }

        Optional<String> configurationFile = Arrays.stream(list)
                .filter(file -> CONFIGURATION_FILES.contains(file))
                .findFirst();

        // TODO: need to implement a parser for files with different extensions
        return Optional.of(new MicroserviceConfiguration());
    }
}
