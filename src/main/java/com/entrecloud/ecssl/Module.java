package com.entrecloud.ecssl;

import com.entrecloud.ecssl.configuration.Option;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public interface Module {
    List<Option> getOptions();

    @Nullable
    default Mode getMode() {
        return null;
    }
}
