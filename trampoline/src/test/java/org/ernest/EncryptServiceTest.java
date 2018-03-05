package org.ernest;

import org.ernest.applications.trampoline.services.EncryptService;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public class EncryptServiceTest {

    @Test
    public void givenValueWhenEncryptAndDecryptThenSameValueIsObtained() throws Exception {
        String value = UUID.randomUUID().toString();

        EncryptService encryptService = new EncryptService();
        encryptService.initializeEncrypt();

        String encryptValue = encryptService.encrypt(value);

        Assert.assertNotEquals(value, encryptValue);
        Assert.assertEquals(value, encryptService.decrypt(encryptValue));

        encryptService.initializeEncrypt();
        Assert.assertEquals(value, encryptService.decrypt(encryptValue));
    }
}