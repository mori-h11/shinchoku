package model;

import dao.TasksDAO;

public class DeleteTaskLogic {
	public void execute(int taskId) {
		TasksDAO dao = new TasksDAO();
		dao.delete(taskId);
	}
}
