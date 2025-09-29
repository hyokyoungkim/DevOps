package com.codeenator.utils;

import com.ithows.AppConfig;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 이메일 전송 Utils
 */
public class MailUtils {
    private static final String ADMINNAME = "CODEENATOR";
    private static final String ENCODING = "UTF-8";
    
    /**
     * 이메일 전송
     * @param toEmail       보낼 이메일
     * @param title         제목
     * @param content       내용
     * @return              전송 여부
     */
    public static boolean sendMail(String toEmail, String title, String content) {
        // 이메일 아이디, 비밀번호
        String id = AppConfig.getConf("admin_email_id");
        String password = AppConfig.getConf("admin_email_password");
        
        // SMTP 서버 정보를 설정한다.
        Properties props = new Properties();
        props.put("mail.smtp.host", "wsmtp.ecounterp.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true"); 
        props.put("mail.smtp.ssl.enable", "false");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.trust", "wsmtp.ecounterp.com");
        
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(id, password);
            }
        });
        
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(id, ADMINNAME));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail)); 
            
            // 메일 제목, 메일 내용
            message.setSubject(title);            
            message.setText(content, ENCODING, "html");

            Transport.send(message);                                            // 메일 전송        
        } catch (Exception e) {
            e.printStackTrace();
            
            return false;
        }
        
        return true;
    }
}
