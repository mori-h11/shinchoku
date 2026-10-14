package model;

import dao.GoalsDAO;

public class PostGoalLogic {
    public boolean execute(Goal goal) {
        return new GoalsDAO().create(goal);
    }
}
