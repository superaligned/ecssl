package com.entrecloud.ecssl.acme;

import com.entrecloud.ecssl.configuration.Configuration;
import org.shredzone.acme4j.Registration;
import org.shredzone.acme4j.Session;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class EnsureDomainVerification {
    public void verify(Configuration configuration, Registration registration, Session session) {
        String[] domains = configuration.getOption("domains").getValueAsString().split(",");

        for (String domain : domains) {
            new HttpDomainVerification(domain, registration, configuration.getOption("webroot-path").getValueAsString()).run();
        }
    }
}
