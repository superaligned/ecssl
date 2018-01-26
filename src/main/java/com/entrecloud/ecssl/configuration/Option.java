package com.entrecloud.ecssl.configuration;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.security.InvalidParameterException;

@ParametersAreNonnullByDefault
public class Option {
    private final String name;
    private final String description;
    @Nullable
    private final String defaultValue;
    @Nullable
    private final String value;

    public Option(
        String name,
        String description
    ) {
        this.name = name;
        this.description = description;
        this.defaultValue = null;
        value = null;
    }

    public Option(
        String name,
        String description,
        @Nullable String defaultValue
    ) {
        this.name = name;
        this.description = description;
        this.defaultValue = defaultValue;
        value = null;
    }

    private Option(
        String name,
        String description,
        @Nullable String defaultValue,
        @Nullable String value
    ) {

        this.name = name;
        this.description = description;
        this.defaultValue = defaultValue;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Option withValue(String value) {
        return new Option(
            name,
            description,
            defaultValue,
            value
        );
    }

    public String getValueAsString() {
        if (value != null) {
            return value;
        }
        if (defaultValue != null) {
            return defaultValue;
        }
        throw new ConfigurationOptionNotSet(name);
    }

    public boolean getValueAsBoolean() {
        String value = getValueAsString();
        switch (value.toLowerCase()) {
            case "on":
            case "yes":
            case "true":
            case "1":
                return true;
            case "off":
            case "no":
            case "false":
            case "0":
                return false;
            default:
                throw new InvalidValue(name, value, "one of on, yes, true, 1, off, no, false, 0");
        }
    }

    public int getValueAsInteger() {
        String value = getValueAsString();
        try {
            return Integer.parseInt(value);
        } catch (InvalidParameterException e) {
            throw new InvalidValue(name, value, "an integer");
        }
    }

    public int getValueAsPositiveInteger() {
        String value = getValueAsString();
        try {
            int intValue = Integer.parseInt(value);
            if (intValue < 0) {
                throw new InvalidValue(name, value, "a positive integer");
            }
            return intValue;
        } catch (InvalidParameterException e) {
            throw new InvalidValue(name, value, "a positive integer");
        }
    }

    public File getValueAsFile() {
        String value = getValueAsString();
        try {
            return new File(value);
        } catch (Exception e) {
            throw new InvalidValue(name, value, "a file name");
        }
    }

    public String help() {
        return "  --" + name + "  " + description + (defaultValue != null?" (default: " + defaultValue + ")":"");
    }

    public class ConfigurationOptionNotSet extends RuntimeException {
        public ConfigurationOptionNotSet(String option) {
            super("Configuration option required but not set: --" + option);
        }
    }

    public static class InvalidValue extends RuntimeException {
        public InvalidValue(String option, String value, String expected) {
            super("Invalid configuration option value: --" + option + " " + value + ", expected " + expected);
        }
    }
}
