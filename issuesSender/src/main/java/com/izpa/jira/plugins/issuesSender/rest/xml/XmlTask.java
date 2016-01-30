package com.izpa.jira.plugins.issuesSender.rest.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class XmlTask {
    public XmlTask(){}
    @XmlElement
    public long id;
    @XmlElement
    public String email;
    @XmlElement
    public String cron;
}
