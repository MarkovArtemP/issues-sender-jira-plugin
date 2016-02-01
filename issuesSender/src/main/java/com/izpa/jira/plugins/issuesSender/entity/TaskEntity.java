package com.izpa.jira.plugins.issuesSender.entity;

import com.izpa.jira.plugins.issuesSender.logic.Task;
import net.java.ao.Entity;

import java.util.Date;

public interface TaskEntity extends Task, Entity {
  public Date getLastSend();
  public void setLastSend(Date lastSend);

  public Date getNextSend();
  public void setNextSend(Date nextSend);
}
