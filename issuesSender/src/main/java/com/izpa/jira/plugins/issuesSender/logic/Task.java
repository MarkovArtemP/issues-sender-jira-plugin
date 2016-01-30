package com.izpa.jira.plugins.issuesSender.logic;


import com.cronutils.model.Cron;
import javax.mail.internet.InternetAddress;

public interface Task {
    public InternetAddress getEmail();
    public void setEmail(InternetAddress email);

    public Cron getCron();
    public void setCron(Cron cron);

}
