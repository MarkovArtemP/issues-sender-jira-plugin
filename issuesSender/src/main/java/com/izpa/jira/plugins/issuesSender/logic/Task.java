package com.izpa.jira.plugins.issuesSender.logic;

import java.util.Date;

public interface Task {
  public String getEmail();
  public void setEmail(String email);

  public String getCron();
  public void setCron(String cron);
}
