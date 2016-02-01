package com.izpa.jira.plugins.issuesSender.rest.xml;

import com.izpa.jira.plugins.issuesSender.entity.TaskEntity;

public class Mapper {
  public static XmlTask toXmlTask(TaskEntity entity) {
    XmlTask task = new XmlTask();
    task.id = entity.getID();
    task.email = entity.getEmail();
    task.cron = entity.getCron();
    return task;
  }
}

