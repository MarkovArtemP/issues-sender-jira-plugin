package com.izpa.jira.plugins.issuesSender.issues;

import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import org.ofbiz.core.entity.GenericEntityException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IssuesCSVGetter {
  private List<String> issuesCSV;
  private static IssuesCSVGetter instance = null;

  private IssuesCSVGetter(){
    issuesCSV = new ArrayList<String>();
  }

  public static synchronized IssuesCSVGetter getInstance()throws Exception{
    if (instance == null) {
      instance = new IssuesCSVGetter();
    }
    return instance;
  }

  public List getIssues(){
    refreshIssues();
    return issuesCSV;
  }

  private void refreshIssues(){
    issuesCSV.clear();
    issuesCSV.add("Summary,Project,Assignee user,Reporter user,Description,Due date,Created,Priority");
    ProjectManager projectManager = ComponentManager.getInstance().getComponentInstanceOfType(ProjectManager.class);
    List<Project> projects = projectManager.getProjectObjects();
    IssueManager issueManager = ComponentManager.getInstance().getComponentInstanceOfType(IssueManager.class);
    for (Project project:projects){
      long projectID = project.getId();
      try {
        Collection<Long> ids = issueManager.getIssueIdsForProject(projectID);
        for(Issue issue:issueManager.getIssueObjects(ids)){
          if(issue.getStatus().getStatusCategory().getName()=="In Progress") {
            StringBuilder row = new StringBuilder();
            row.append(issue.getSummary()+",");
            row.append(project.getName()+",");
            try {
              row.append(issue.getAssigneeUser().getDisplayName()+",");
            } catch (Exception e) {
              row.append(",");
            }
            try {
              row.append(issue.getReporterUser().getDisplayName());
            } catch (Exception e) {
              row.append(",");
            }
            try {
              row.append(issue.getDescription());
            } catch (Exception e) {
              row.append(",");
            }
            try {
              row.append(issue.getDueDate().toString());
            } catch (Exception e) {
              row.append(",");
            }
            row.append(issue.getCreated().toString());
            row.append(issue.getPriority().getName());
            issuesCSV.add(row.toString());
          }
        }
      } catch (GenericEntityException e) {
        e.printStackTrace();
      }
    }
  }
}
