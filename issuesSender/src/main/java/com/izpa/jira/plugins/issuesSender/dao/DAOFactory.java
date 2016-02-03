package com.izpa.jira.plugins.issuesSender.dao;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.izpa.jira.plugins.issuesSender.dao.impl.TaskDAOImpl;
import com.izpa.jira.plugins.issuesSender.schedule.Scheduler;

import static com.google.common.base.Preconditions.checkNotNull;


public class DAOFactory {
  private static TaskDAO recordDAO = null;
  private static DAOFactory instance = null;
  private static ActiveObjects ao;
  private static Scheduler scheduler;

  private DAOFactory(ActiveObjects ao, Scheduler scheduler) {
    DAOFactory.ao=checkNotNull(ao);
    DAOFactory.scheduler = checkNotNull(scheduler);
  }

  public static synchronized DAOFactory getInstance() throws Exception {
    if (instance == null) {
      instance = new DAOFactory(ao, scheduler);
    }
    return instance;
  }

  public TaskDAO getTaskDAO() {
    if (recordDAO == null) {
      recordDAO = new TaskDAOImpl(ao, scheduler);
    }
    return recordDAO;
  }
}

