package model;

import dao.TasksDAO;

public class DeleteTaskLogic {
    public boolean execute(int taskId, int goalId) {
        return new TasksDAO().delete(taskId, goalId);
    }
}
