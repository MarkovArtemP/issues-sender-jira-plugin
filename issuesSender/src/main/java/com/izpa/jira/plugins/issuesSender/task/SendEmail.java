package com.izpa.jira.plugins.issuesSender.task;

import com.atlassian.sal.api.scheduling.PluginJob;
import com.izpa.jira.plugins.issuesSender.dao.DAOFactory;
import org.apache.log4j.Logger;

import java.util.Map;

public class SendEmail implements  PluginJob{
  public void execute(Map<String, Object> map) {
    //TODO Delete this
    System.out.println("============================================In task!===================================");
    try {
      DAOFactory.getInstance().getTaskDAO().sendMail((Long) map.get("id"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
