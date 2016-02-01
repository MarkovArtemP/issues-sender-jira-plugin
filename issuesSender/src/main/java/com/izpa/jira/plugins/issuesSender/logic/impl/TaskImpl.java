package com.izpa.jira.plugins.issuesSender.logic.impl;

import com.izpa.jira.plugins.issuesSender.logic.Task;

import java.util.Date;

public class TaskImpl implements Task {
  private String email;
  private String cron;
  private Date lastSend;
  private Date nextSend;

  public TaskImpl (String email, String cron){
    this.setEmail(email);
    this.setCron(cron);
  }

  public String getEmail() {
      return email;
  }
  public void setEmail(String email) {
      this.email=email;
  }

  public String getCron() {
      return cron;
  }
  public void setCron(String cron) {
      this.cron = cron;
  }
  }
