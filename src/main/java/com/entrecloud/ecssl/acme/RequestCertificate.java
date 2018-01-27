package com.entrecloud.ecssl.acme;

import com.entrecloud.ecssl.configuration.Configuration;
import org.shredzone.acme4j.Certificate;
import org.shredzone.acme4j.Registration;
import org.shredzone.acme4j.exception.AcmeException;
import org.shredzone.acme4j.util.CSRBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;

@ParametersAreNonnullByDefault
public class RequestCertificate {
    private final static Logger logger = LoggerFactory.getLogger(RequestCertificate.class);

    public Certificate request(Configuration configuration, Registration registration, KeyPair domainKeyPair) {
        String[] domains = configuration.getOption("domains").getValueAsString().split(",");

        try {
            logger.trace("Requesting certificate...");
            CSRBuilder csrb = new CSRBuilder();
            csrb.addDomains(Arrays.asList(domains));
            csrb.sign(domainKeyPair);

            return registration.requestCertificate(csrb.getEncoded());
        } catch (AcmeException|IOException e) {
            logger.warn("Failed to request certificate: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
