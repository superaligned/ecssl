package com.entrecloud.ecssl.acme;

import com.entrecloud.ecssl.configuration.Configuration;
import org.shredzone.acme4j.util.CertificateUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.FileInputStream;
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
    public boolean needsRenew(Configuration configuration, KeyPair keyPair) {
        File certificateFile = configuration.getOption("certificate-file").getValueAsFile();
        try {
            X509Certificate certificate = CertificateUtils.readX509Certificate(new FileInputStream(certificateFile));
            if (!((RSAPublicKey)certificate.getPublicKey()).getModulus().equals(((RSAPublicKey)keyPair.getPublic()).getModulus())) {
                //public keys don't match
                return true;
            }
            java.util.Date targetDate = Date.from(LocalDateTime.now().plus(30, ChronoUnit.DAYS).atZone(ZoneId.systemDefault()).toInstant());
            try {
                certificate.checkValidity(targetDate);
            } catch (CertificateExpiredException | CertificateNotYetValidException e) {
                return true;
            }
            return false;
        } catch (IOException e) {
            return true;
        }
    }
}
