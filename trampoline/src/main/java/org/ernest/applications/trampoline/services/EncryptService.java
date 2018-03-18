package org.ernest.applications.trampoline.services;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import javax.annotation.PostConstruct;

@Service
public class EncryptService {

    private StandardPBEStringEncryptor standardPBEStringEncryptor;

    @PostConstruct
    public void initializeEncrypt(){
       /* SystemInfo systemInfo = new SystemInfo();
        OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
        HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();
        CentralProcessor centralProcessor = hardwareAbstractionLayer.getProcessor();*/

        standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        standardPBEStringEncryptor.setPassword("s32rwnj2341mWDaqw12"/*new StringBuilder()
                                                    .append(operatingSystem.getManufacturer())
                                                    .append(centralProcessor.getSystemSerialNumber())
                                                    .append(centralProcessor.getIdentifier())
                                                    .append(centralProcessor.getLogicalProcessorCount())
                                                    .toString()*/);
    }


    public String encrypt(String value){
        return standardPBEStringEncryptor.encrypt(value);
    }

    public String decrypt(String value) {
        return standardPBEStringEncryptor.decrypt(value);
    }
}