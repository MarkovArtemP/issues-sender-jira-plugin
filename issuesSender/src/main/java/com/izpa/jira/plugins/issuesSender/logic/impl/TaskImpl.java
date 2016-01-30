package com.izpa.jira.plugins.issuesSender.logic.impl;

import com.cronutils.model.Cron;
import com.izpa.jira.plugins.issuesSender.logic.Task;

import javax.mail.internet.InternetAddress;

public class TaskImpl implements Task {
    private InternetAddress email;
    private Cron cron;

    public TaskImpl (InternetAddress email, Cron cron){
        this.setEmail(email);
        this.setCron(cron);
    }

    public InternetAddress getEmail() {
        return email;
    }

    public void setEmail(InternetAddress email) {
        this.email=email;
    }

    public Cron getCron() {
        return cron;
    }

    public void setCron(Cron cron) {
        this.cron = cron;
    }
}
