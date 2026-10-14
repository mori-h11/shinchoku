package model;

import dao.TasksDAO;

public class UpdateTaskLogic {
    public boolean execute(int taskId, int goalId) {
        return new TasksDAO().updateCompleted(taskId, goalId);
    }
}
