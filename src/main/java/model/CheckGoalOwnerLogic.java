package model;

import dao.GoalsDAO;

public class CheckGoalOwnerLogic {
	public boolean execute(int goalId, String userName) {
		GoalsDAO dao = new GoalsDAO();
		return dao.belongsToUser(goalId, userName);
	}
}
