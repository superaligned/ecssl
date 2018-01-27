package com.entrecloud.ecssl;

import com.entrecloud.ecssl.configuration.Configuration;
import com.entrecloud.ecssl.configuration.Option;
import com.entrecloud.ecssl.single.SingleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ParametersAreNonnullByDefault
public class ECSSLApplication implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(ECSSLApplication.class);
    private final List<Module> modules;
    private final Configuration configuration;
    private final String[] argv;

    public ECSSLApplication(String[] argv) {
        this.argv = argv;
        modules = Arrays.asList(
            new SingleModule()
        );

        List<Option> options = new ArrayList<>();
        options.add(new Option("mode", "Set operation mode", "single"));
        options.add(new Option("log-level", "Set log level (trace, info, warn, error)", "info"));
        for (Module module : modules) {
            options.addAll(module.getOptions());
        }

        configuration = new Configuration(options);
    }

    @Override
    public void run() {
        if (argv.length == 0 || (argv.length == 1 && (argv[0].equals("-h") || argv[0].equals("--help")))) {
            System.out.println(configuration.help());
            return;
        }

        configuration.parse(argv);

        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", configuration.getOption("log-level").getValueAsString());

        String modeName = configuration.getOption("mode").getValueAsString();
        List<String> modes = new ArrayList<>();
        for (Module module : modules) {
            Mode mode = module.getMode();
            if (mode != null) {
                if (mode.getName().toLowerCase().equals(modeName.toLowerCase())) {
                    mode.run(configuration);
                    return;
                } else {
                    modes.add(mode.getName());
                }
            }
        }
        throw new Option.InvalidValue("mode", modeName, "one of " + String.join(", ", modes));
    }

    public static void main(String[] argv) {
        new ECSSLApplication(argv).run();
    }
}
