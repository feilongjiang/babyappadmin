package com.example.happybaby.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 读取resource下的.properties文件，将文件中的内容封装到map中，注入到bean中方便依赖注入
 *
 * @author Administrator
 */
@Configuration
public class CommonConfig {


    private Logger logger = LoggerFactory.getLogger(CommonConfig.class);

    private Map<String, String> commonProperties = new HashMap<>(16);

    private void init(String name) {

        try {
            Properties properties = new Properties();

            InputStream in = CommonConfig.class.getClassLoader().getResourceAsStream(name + ".properties");

            properties.load(in);

            logger.info("加载{}.properties参数", name);

            for (String keyName : properties.stringPropertyNames()) {
                String value = properties.getProperty(keyName);

                commonProperties.put(keyName, value);

                logger.info("{}.properties---------key:{},value:{}", name, keyName, value);
            }
            logger.info("{}.properties参数加载完毕", name);
        } catch (IOException ignored) {

        }

    }

    @Bean(name = "commonProperties")
    public Map<String, String> commonMap() {
        init("common");
        return commonProperties;
    }

}
