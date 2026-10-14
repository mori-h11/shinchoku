package model;

import dao.TasksDAO;

public class UpdateTaskLogic {
	public void execute(int taskId) {
		TasksDAO dao = new TasksDAO();
		dao.updateCompleted(taskId);
	}
}
