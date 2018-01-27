package com.entrecloud.ecssl.single;

import com.entrecloud.ecssl.Mode;
import com.entrecloud.ecssl.Module;
import com.entrecloud.ecssl.configuration.Option;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ParametersAreNonnullByDefault
public class SingleModule implements Module {
    @Override
    public List<Option> getOptions() {
        return Arrays.asList(
            new Option("account-key-file", "The file where the LetsEncrypt/ACME account key is stored."),
            new Option("account-email", "E-mail address to register with the account. Will update the ACME account if changed."),
            new Option("endpoint", "ACME endpoint, use acme://letsencrypt.org/staging for testing", "acme://letsencrypt.org"),
            new Option("domains", "A list of domain names to generate certificates for."),
            new Option("certificate-file", "File to store the certificate in."),
            new Option("ca-file", "File to store the CA certificate in."),
            new Option("chain-file", "File to store the full certificate chain in."),
            new Option("privatekey-file", "File to store the private key in."),
            new Option("webroot-path", "Webroot for creating the verification file."),
            new Option("agree-terms-url", "Agree to the terms of service on the specified url.")
        );
    }

    @Nullable
    @Override
    public Mode getMode() {
        return new SingleMode();
    }
}
