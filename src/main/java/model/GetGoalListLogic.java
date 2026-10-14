package model;

import java.util.List;

import dao.GoalsDAO;

public class GetGoalListLogic {
	
	public List<Goal> execute(String userName) {
		
		GoalsDAO dao = new GoalsDAO();
		List<Goal> goalList = dao.findByUserName(userName);
		
		return goalList;
	}
}
