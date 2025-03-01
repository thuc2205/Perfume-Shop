package com.example.thucbashop.components;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@RequiredArgsConstructor
@Component
public class LocalizationUtils {
    private final LocaleResolver localeResolver;
    private final MessageSource messageSource;
    public String getLocalizationUtils(String messageKey,Object ... params){
        HttpServletRequest request = WebUtils.getCurrentRequest();
        Locale locale = localeResolver.resolveLocale(request);
        return messageSource.getMessage(messageKey,params,locale);
    }
}
