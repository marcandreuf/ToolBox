package org.mandfer.tools.email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

/**
 * @author marcandreuf
 */
//TODO: Clean up and use guice to inject dependencies.
public class JMailer {

    private String smtpUser = "marcandreuf@gmail.com";
    private String smtpPass = "";

    private final String ATTACHMENT_IMAGE = "<image>";

    public void send(String emailReceipient, String emailSubject,
                     String emailBody) {
        send(emailReceipient, emailSubject, emailBody, null);
    }

    public void send(String emailReceipient, String emailSubject,
                     String emailBody, MailAttachment attachment) {
        Properties props = new Properties();

//		props.put("mail.smtp.auth", "true");
//		props.put("mail.smtp.starttls.enable", "true");
//		props.put("mail.smtp.host", "smtp.gmail.com");
//		props.put("mail.smtp.port", "587");

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(smtpUser, smtpPass);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(smtpUser));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(emailReceipient));
            message.setSubject(emailSubject);

            MimeMultipart multipart = new MimeMultipart("related");
            BodyPart htmlBodyPart = new MimeBodyPart();
            htmlBodyPart.setContent(emailBody, "text/html; charset=utf-8");
            multipart.addBodyPart(htmlBodyPart);
            message.setContent(multipart);


//			if (attachment != null) { 
//				multipart.addBodyPart(messageBodyPart);
//				messageBodyPart = new MimeBodyPart();
//				DataSource fds = new FileDataSource(attachment.getFilename());
//				messageBodyPart.setDataHandler(new DataHandler(fds));
//				messageBodyPart.setHeader("Content-ID", attachment.getFileType());
//				multipart.addBodyPart(messageBodyPart);
//				message.setContent(multipart);
//			}

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
