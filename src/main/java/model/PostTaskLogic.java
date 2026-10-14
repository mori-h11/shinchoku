package model;

import dao.TasksDAO;

public class PostTaskLogic {
	public void execute(Task task) {
		TasksDAO dao = new TasksDAO();
		dao.create(task);
	}
}
