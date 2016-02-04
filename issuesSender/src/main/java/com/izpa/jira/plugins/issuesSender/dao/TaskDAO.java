package com.izpa.jira.plugins.issuesSender.dao;

import com.izpa.jira.plugins.issuesSender.entity.TaskEntity;
import com.izpa.jira.plugins.issuesSender.logic.Task;

public interface TaskDAO {
  public TaskEntity addTask(Task task) throws Exception;
  public TaskEntity[] getTasks() throws Exception;
  public TaskEntity deleteTask(long id) throws Exception;
  public TaskEntity getTask(long id) throws Exception;
  public TaskEntity sendMail(long id) throws Exception;
  public TaskEntity updateTask(long id) throws Exception;
}
