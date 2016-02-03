package com.izpa.jira.plugins.issuesSender.issues;

import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import org.ofbiz.core.entity.GenericEntityException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IssuesCSVGetter {
  private List<String> issues;
  private static IssuesCSVGetter instance = null;

  private IssuesCSVGetter(){
    issues = new ArrayList<String>();
  }

  public static synchronized IssuesCSVGetter getInstance()throws Exception{
    if (instance == null) {
      instance = new IssuesCSVGetter();
    }
    return instance;
  }

  public File getIssues() throws IOException {
    refreshIssues();
    File issuesCSV = new File("issues.csv");
    FileWriter writer = new FileWriter(issuesCSV);
    for (String i : issues){
      writer.append(i+'\n');
    }
    writer.flush();
    writer.close();
    return issuesCSV;
  }

  private void refreshIssues(){
    issues.clear();
    issues.add("Summary, Project, Assignee, Reporter, Description, Due date, Created, Priority");
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
            row.append(issue.getSummary()+", ");
            row.append(project.getName()+", ");
            try {
              row.append(issue.getAssigneeUser().getDisplayName()+", ");
            } catch (Exception e) {
              row.append(", ");
            }
            try {
              row.append(issue.getReporterUser().getDisplayName()+", ");
            } catch (Exception e) {
              row.append(", ");
            }
            try {
              row.append(issue.getDescription()+", ");
            } catch (Exception e) {
              row.append(", ");
            }
            try {
              row.append(issue.getDueDate().toString()+", ");
            } catch (Exception e) {
              row.append(", ");
            }
            row.append(issue.getCreated().toString()+", ");
            row.append(issue.getPriority().getName());
            issues.add(row.toString());
          }
        }
      } catch (GenericEntityException e) {
        e.printStackTrace();
      }
    }

  }
}
