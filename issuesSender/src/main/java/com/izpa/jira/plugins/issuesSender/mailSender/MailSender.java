package com.izpa.jira.plugins.issuesSender.mailSender;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.mail.Email;
import com.atlassian.mail.MailException;
import com.atlassian.mail.queue.SingleMailQueueItem;


import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MailSender {
  private static MailSender instance = null;
  private SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
  public static synchronized MailSender getInstance()throws Exception{
    if (instance == null) {
      instance = new MailSender();
    }
    return instance;
  }
  private MailSender(){}
  public void sendIssues(String address, File file) throws MessagingException, MailException {
    Email em = new Email(address);

    Date now = new Date();
    em.setSubject("Issues in progress from "+ dt.format(now));
    em.setBody(dt.format(now));

    em.setEncoding("text/plain");
    Multipart multipart = new MimeMultipart();
    BodyPart messageBodyPart= new MimeBodyPart();
    DataSource source = new FileDataSource(file);
    messageBodyPart.setDataHandler(new DataHandler(source));
    messageBodyPart.setFileName(file.getName());
    multipart.addBodyPart(messageBodyPart);
    em.setMultipart(multipart);

    em.setEncoding("text/plain");

    SingleMailQueueItem smqi = new SingleMailQueueItem(em);
    ComponentAccessor.getMailQueue().addItem(smqi);

    //MailFactory.getServerManager().getDefaultSMTPMailServer().send(em);
  }
}
