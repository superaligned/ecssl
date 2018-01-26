package com.entrecloud.ecssl.acme;

import com.entrecloud.ecssl.configuration.Configuration;
import org.shredzone.acme4j.Session;

import javax.annotation.ParametersAreNonnullByDefault;
import java.security.KeyPair;

@ParametersAreNonnullByDefault
public class EnsureSession {
    public Session session(Configuration configuration, KeyPair userKeyPair) {
        return new Session(configuration.getOption("endpoint").getValueAsString(), userKeyPair);
    }
}
