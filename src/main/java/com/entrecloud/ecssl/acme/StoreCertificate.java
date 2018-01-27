package com.entrecloud.ecssl.acme;

import com.entrecloud.ecssl.configuration.Configuration;
import org.shredzone.acme4j.Certificate;
import org.shredzone.acme4j.exception.AcmeException;
import org.shredzone.acme4j.util.CertificateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ParametersAreNonnullByDefault
public class StoreCertificate {
    private final static Logger logger = LoggerFactory.getLogger(StoreCertificate.class);
    public void store(Configuration configuration, Certificate certificate) {
        try {
            File caFile = configuration.getOption("ca-file").getValueAsFile();
            File certificateFile = configuration.getOption("certificate-file").getValueAsFile();
            File chainFile = configuration.getOption("chain-file").getValueAsFile();

            logger.info("Downloading certificate...");
            X509Certificate domainCert = certificate.download();
            logger.info("Downloading ca certificate chain...");
            X509Certificate[] caChain = certificate.downloadChain();

            logger.info("Storing certificate in " + certificateFile.toString() + "...");
            CertificateUtils.writeX509Certificate(domainCert, new FileWriter(certificateFile));
            logger.info("Storing CA certificate chain in " + caFile.toString() + "...");
            CertificateUtils.writeX509CertificateChain(new FileWriter(caFile), null, caChain);
            logger.info("Storing full chain in " + chainFile.toString() + "...");
            CertificateUtils.writeX509CertificateChain(new FileWriter(chainFile), domainCert, caChain);
        } catch (IOException e) {
            logger.warn("IO exception while storing certificate: " + e.getMessage(), e);
        } catch (AcmeException e) {
            logger.warn("ACME exception while obtaining certificate: " + e.getMessage(), e);
        }
    }
}
