package com.entrecloud.ecssl;

import com.entrecloud.ecssl.configuration.Configuration;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface Mode {
    String getName();
    void run(Configuration configuration);
}
