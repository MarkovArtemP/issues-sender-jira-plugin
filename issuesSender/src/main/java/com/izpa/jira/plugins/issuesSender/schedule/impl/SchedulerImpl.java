package com.izpa.jira.plugins.issuesSender.schedule.impl;

import com.atlassian.sal.api.lifecycle.LifecycleAware;
import com.atlassian.sal.api.scheduling.PluginScheduler;
import com.izpa.jira.plugins.issuesSender.dao.DAOFactory;
import com.izpa.jira.plugins.issuesSender.entity.TaskEntity;
import com.izpa.jira.plugins.issuesSender.logic.Task;
import com.izpa.jira.plugins.issuesSender.logic.impl.TaskImpl;
import com.izpa.jira.plugins.issuesSender.schedule.Scheduler;
import com.izpa.jira.plugins.issuesSender.schedule.task.SendEmail;

import java.util.Date;
import java.util.HashMap;

public class SchedulerImpl implements LifecycleAware, Scheduler {
  //TODO добавить логирование
  private final PluginScheduler pluginScheduler;

  public SchedulerImpl(PluginScheduler pluginScheduler){
    this.pluginScheduler = pluginScheduler;
  }

  public void createSchedule(final long id, Date date) {
    pluginScheduler.scheduleJob(
            "sendMailJob" + Long.toString(id),
            SendEmail.class,
            new HashMap<String,Object>() {{
              put("id", id);
            }},
            date,
            60000L);
  }

  public void deleteSchedule(long id){
    pluginScheduler.unscheduleJob("sendMailJob" + Long.toString(id));
  }

  public void onStart() {
    TaskEntity[] taskEntities;
    try {
      taskEntities = DAOFactory.getInstance().getTaskDAO().getTasks();
      for(TaskEntity entity : taskEntities){
        Task task = new TaskImpl(entity.getEmail(), entity.getCron());
        DAOFactory.getInstance().getTaskDAO().deleteTask(entity.getID());
        DAOFactory.getInstance().getTaskDAO().addTask(task);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
