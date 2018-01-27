package com.entrecloud.ecssl;

import com.entrecloud.ecssl.configuration.Configuration;
import com.entrecloud.ecssl.configuration.Option;
import com.entrecloud.ecssl.single.SingleModule;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;
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
        try {
            if (argv.length == 0 || (argv.length == 1 && (argv[0].equals("-h") || argv[0].equals("--help")))) {
                System.out.println(configuration.help());
                return;
            }

            configuration.parse(argv);

            org.apache.log4j.Logger logger4j = org.apache.log4j.Logger.getRootLogger();
            logger4j.addAppender(new ConsoleAppender(new PatternLayout("[%p] %C{1}: %m%n"), ConsoleAppender.SYSTEM_OUT));
            logger4j.setLevel(org.apache.log4j.Level.toLevel(configuration.getOption("log-level").getValueAsString().toUpperCase()));

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
        } catch (Option.ConfigurationOptionNotSet e) {
            System.err.println(e.getMessage());
            System.out.println(configuration.help());
            System.exit(1);
        } catch (Throwable e) {
            logger.error("Unchaught " + e.getClass().getSimpleName() + " with message " + e.getMessage(), e);
            System.exit(1);
        }
    }

    public static void main(String[] argv) {
        new ECSSLApplication(argv).run();
    }
}
