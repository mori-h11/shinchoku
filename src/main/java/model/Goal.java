package model;

import java.io.Serializable;

public class Goal implements Serializable {
	private int id;          // ID
	private String userName; // ユーザー名
	private String title;    // タイトル
	private int progress;    // 進捗率
	
	public Goal() { }
	
	public Goal(String userName, String title) {
		this.userName = userName;
		this.title = title;
	}
	
	public Goal(int id, String userName, String title) {
		this.id = id;
		this.userName = userName;
		this.title = title;
	}
	
	public void setProgress(int progress) {
		this.progress = progress;
	}
	
	public int getId() { return id;	}
	public String getUserName() { return userName; }
	public String getTitle() { return title; }
	public int getProgress() { return progress;	}
}
