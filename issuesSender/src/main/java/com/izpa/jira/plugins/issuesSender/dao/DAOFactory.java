package com.izpa.jira.plugins.issuesSender.dao;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.izpa.jira.plugins.issuesSender.dao.impl.TaskDAOImpl;
import com.izpa.jira.plugins.issuesSender.task.EmailSender;

import static com.google.common.base.Preconditions.checkNotNull;


public class DAOFactory {
  private static TaskDAO recordDAO = null;
  private static DAOFactory instance = null;
  private static ActiveObjects ao;
  private static EmailSender emailSender;

  private DAOFactory(ActiveObjects ao, EmailSender emailSender) {
    DAOFactory.ao=checkNotNull(ao);
    DAOFactory.emailSender = checkNotNull(emailSender);
  }

  public static synchronized DAOFactory getInstance() throws Exception {
    if (instance == null) {
      instance = new DAOFactory(ao, emailSender);
    }
    return instance;
  }

  public TaskDAO getTaskDAO() {
    if (recordDAO == null) {
      recordDAO = new TaskDAOImpl(ao, emailSender);
    }
    return recordDAO;
  }
}

