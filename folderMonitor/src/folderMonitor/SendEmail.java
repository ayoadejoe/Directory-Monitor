package folderMonitor;

import java.util.Properties;  
import javax.mail.*;  
import javax.mail.internet.*;  
  
public class SendEmail {  
 public SendEmail(String toEmail, String domain, String fromEmail, String fromPassword, String text, String who) {  

    System.out.println("About to send an Email....");
    String cc="admin@iq-joy.com";
  
   //Get the session object  
   Properties props = new Properties();  
   props.put("mail.smtp.host",domain);  
   props.put("mail.smtp.auth", "true");  
     
   Session session = Session.getDefaultInstance(props,  
    new javax.mail.Authenticator() {  
      protected PasswordAuthentication getPasswordAuthentication() {  
    return new PasswordAuthentication(fromEmail,fromPassword);  
      }  
    });  
  
   //Compose the message  
    try {  
     MimeMessage message = new MimeMessage(session);  
     message.setFrom(new InternetAddress(fromEmail));  
     message.addRecipient(Message.RecipientType.TO,new InternetAddress(toEmail));  
     message.addRecipient(Message.RecipientType.BCC,new InternetAddress(cc));  
     message.setSubject("DIRECTORY MONITOR INFO FROM: "+who);  
     message.setText(text);  
       
    //send the message  
     Transport.send(message);  
  
     System.out.println("message sent successfully...");  
   
     } catch (MessagingException e) {e.printStackTrace();}  
 }  
}  