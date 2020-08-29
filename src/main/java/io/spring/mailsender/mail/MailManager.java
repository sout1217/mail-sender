package io.spring.mailsender.mail;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Component
public class MailManager {

    private final JavaMailSender sender;
    private MimeMessage message;
    private MimeMessageHelper messageHelper;
    private final ThymeleafTemplateMapper thymeleafTemplateMapper;

    // 생성자
    public MailManager(JavaMailSender jSender, ThymeleafTemplateMapper thymeleafTemplateMapper) throws MessagingException {
        this.sender = jSender;
        message = jSender.createMimeMessage();
        messageHelper = new MimeMessageHelper(message, true, "UTF-8");
        this.thymeleafTemplateMapper = thymeleafTemplateMapper;
    }

    // 보내는 사람 이메일
    public void setFrom(String fromAddress) throws MessagingException {
        messageHelper.setFrom(fromAddress);
    }

    // 보내는 사람 이름
    public void setFromName(String fromAddress, String fromName) throws MessagingException, UnsupportedEncodingException {
        messageHelper.setFrom(fromAddress, fromName);
    }

    // 받는 사람 이메일
    public void setTo(String email) throws MessagingException {
        messageHelper.setTo(email);
    }

    // 제목
    public void setSubject(String subject) throws MessagingException {
        messageHelper.setSubject(subject);
    }

    // 메일 내용
    public void setText(String text, boolean useHtml) throws MessagingException {
        messageHelper.setText(text, useHtml);
    }

    // 메일 내용
    public void setThymeleafText(Context context, String fileName, boolean useHtml) throws MessagingException {

        String resultText = thymeleafTemplateMapper.parse(context, fileName);
        System.out.println(resultText);

        messageHelper.setText(resultText, useHtml);
    }

    // 첨부 파일
    public void setAttach(String displayFileName, String pathToAttachment) throws MessagingException, IOException {
        File file = new ClassPathResource(pathToAttachment).getFile();
        FileSystemResource fsr = new FileSystemResource(file);

        messageHelper.addAttachment(displayFileName, fsr);
    }

    // 이미지 삽입
    public void setInline(String contentId, String pathToInline) throws MessagingException, IOException {
        File file = new ClassPathResource(pathToInline).getFile();
        FileSystemResource fsr = new FileSystemResource(file);

        messageHelper.addInline(contentId, fsr);
    }

    // 발송
    public void send() {
        try {
            sender.send(message);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}