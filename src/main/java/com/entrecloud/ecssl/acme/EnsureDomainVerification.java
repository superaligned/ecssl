package com.entrecloud.ecssl.acme;

import com.entrecloud.ecssl.configuration.Configuration;
import org.shredzone.acme4j.Registration;
import org.shredzone.acme4j.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class EnsureDomainVerification {
    private final static Logger logger = LoggerFactory.getLogger(EnsureDomainVerification.class);
    public void verify(Configuration configuration, Registration registration, Session session) {
        String[] domains = configuration.getOption("domains").getValueAsString().split(",");

        for (String domain : domains) {
            logger.info("Attempting to validate domain " + domain + "...");
            new HttpDomainVerification(domain, registration, configuration.getOption("webroot-path").getValueAsString()).run();
        }
    }
}
