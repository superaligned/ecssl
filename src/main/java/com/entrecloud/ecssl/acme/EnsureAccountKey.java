package com.entrecloud.ecssl.acme;

import com.entrecloud.ecssl.configuration.Configuration;
import org.shredzone.acme4j.util.KeyPairUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.*;
import java.security.KeyPair;

@ParametersAreNonnullByDefault
public class EnsureAccountKey {
    private final static Logger logger = LoggerFactory.getLogger(EnsureAccountKey.class);
    public KeyPair accountKey(Configuration configuration) {
        File accountKeyFile = configuration.getOption("account-key-file").getValueAsFile();
        logger.trace("Attempting to load ACME account key pair from " + accountKeyFile.toString() + "...");
        KeyPair userKeyPair;
        if (!accountKeyFile.exists()) {
            logger.trace("Account key does not exist, regenerating...");
            userKeyPair = KeyPairUtils.createKeyPair(2048);
            try {
                KeyPairUtils.writeKeyPair(userKeyPair, new FileWriter(accountKeyFile));
            } catch (IOException e) {
                logger.warn("Exception while trying to write key pair, aborting.", e);
                throw new RuntimeException(e.getMessage(), e);
            }
        } else {
            try {
                userKeyPair = KeyPairUtils.readKeyPair(new FileReader(accountKeyFile));
            } catch (IOException e) {
                logger.warn("Exception while trying to read key pair, aborting.", e);
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return userKeyPair;
    }
}
