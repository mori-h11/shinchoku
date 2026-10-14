package model;

import java.util.List;

import dao.TasksDAO;

public class GetTaskListLogic {
	public List<Task> execute(int goalId) {
		TasksDAO dao = new TasksDAO();
		List<Task> taskList = dao.findByGoalId(goalId);
		return taskList;
	}
}
