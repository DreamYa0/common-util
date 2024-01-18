package com.g7.framwork.common.util.json;

import com.g7.framwork.common.util.json.adpter.JacksonAdapter;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

public class JsonPathUtils {

    private static final Configuration JACKSON_CONFIGURATION = Configuration.defaultConfiguration()
            .jsonProvider(new JacksonJsonProvider(JacksonAdapter.OBJECT_MAPPER))
            .mappingProvider(new JacksonMappingProvider(JacksonAdapter.OBJECT_MAPPER));

    /**
     * 找不到路径，则抛出异常
     * @return Configuration
     */
    public static Configuration def() {
        return JACKSON_CONFIGURATION.addOptions(Option.SUPPRESS_EXCEPTIONS);
    }

    /**
     * 遇到异常，则抛出异常
     * @return Configuration
     */
    public static Configuration throwEx() {
        return JACKSON_CONFIGURATION;
    }

    /**
     * 找不到路径，则返回 Null
     * @return Configuration
     */
    public static Configuration leafToNull() {
        return JACKSON_CONFIGURATION.addOptions(Option.SUPPRESS_EXCEPTIONS)
                .addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);
    }

    /**
     * 始终返回 List<T>
     * @return Configuration
     */
    public static Configuration alwaysCol() {
        return JACKSON_CONFIGURATION.addOptions(Option.SUPPRESS_EXCEPTIONS)
                .addOptions(Option.ALWAYS_RETURN_LIST);
    }
}
