package org.ernest.applications.trampoline.services;

import org.ernest.applications.trampoline.entities.MicroserviceConfiguration;

import java.util.Optional;

public interface ConfigurationLoader {
    Optional<MicroserviceConfiguration> load(String root);
}
