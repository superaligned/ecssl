package com.entrecloud.ecssl.acme;

import com.entrecloud.ecssl.configuration.Configuration;
import org.shredzone.acme4j.util.KeyPairUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.*;
import java.security.KeyPair;

@ParametersAreNonnullByDefault
public class EnsureAccountKey {
    public KeyPair accountKey(Configuration configuration) {
        File accountKeyFile = configuration.getOption("account-key-file").getValueAsFile();
        KeyPair userKeyPair;
        if (!accountKeyFile.exists()) {
            userKeyPair = KeyPairUtils.createKeyPair(2048);
            try {
                KeyPairUtils.writeKeyPair(userKeyPair, new FileWriter(accountKeyFile));
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        } else {
            try {
                userKeyPair = KeyPairUtils.readKeyPair(new FileReader(accountKeyFile));
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return userKeyPair;
    }
}
