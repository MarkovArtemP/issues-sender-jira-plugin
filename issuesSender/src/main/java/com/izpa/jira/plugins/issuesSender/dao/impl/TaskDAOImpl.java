package com.izpa.jira.plugins.issuesSender.dao.impl;


import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.mail.Email;
import com.atlassian.mail.queue.SingleMailQueueItem;
import com.atlassian.sal.api.transaction.TransactionCallback;
import com.izpa.jira.plugins.issuesSender.issues.IssuesCSVGetter;
import com.izpa.jira.plugins.issuesSender.mailSender.MailSender;
import com.izpa.jira.plugins.issuesSender.schedule.Scheduler;
import net.java.ao.Query;
import com.izpa.jira.plugins.issuesSender.dao.TaskDAO;
import com.izpa.jira.plugins.issuesSender.entity.TaskEntity;
import com.izpa.jira.plugins.issuesSender.logic.Task;
import org.quartz.CronExpression;

import java.text.ParseException;
import java.util.Date;

public class TaskDAOImpl implements TaskDAO {
  private final ActiveObjects ao;
  private final Scheduler scheduler;

  public TaskDAOImpl(ActiveObjects ao, Scheduler scheduler) {
    this.ao = ao;
    this.scheduler = scheduler;
  }

  public TaskEntity addTask(final Task task) throws Exception {
    return ao.executeInTransaction(new TransactionCallback<TaskEntity>() {
      public TaskEntity doInTransaction() {
        TaskEntity entity = ao.create(TaskEntity.class);
        entity.setEmail(task.getEmail());
        entity.setCron(task.getCron());
        entity.setLastSend(null);
        try {
          CronExpression cron = new CronExpression(task.getCron());
          Date nextSend = cron.getNextValidTimeAfter(new Date());
          scheduler.createSchedule(entity.getID(), nextSend);
          entity.setNextSend(nextSend);
        } catch (ParseException e) {
          e.printStackTrace();
        }
        entity.save();
        return entity;
      }
    });
  }

  public TaskEntity[] getTasks() throws Exception {
    return ao.executeInTransaction(new TransactionCallback<TaskEntity[]>() {
      public TaskEntity[] doInTransaction() {
        return ao.find(TaskEntity.class);
      }
    });
  }

  public TaskEntity deleteTask(final long id) throws Exception {
    return ao.executeInTransaction(new TransactionCallback<TaskEntity>() {
        public TaskEntity doInTransaction() {
      TaskEntity entity = ao.find(TaskEntity.class, Query.select().where("ID=?", id))[0];
      try {
        scheduler.deleteSchedule(id);
      }catch (IllegalArgumentException e){}
      ao.delete(entity);
      return entity;
        }
    });
  }

public TaskEntity sendMail(final long id) throws Exception {
  return ao.executeInTransaction(new TransactionCallback<TaskEntity>() {
    public TaskEntity doInTransaction() {
      TaskEntity entity = ao.find(TaskEntity.class, Query.select().where("ID=?", id))[0];
      //TODO обработка отсутствия задач
      try {
        MailSender.getInstance().sendIssues(entity.getEmail(),IssuesCSVGetter.getInstance().getIssues());
      } catch (Exception e) {
        e.printStackTrace();
      }

      scheduler.deleteSchedule(id);

      try {
        CronExpression cron = new CronExpression(entity.getCron());
        Date nextSend = cron.getNextValidTimeAfter(new Date());
        scheduler.createSchedule(entity.getID(), nextSend);
        entity.setNextSend(nextSend);
      } catch (ParseException e) {
        e.printStackTrace();
      }
      entity.save();
      return entity;
    }
  });
}
  public TaskEntity getTask(long id) throws Exception{
    return ao.find(TaskEntity.class, Query.select().where("ID=?", id))[0];
  }
}

