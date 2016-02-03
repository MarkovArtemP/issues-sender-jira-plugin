package com.izpa.jira.plugins.issuesSender.rest.xml;

import com.izpa.jira.plugins.issuesSender.entity.TaskEntity;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Mapper {
  private static SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
  public static XmlTask toXmlTask(TaskEntity entity) {
    XmlTask task = new XmlTask();
    task.id = entity.getID();
    task.email = entity.getEmail();
    task.cron = entity.getCron();
    task.lastSend=entity.getLastSend()==null?null:dt.format(entity.getLastSend());
    task.nextSend=entity.getNextSend()==null?null:dt.format(entity.getNextSend());
    //TODO добавить дату пропущенного отправления
    return task;
  }
}

