package com.izpa.jira.plugins.issuesSender.schedule;

import java.util.Date;

public interface Scheduler {
  public void createSchedule(final long id, Date date);
  public void deleteSchedule(long id);
}
