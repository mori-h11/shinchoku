package model;

import dao.TasksDAO;

public class GetProgressLogic {
	public int execute(int goalId) {
		TasksDAO dao = new TasksDAO();
		
		int total = dao.countAllByGoalId(goalId);

		int completed = dao.countCompletedByGoalId(goalId);
		
		// System.out.println("goalId=" + goalId + "total=" + total + "completed=" + completed);
		
		if (total == 0) {
			return 0;
		}
		
		int progress = completed * 100 / total;
		
		// System.out.println("progress=" + progress);
		
		return progress;
	}
}
