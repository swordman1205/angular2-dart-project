package com.qurasense.communication.rules;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class TypeProcessorFactory {

    private Logger logger = LoggerFactory.getLogger(TypeProcessorFactory.class);

    @Autowired
    private ApplicationContext applicationContext;

    private Map<String, String> processorBeanNames = new HashMap<>();

    @PostConstruct
    protected void init() {
        Map<String, TypeProcessor> beansOfType = applicationContext.getBeansOfType(TypeProcessor.class);
        for (Map.Entry<String, TypeProcessor> typeProcessor : beansOfType.entrySet()) {
            String supportType = typeProcessor.getValue().supportType();
            String beanName = typeProcessor.getKey();
            processorBeanNames.put(supportType, beanName);
        }
    }

    public TypeProcessor getProcessor(String type) {
        String beanName = processorBeanNames.get(type);
        if (Objects.nonNull(beanName)) {
            return applicationContext.getBean(beanName, TypeProcessor.class);
        }
        logger.warn("not found processor for type {}, using default", type);
        return applicationContext.getBean("defaultProcessor", DefaultProcessor.class);
    }

}
