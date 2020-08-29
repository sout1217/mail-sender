package io.spring.mailsender.controller;

import io.spring.mailsender.dto.MailRequestDto;
import io.spring.mailsender.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/")
    public String messageForm() {

        info("[GET] Success messageForm()");

        return "main";
    }

    @PostMapping("/mail")
    public String messageSend(
            final MailRequestDto mailRequestDto,
            @RequestParam(name = "fileName", required = false) String fileName,
            @RequestParam(name = "async", defaultValue = "false") boolean async ) {

        info("[POST] Success messageSend()");
        log.info("mailRequestDto -> {}", mailRequestDto);
        log.info("fileName -> {}", fileName);
        log.info("async -> {}", async);

        if (!async)
            messageService.mailSend(mailRequestDto, fileName);
        else
            messageService.mailSendWithAsync(mailRequestDto, fileName);

        return "redirect:/";
    }

    private void info(String logMessage) {
        log.info(logMessage);
    }
}
