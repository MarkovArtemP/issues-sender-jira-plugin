package com.izpa.jira.services.issuesSender;

import com.atlassian.configurable.ObjectConfiguration;
import com.atlassian.configurable.ObjectConfigurationException;
import com.atlassian.jira.service.AbstractService;
import com.izpa.jira.services.issuesSender.issues.IssuesCSVGetter;
import com.izpa.jira.services.issuesSender.mailSender.MailSender;
import com.opensymphony.module.propertyset.PropertySet;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class IssuesSenderService extends AbstractService {
  public static final String EMAIL = "Email";
  private InternetAddress email;

  @Override
  public void run() {
    if (email!=null){
      try {
        MailSender.getInstance().sendIssues(email.getAddress(), IssuesCSVGetter.getInstance().getIssues());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void init(PropertySet props) throws ObjectConfigurationException {
    super.init(props);
    if (hasProperty(EMAIL)) {
      try {
        email = new InternetAddress(getProperty(EMAIL));
      } catch (AddressException e) {
        email = null;
        e.printStackTrace();
      }
    }
    else email = null;
  }

  public ObjectConfiguration getObjectConfiguration() throws ObjectConfigurationException {
    return getObjectConfiguration("MYNEWSERVICE", "com/izpa/jira/services/issuesSender/configuration.xml", null);
  }
}
