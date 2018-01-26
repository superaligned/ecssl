package com.entrecloud.ecssl.acme;

import com.entrecloud.ecssl.configuration.Configuration;
import org.shredzone.acme4j.Registration;
import org.shredzone.acme4j.RegistrationBuilder;
import org.shredzone.acme4j.Session;
import org.shredzone.acme4j.exception.AcmeConflictException;
import org.shredzone.acme4j.exception.AcmeException;

import javax.annotation.ParametersAreNonnullByDefault;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
public class EnsureRegistration {
    public Registration registration(Configuration configuration, Session session) {
        String email = configuration.getOption("account-email").getValueAsString();

        Registration reg;
        try {
            RegistrationBuilder registrationBuilder = new RegistrationBuilder();
            registrationBuilder.addContact("mailto:" + email);
            reg = registrationBuilder.create(session);
        } catch (AcmeConflictException ex) {
            reg = Registration.bind(session, ex.getLocation());
            List<String> registeredEmails = reg
                .getContacts()
                .stream()
                .map(URI::toString)
                .map(registeredEmail -> registeredEmail.replaceAll("\\Amailto:", ""))
                .collect(Collectors.toList());
            if (!registeredEmails.contains(email)) {
                try {
                    reg.modify().addContact("mailto:" + email).commit();
                } catch (AcmeException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (AcmeException e) {
            throw new RuntimeException(e);
        }
        return reg;
    }
}
