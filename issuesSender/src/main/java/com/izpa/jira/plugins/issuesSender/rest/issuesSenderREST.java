package com.izpa.jira.plugins.issuesSender.rest;

import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import com.cronutils.model.Cron;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import com.cronutils.validator.CronValidator;
import com.izpa.jira.plugins.issuesSender.dao.DAOFactory;
import com.izpa.jira.plugins.issuesSender.entity.TaskEntity;
import com.izpa.jira.plugins.issuesSender.logic.Task;
import com.izpa.jira.plugins.issuesSender.logic.impl.TaskImpl;
import com.izpa.jira.plugins.issuesSender.rest.xml.Mapper;
import com.izpa.jira.plugins.issuesSender.rest.xml.XmlTask;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.ws.rs.*;
import com.atlassian.jira.rest.v1.util.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("tasks")
@Consumes ({ MediaType.APPLICATION_JSON })
@Produces ({ MediaType.APPLICATION_JSON })
public class issuesSenderREST {
    private CronDefinition cronDefinition = CronDefinitionBuilder.defineCron()
                    .withSeconds().and()
                    .withMinutes().and()
                    .withHours().and()
                    .withDayOfMonth()
                    .supportsHash().supportsL().supportsW().and()
                    .withMonth().and()
                    .withDayOfWeek()
                    .withIntMapping(7, 0)
                    .supportsHash().supportsL().supportsW().and()
                    .withYear().and()
                    .lastFieldOptional()
                    .instance();
    private CronValidator quartzValidator = new CronValidator(cronDefinition);

    @GET
    public Response getTsaks() throws Exception {
        TaskEntity[] tasks = DAOFactory.getInstance().getTaskDAO().getTasks();
        List<XmlTask> result = new ArrayList<XmlTask>();
        for (TaskEntity i : tasks){
            result.add(Mapper.toXmlTask(i));
        }
        return Response.ok(result).cacheControl(CacheControl.NO_CACHE).build();
    }


    @GET
    @Path("{id}")
    public Response getTask(@PathParam("id") final String idStr) throws Exception {
        long id = Long.parseLong(idStr);
        return Response.ok(Mapper.toXmlTask(DAOFactory.getInstance().getTaskDAO().getTask(id))).cacheControl(CacheControl.NO_CACHE).build();
    }

    @POST
    public Response addTask(final XmlTask xmlTask) throws Exception {
        InternetAddress email;
        try {
            email = new InternetAddress(xmlTask.email);
        }
        catch (AddressException e){
            return Response.status(403).type("text/plain")
                    .entity("Invalid email").build();
        }
        if (!quartzValidator.isValid(xmlTask.cron)){
            return Response.status(403).type("text/plain")
                    .entity("Invalid cron-string").build();
        }
        CronParser parser = new CronParser(cronDefinition);
        Cron quartzCron = parser.parse(xmlTask.cron);
        Task task = new TaskImpl(email, quartzCron);
        TaskEntity taskEntity = DAOFactory.getInstance().getTaskDAO().addTask(task);
        return Response.ok(Mapper.toXmlTask(taskEntity)).cacheControl(CacheControl.NO_CACHE).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteTask(@PathParam("id") final String idStr) throws Exception {
        long id = Long.parseLong(idStr);
        TaskEntity taskEntity = DAOFactory.getInstance().getTaskDAO().deleteTask(id);
        return Response.ok(Mapper.toXmlTask(taskEntity)).cacheControl(CacheControl.NO_CACHE).build();
    }

    @PUT
    @Path("{id}")
    public Response updateTask(@PathParam("id") final String idStr, final XmlTask xmlTask) throws Exception {
        long id = Long.parseLong(idStr);
        InternetAddress email;
        try {
            email = new InternetAddress(xmlTask.email);
        }
        catch (AddressException e){
            return Response.status(403).type("text/plain")
                    .entity("Invalid email").build();
        }
        if (!quartzValidator.isValid(xmlTask.cron)){
            return Response.status(403).type("text/plain")
                    .entity("Invalid cron-string").build();
        }
        CronParser parser = new CronParser(cronDefinition);
        Cron quartzCron = parser.parse(xmlTask.cron);
        Task task = new TaskImpl(email, quartzCron);
        TaskEntity taskEntity = DAOFactory.getInstance().getTaskDAO().updateTask(id, task);
        return Response.ok(Mapper.toXmlTask(taskEntity)).cacheControl(CacheControl.NO_CACHE).build();
    }
}