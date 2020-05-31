package space.technological.modules.auth;

import com.sun.mail.smtp.SMTPTransport;
import org.apache.commons.io.FileUtils;
import space.technological.Main;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class Mailer {
    public static void sendVerificationEmail(String verification_key, String target_email, String username) throws IOException {
        String mail_content = FileUtils.readFileToString(new File("./emails/registration")).replaceAll("<verkey>", verification_key).replaceAll("<username>", username);
        String SMTP_SERVER = Main.config.mailHost;
        String USERNAME = Main.config.mailUsername;
        String PASSWORD = Main.config.mailPassword;

        String EMAIL_FROM = Main.config.mailUsername;
        String EMAIL_TO = target_email;
        String EMAIL_TO_CC = "";

        String EMAIL_SUBJECT = Main.config.mailSubject;
        String EMAIL_TEXT = mail_content;

        Properties properties = System.getProperties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", SMTP_SERVER);
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, null);
        Message msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(EMAIL_FROM, "Asmodeus"));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(EMAIL_TO, false));
            msg.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(EMAIL_TO_CC, false));
            msg.setSubject(EMAIL_SUBJECT);
            msg.setContent(EMAIL_TEXT, "text/html");
            msg.setSentDate(new Date());

            SMTPTransport t = (SMTPTransport) session.getTransport("smtp");
            t.connect(SMTP_SERVER, USERNAME, PASSWORD);
            t.sendMessage(msg, msg.getAllRecipients());
            t.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
