package io.spring.mailsender.service;

import io.spring.mailsender.dto.MailRequestDto;
import io.spring.mailsender.mail.MailManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Service
@Slf4j
@RequiredArgsConstructor
public class  MessageService {

    private final MailManager mailManager;

    @Value("${spring.mail.username}")
    private String fromAddress;
    @Value("${spring.mail.properties.mail.smtp.nickname}")
    private String fromName;

    @Async(value = "mailSenderExecutor")
    public void mailSendWithAsync(MailRequestDto mailRequestDto, String fileName) {
        send(mailRequestDto, fileName);
    }

    public void mailSend(MailRequestDto mailRequestDto, String fileName) {
        send(mailRequestDto, fileName);
    }

    private void send(MailRequestDto mailRequestDto, String fileName) {
        String subject = mailRequestDto.getSubject();
        String text = mailRequestDto.getText();

        Context context = new Context();
        context.setVariable("subject", subject);
        context.setVariable("text", text);
        context.setVariable("from", fromAddress);

        for (String to : mailRequestDto.getTo()) {
            try {
                mailManager.setTo(to);
                mailManager.setSubject(subject);
                mailManager.setThymeleafText(context, fileName, true);
                mailManager.setFromName(fromAddress, fromName);

                mailManager.send();
            } catch (MailAuthenticationException e) {
                throw new IllegalArgumentException("계정 인증 실패");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
