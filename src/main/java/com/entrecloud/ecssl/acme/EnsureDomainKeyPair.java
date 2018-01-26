package com.entrecloud.ecssl.acme;

import com.entrecloud.ecssl.configuration.Configuration;
import org.shredzone.acme4j.util.KeyPairUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyPair;

@ParametersAreNonnullByDefault
public class EnsureDomainKeyPair {
    public KeyPair ensureKeyPair(Configuration configuration) {
        File privateKeyfile = configuration.getOption("privatekey-file").getValueAsFile();
        KeyPair domainKeyPair;
        if (!privateKeyfile.exists()) {
            domainKeyPair = KeyPairUtils.createKeyPair(2048);
            try {
                KeyPairUtils.writeKeyPair(domainKeyPair, new FileWriter(privateKeyfile));
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        } else {
            try {
                domainKeyPair = KeyPairUtils.readKeyPair(new FileReader(privateKeyfile));
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return domainKeyPair;
    }
}
