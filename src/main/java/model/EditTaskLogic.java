package model;

import dao.TasksDAO;

public class EditTaskLogic {
	public void execute(int taskId, String title) {
		TasksDAO dao = new TasksDAO();
		dao.updateTitle(taskId, title);
	}
}
