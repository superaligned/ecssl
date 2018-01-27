package com.entrecloud.ecssl.single;

import com.entrecloud.ecssl.Mode;
import com.entrecloud.ecssl.acme.*;
import com.entrecloud.ecssl.configuration.Configuration;
import org.shredzone.acme4j.Certificate;
import org.shredzone.acme4j.Registration;
import org.shredzone.acme4j.Session;
import org.shredzone.acme4j.util.CSRBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;

@ParametersAreNonnullByDefault
public class SingleMode implements Mode {
    private final static Logger logger = LoggerFactory.getLogger(SingleMode.class);

    @Override
    public String getName() {
        return "single";
    }

    @Override
    public void run(Configuration configuration) {
        logger.info("Creating or loading domain private key...");
        KeyPair domainKeyPair = new EnsureDomainKeyPair().ensureKeyPair(configuration);
        logger.info("Creating or loading certificate...");
        if (!new CertificateNeedsRenew().needsRenew(configuration, domainKeyPair)) {
            logger.info("Certificate does not need a renew, exiting.");
            return;
        }
        logger.info("Creating or loading account key and establishing registration...");
        KeyPair accountKey = new EnsureAccountKey().accountKey(configuration);
        Session session = new EnsureSession().session(configuration, accountKey);
        Registration registration = new EnsureRegistration().registration(configuration, session);

        logger.info("Validating domain...");
        new EnsureDomainVerification().verify(configuration, registration, session);

        logger.info("Requesting and storing certificate...");
        Certificate certificate = new RequestCertificate().request(configuration, registration, domainKeyPair);
        new StoreCertificate().store(configuration, certificate);
    }
}
