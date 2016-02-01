package com.izpa.jira.plugins.issuesSender.dao.impl;


import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.sal.api.transaction.TransactionCallback;
import com.cronutils.model.Cron;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import com.izpa.jira.plugins.issuesSender.task.EmailSender;
import net.java.ao.Query;
import com.izpa.jira.plugins.issuesSender.dao.TaskDAO;
import com.izpa.jira.plugins.issuesSender.entity.TaskEntity;
import com.izpa.jira.plugins.issuesSender.logic.Task;
import org.joda.time.DateTime;

import java.util.Date;

public class TaskDAOImpl implements TaskDAO {
  private final ActiveObjects ao;
  private final EmailSender emailSender;
  private CronDefinition cronDefinition = CronDefinitionBuilder.defineCron()
          .withMinutes().and()
          .withHours().and()
          .withDayOfMonth()
          .supportsHash().supportsL().supportsW().and()
          .withMonth().and()
          .withDayOfWeek()
          .withIntMapping(7, 0)
          .supportsHash().supportsL().supportsW().and()
          .instance();
  CronParser parser = new CronParser(cronDefinition);

  public TaskDAOImpl(ActiveObjects ao, EmailSender emailSender) {
    this.ao = ao;
    this.emailSender = emailSender;
  }

  public TaskEntity addTask(final Task task) throws Exception {
    return ao.executeInTransaction(new TransactionCallback<TaskEntity>() {
        public TaskEntity doInTransaction() {
      TaskEntity entity = ao.create(TaskEntity.class);
      entity.setEmail(task.getEmail());
      entity.setCron(task.getCron());
      entity.setLastSend(null);

      DateTime now = DateTime.now();
      Cron cron = parser.parse(task.getCron());
      ExecutionTime executionTime = ExecutionTime.forCron(cron);
      DateTime nSend = executionTime.nextExecution(now);
      Date nextSend = nSend.toDate();

      emailSender.createSchedule(entity.getID(), nextSend);
      entity.setNextSend(nextSend);
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
      emailSender.deleteSchedule(id);
      ao.delete(entity);
      return entity;
        }
    });
  }
/*
  public TaskEntity updateTask(final long id, final Task task) throws Exception {
    return ao.executeInTransaction(new TransactionCallback<TaskEntity>() {
      public TaskEntity doInTransaction() {
        TaskEntity entity = ao.find(TaskEntity.class, Query.select().where("ID=?", id))[0];
        if ((task.getEmail() != null)&&(task.getCron() != null)) {
          entity.setEmail(task.getEmail());
          entity.setCron(task.getCron());
        }
        entity.save();
        return entity;
      }
    });
  }
  */
public TaskEntity sendMail(final long id) throws Exception {
  return ao.executeInTransaction(new TransactionCallback<TaskEntity>() {
    public TaskEntity doInTransaction() {
      TaskEntity entity = ao.find(TaskEntity.class, Query.select().where("ID=?", id))[0];
      String email = entity.getEmail();
      //TODO послать email
      //TODO delete this
      System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++Send email to"+email);

      entity.setLastSend(new Date());
      emailSender.deleteSchedule(id);
      Date nextSend = ExecutionTime.forCron(parser.parse(entity.getCron())).nextExecution(DateTime.now()).toDate();
      emailSender.createSchedule(entity.getID(), nextSend);
      entity.setNextSend(nextSend);
      entity.save();
      return entity;
    }
  });
}
  public TaskEntity getTask(long id) throws Exception{
    return ao.find(TaskEntity.class, Query.select().where("ID=?", id))[0];
  }
}

