package com.izpa.jira.plugins.issuesSender.task;

import java.util.Date;

public interface EmailSender {
  public void createSchedule(final long id, Date date);
  public void deleteSchedule(long id);
}
