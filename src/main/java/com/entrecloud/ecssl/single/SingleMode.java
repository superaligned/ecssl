package com.entrecloud.ecssl.single;

import com.entrecloud.ecssl.Mode;
import com.entrecloud.ecssl.acme.*;
import com.entrecloud.ecssl.configuration.Configuration;
import org.shredzone.acme4j.Certificate;
import org.shredzone.acme4j.Registration;
import org.shredzone.acme4j.Session;
import org.shredzone.acme4j.util.CSRBuilder;

import javax.annotation.ParametersAreNonnullByDefault;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;

@ParametersAreNonnullByDefault
public class SingleMode implements Mode {
    @Override
    public String getName() {
        return "single";
    }

    @Override
    public void run(Configuration configuration) {
        KeyPair accountKey = new EnsureAccountKey().accountKey(configuration);
        KeyPair domainKeyPair = new EnsureDomainKeyPair().ensureKeyPair(configuration);
        if (!new CertificateNeedsRenew().needsRenew(configuration, domainKeyPair)) {
            return;
        }
        Session session = new EnsureSession().session(configuration, accountKey);
        Registration registration = new EnsureRegistration().registration(configuration, session);

        new EnsureDomainVerification().verify(configuration, registration, session);

        Certificate certificate = new RequestCertificate().request(configuration, registration, domainKeyPair);

        new StoreCertificate().store(configuration, certificate);
    }
}
