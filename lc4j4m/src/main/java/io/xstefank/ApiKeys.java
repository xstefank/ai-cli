package io.xstefank;

import org.eclipse.microprofile.config.ConfigProvider;

public class ApiKeys {

    public static final String OPENAI_API_KEY = ConfigProvider.getConfig().getValue("openai.api.key", String.class);
    public static final String GEMINI_API_KEY = ConfigProvider.getConfig().getValue("gemini.api.key", String.class);
}
