package model;

import dao.TasksDAO;

public class PostTaskLogic {
    public boolean execute(Task task) {
        return new TasksDAO().create(task);
    }
}
