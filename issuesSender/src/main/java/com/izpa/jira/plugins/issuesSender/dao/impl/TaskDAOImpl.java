package com.izpa.jira.plugins.issuesSender.dao.impl;


import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.sal.api.transaction.TransactionCallback;
import net.java.ao.Query;
import com.izpa.jira.plugins.issuesSender.dao.TaskDAO;
import com.izpa.jira.plugins.issuesSender.entity.TaskEntity;
import com.izpa.jira.plugins.issuesSender.logic.Task;

public class TaskDAOImpl implements TaskDAO {
    private final ActiveObjects ao;
    public TaskDAOImpl(ActiveObjects ao) {
        this.ao = ao;
    }

    public TaskEntity addTask(final Task task) throws Exception {
        return ao.executeInTransaction(new TransactionCallback<TaskEntity>() {
            public TaskEntity doInTransaction() {
                TaskEntity entity = ao.create(TaskEntity.class);
                entity.setEmail(task.getEmail());
                entity.setCron(task.getCron());
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
                ao.delete(entity);
                return entity;
            }
        });
    }

    public TaskEntity updateTask(final long id, final Task task) throws Exception {
        return ao.executeInTransaction(new TransactionCallback<TaskEntity>() {
            public TaskEntity doInTransaction() {
                TaskEntity entity = ao.find(TaskEntity.class, Query.select().where("ID=?", id))[0];
                if (task.getEmail() != null) {
                    entity.setEmail(task.getEmail());
                }
                if (task.getCron() != null) {
                    entity.setCron(task.getCron());
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

