package model;

import dao.TasksDAO;

public class EditTaskLogic {
    public boolean execute(int taskId, int goalId, String title) {
        return new TasksDAO().updateTitle(taskId, goalId, title);
    }
}
