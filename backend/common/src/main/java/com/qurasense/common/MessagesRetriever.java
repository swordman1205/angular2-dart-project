package com.qurasense.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;

public class MessagesRetriever {

    @Autowired
    private ApplicationContext applicationContext;

    public <E extends Enum<E>> String getCaption(E enumInstanse) {
        if (enumInstanse == null) {
            return StringUtils.EMPTY;
        }
        String code = String.format("%s.%s", enumInstanse.getClass().getName(), enumInstanse.name());
        return applicationContext.getMessage(code, null, code, LocaleContextHolder.getLocale());
    }

    public  String getMessage(String code) {
        return applicationContext.getMessage(code, null, code, LocaleContextHolder.getLocale());
    }

}
