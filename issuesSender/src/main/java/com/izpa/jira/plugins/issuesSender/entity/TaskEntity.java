package com.izpa.jira.plugins.issuesSender.entity;

import com.izpa.jira.plugins.issuesSender.logic.Task;
import net.java.ao.Entity;
import net.java.ao.Preload;

import java.util.Date;

@Preload
public interface TaskEntity extends Task, Entity {
  public Date getLastSend();
  public void setLastSend(Date lastSend);

  public Date getNextSend();
  public void setNextSend(Date nextSend);
}
