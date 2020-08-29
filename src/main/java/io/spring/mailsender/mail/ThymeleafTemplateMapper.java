package io.spring.mailsender.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class ThymeleafTemplateMapper {

    private final TemplateEngine templateEngine;

    public String parse(Context context, String fileName) {
        return templateEngine.process(fileName, context);
    }
}
