package cz.mail.email;

import java.io.UnsupportedEncodingException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class SendEmail {
   public static String myEmailAccount = "xxx@126.com";
   public static String myEmailPassword = "xxx";
   public static String myEmailSMTPHost = "smtp.126.com";
   public static String receiveMailAccount = "xxx@qq.com";

   public static void main(String[] args)  {
      Properties props = new Properties();
      props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
      props.setProperty("mail.host", myEmailSMTPHost);
      props.setProperty("mail.smtp.auth", "true");            // 请求认证，参数名称与具体实现有关
      Session session = Session.getDefaultInstance(props);
      session.setDebug(true);                                 // 设置为debug模式, 可以查看详细的发送 log
      Transport transport = null;
      try {
         MimeMessage message = createMimeMessage(session, myEmailAccount, receiveMailAccount);
         transport = session.getTransport();
         transport.connect(myEmailAccount, myEmailPassword);
         transport.sendMessage(message, message.getAllRecipients());
      } catch (NoSuchProviderException e) {
         e.printStackTrace();
      } catch (MessagingException e) {
         e.printStackTrace();
      } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
      } finally {
         if(null!=transport){
            try {
               transport.close();
            } catch (MessagingException e) {
               e.printStackTrace();
            }
         }
      }
   }

   /**
    * 创建一封只包含文本的简单邮件
    * @param session 和服务器交互的会话
    * @param sendMail 发件人邮箱
    * @param receiveMail 收件人邮箱
    * @return
    * @throws Exception
    */
   public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail) throws MessagingException, UnsupportedEncodingException {
      MimeMessage message = new MimeMessage(session);
      message.setFrom(new InternetAddress(sendMail, "某宝网", "UTF-8"));
      message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, "XX用户", "UTF-8"));
      message.setSubject("打折钜惠", "UTF-8");
      message.setContent("XX用户你好, 今天全场5折, 快来抢购, 错过今天再等一年。。。", "text/html;charset=UTF-8");
      message.setSentDate(new Date());
      message.saveChanges();
      return message;
   }

}