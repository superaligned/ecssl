package com.entrecloud.ecssl.acme;

import com.entrecloud.ecssl.configuration.Configuration;
import org.shredzone.acme4j.util.CertificateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyPair;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@ParametersAreNonnullByDefault
public class CertificateNeedsRenew {
    private final static Logger logger = LoggerFactory.getLogger(CertificateNeedsRenew.class);
    public boolean needsRenew(Configuration configuration, KeyPair keyPair) {
        File certificateFile = configuration.getOption("certificate-file").getValueAsFile();
        logger.trace("Attempting to load certificate file " + certificateFile.toString() + "...");
        try {
            X509Certificate certificate = CertificateUtils.readX509Certificate(new FileInputStream(certificateFile));
            if (!((RSAPublicKey) certificate.getPublicKey()).getModulus().equals(((RSAPublicKey) keyPair.getPublic()).getModulus())) {
                logger.trace("Certificate does not match private key, regenerating...");
                //public keys don't match
                return true;
            }
            java.util.Date targetDate = Date.from(LocalDateTime.now().plus(30, ChronoUnit.DAYS).atZone(ZoneId.systemDefault()).toInstant());
            try {
                certificate.checkValidity(targetDate);
            } catch (CertificateExpiredException | CertificateNotYetValidException e) {
                logger.trace("Certificate will or has expired, regenerating...");
                return true;
            }
            return false;
        } catch (FileNotFoundException e) {
            logger.info("Certificate file not found, regenerating.");
            return true;
        } catch (IOException e) {
            logger.info("Exception while trying to load certificate file, regenerating.", e);
            return true;
        }
    }
}
