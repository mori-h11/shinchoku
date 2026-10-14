package model;

import dao.GoalsDAO;

public class PostGoalLogic {
	public void execute(Goal goal) {
		GoalsDAO dao = new GoalsDAO();
		dao.create(goal);
	}
}
