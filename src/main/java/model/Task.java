package model;

import java.io.Serializable;

public class Task implements Serializable {
	private int id;
	private int goalId;
	private String title;
	private boolean completed;
	
	public Task() { }
	
	public Task(int goalId, String title) {
		this.goalId = goalId;
		this.title = title;
	}
	
	public Task(int id, int goalId, String title, boolean completed) {
		this.id =id;
		this.goalId = goalId;
		this.title = title;
		this.completed = completed;
	}
	
	public int getId() { return id; }
	public int getGoalId() { return goalId; }
	public String getTitle() { return title; }
	public boolean isCompleted() { return completed; }
}
