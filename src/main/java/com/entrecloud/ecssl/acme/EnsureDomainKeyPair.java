package com.entrecloud.ecssl.acme;

import com.entrecloud.ecssl.configuration.Configuration;
import org.shredzone.acme4j.util.KeyPairUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyPair;

@ParametersAreNonnullByDefault
public class EnsureDomainKeyPair {
    private final static Logger logger = LoggerFactory.getLogger(EnsureDomainKeyPair.class);

    public KeyPair ensureKeyPair(Configuration configuration) {
        File privateKeyfile = configuration.getOption("privatekey-file").getValueAsFile();
        logger.trace("Loading domain private key from " + privateKeyfile.toString() + "...");
        KeyPair domainKeyPair;
        if (!privateKeyfile.exists()) {
            logger.trace("Private key file does not exist, regenerating...");
            domainKeyPair = KeyPairUtils.createKeyPair(2048);
            try {
                KeyPairUtils.writeKeyPair(domainKeyPair, new FileWriter(privateKeyfile));
            } catch (IOException e) {
                logger.warn("Exception while trying to write domain private key, aborting...", e);
                throw new RuntimeException(e.getMessage(), e);
            }
        } else {
            try {
                domainKeyPair = KeyPairUtils.readKeyPair(new FileReader(privateKeyfile));
            } catch (IOException e) {
                logger.warn("Exception while trying to read domain private key, aborting...", e);
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return domainKeyPair;
    }
}
