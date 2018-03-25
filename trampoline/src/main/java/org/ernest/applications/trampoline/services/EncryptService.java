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
        standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        standardPBEStringEncryptor.setPassword("s32rwnj2341mWDaqw12");
    }


    public String encrypt(String value){
        return standardPBEStringEncryptor.encrypt(value);
    }

    public String decrypt(String value) {
        return standardPBEStringEncryptor.decrypt(value);
    }
}