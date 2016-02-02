package com.izpa.jira.plugins.issuesSender.dao.impl;


import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.sal.api.transaction.TransactionCallback;
import com.izpa.jira.plugins.issuesSender.task.EmailSender;
import net.java.ao.Query;
import com.izpa.jira.plugins.issuesSender.dao.TaskDAO;
import com.izpa.jira.plugins.issuesSender.entity.TaskEntity;
import com.izpa.jira.plugins.issuesSender.logic.Task;
import org.ofbiz.core.entity.GenericEntityException;
import org.quartz.CronExpression;
import com.atlassian.jira.ComponentManager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class TaskDAOImpl implements TaskDAO {
  private final ActiveObjects ao;
  private final EmailSender emailSender;

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
        try {
          CronExpression cron = new CronExpression(task.getCron());
          Date nextSend = cron.getNextValidTimeAfter(new Date());
          emailSender.createSchedule(entity.getID(), nextSend);
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
        emailSender.deleteSchedule(id);
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
      String email = entity.getEmail();

      //TODO доделать получение issues
      ProjectManager projectManager = ComponentManager.getInstance().getProjectManager();
      List<Project> projects = projectManager.getProjectObjects();
      IssueManager issueManager = ComponentManager.getInstance().getIssueManager();
      List<Issue> issues = new ArrayList<Issue>();
      for (Project project:projects){
        long projectID = project.getId();
        try {
          Collection<Long> ids = issueManager.getIssueIdsForProject(projectID);
          for(Issue issue:issueManager.getIssueObjects(ids)){
            System.out.println("-----------------------------------------------");
            System.out.println("ID: "+issue.getId());
            System.out.println("STATUS: "+issue.getStatus().getStatusCategory().getName());
            System.out.println("Project ID: "+issue.getProjectId());
            System.out.println("Summary: "+issue.getSummary());
            System.out.println("Assignee user: "+issue.getAssigneeUser().getDisplayName());
            System.out.println("Reporter user: "+issue.getReporterUser().getDisplayName());
            System.out.println("Description: "+issue.getDescription());
            System.out.println("Environment: "+issue.getEnvironment());

            System.out.println("-----------------------------------------------");

          }
        } catch (GenericEntityException e) {
          e.printStackTrace();
        }
      }

      //TODO послать email

      entity.setLastSend(new Date());

      emailSender.deleteSchedule(id);

      try {
        CronExpression cron = new CronExpression(entity.getCron());
        Date nextSend = cron.getNextValidTimeAfter(new Date());
        emailSender.createSchedule(entity.getID(), nextSend);
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

