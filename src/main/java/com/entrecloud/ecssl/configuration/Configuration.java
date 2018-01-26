package com.entrecloud.ecssl.configuration;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
public class Configuration {
    private final Map<String, Option> options;

    public Configuration(List<Option> options) {
        this.options = options.stream().collect(Collectors.toMap(Option::getName, option->option));
    }

    public void parse(String[] argv) {
        @Nullable String option = null;
        for (int i = 0; i < argv.length; i++) {
            if (option == null) {
                if (!argv[i].startsWith("--")) {
                    throw new RuntimeException("Expected option name in position " + (i+1));
                }
                String optionName = argv[i].replaceAll("\\A--", "");
                if (!options.containsKey(optionName)) {
                    throw new RuntimeException("No such configuration option: " + argv[i]);
                }
                option = optionName;
            } else {
                String value = argv[i];
                options.compute(option, (optionName, optionObject) -> optionObject.withValue(value));
                option = null;
            }
        }
    }

    public Option getOption(String optionName) {
        if (options.containsKey(optionName)) {
            return options.get(optionName);
        } else {
            throw new RuntimeException("BUG: No such configuration option: --" + optionName);
        }
    }

    public String help() {
        StringBuilder help = new StringBuilder();
        help.append(
            "    __________________________\n" +
            "   /                          |\n" +
            "  /_______________________    |\n" +
            "                          |   |\n" +
            "____________________      |   |   _____________           __    ____________    __________      _____________       ________      __             _______       __         __    ___________\n" +
            "|                  /      |   |  |             |  |\\     |  |  |            |  |          \\    |             |     /        \\    |  |           /       \\     |  |       |  |  |           \\\n" +
            "|   ______________/  /|   |   |  |_____________|  | \\    |  |  |____    ____|  |   _____   \\   |_____________|    /  ______  \\   |  |          /   ___   \\    |  |       |  |  |   _______  \\\n" +
            "|   |               / |   |   |                   |  \\   |  |       |  |       |  |     \\  |                     /  /      \\__\\  |  |         /   /   \\   \\   |  |       |  |  |  |       \\  \\\n" +
            "|   |    __        |  |   |   |                   |   \\  |  |       |  |       |  |     |  |                    /  /             |  |        /   /     \\   \\  |  |       |  |  |  |        \\  \\\n" +
            "|   |   |  |       |  |   |   |   _____________   |    \\ |  |       |  |       |  |_____/  |    _____________   |  |             |  |        |  |       |  |  |  |       |  |  |  |        |  |\n" +
            "|   |   |  |       |  |   |   |  |             |  |     \\|  |       |  |       |           /   |             |  |  |             |  |        |  |       |  |  |  |       |  |  |  |        |  |\n" +
            "|   |   |  |       |  |   |   |  |_____________|  |  |\\     |       |  |       |  ___   __/    |_____________|  |  |             |  |        |  |       |  |  |  |       |  |  |  |        |  |\n" +
            "|   |   |  |       |__|   |   |                   |  | \\    |       |  |       |  |  \\  \\                       |  |             |  |        |  |       |  |  |  |       |  |  |  |        |  |\n" +
            "|   |   |  |              |   |                   |  |  \\   |       |  |       |  |   \\  \\                      \\  \\        __   |  |        \\   \\     /   /  \\   \\     /   /  |  |        /  /\n" +
            "|   |   | /  _____________|   |   _____________   |  |   \\  |       |  |       |  |    \\  \\     _____________    \\  \\______/  /  |  |______   \\   \\___/   /    \\   \\___/   /   |  |_______/  /\n" +
            "|   |   |/  /                 |  |             |  |  |    \\ |       |  |       |  |     \\  \\   |             |    \\          /   |         |   \\         /      \\         /    |            /\n" +
            "|   |      /__________________|  |_____________|  |__|     \\|       |__|       |__|      \\__\\  |_____________|     \\________/    |_________|    \\_______/        \\_______/     |___________/\n" +
            "|   |\n" +
            "|   |_____________________\n" +
            "|                        /\n" +
            "|_______________________/\n\n"
        );
        help.append("Usage: java -jar ecssl.jar OPTIONS\n\nOptions:\n");
        for (Option option : options.values()) {
            help.append(option.help()).append("\n");
        }
        help.append("\n")
            .append("ECSSL is free/open source software by Entrecloud under GPLv2 and contains libraries under various open source licenses.\n")
            .append("The source code and more information is available at https://github.com/entrecloud/ecssl\n")
            .append("\n");
        return help.toString();
    }
}
