package com.izpa.jira.plugins.issuesSender.schedule.task;

import com.atlassian.sal.api.scheduling.PluginJob;
import com.izpa.jira.plugins.issuesSender.dao.DAOFactory;

import java.util.Map;

public class SendEmail implements  PluginJob{
  //TODO добавить логирование
  public void execute(Map<String, Object> map) {
    try {
      DAOFactory.getInstance().getTaskDAO().sendMail((Long) map.get("id"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
