package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Task;

public class TasksDAO {
	private final String JDBC_URL = "jdbc:h2:tcp://localhost/~/dokoTsubu";
	private final String DB_USER = "sa";
	private final String DB_PASS = "";
	
	public List<Task> findByGoalId(int goalId) {
		List<Task> taskList = new ArrayList<>();
	
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}
		
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
			String sql = """
				    SELECT ID, GOAL_ID, TITLE, IS_COMPLETED
				    FROM TASKS
				    WHERE GOAL_ID = ?
				    ORDER BY ID ASC
				    """;
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, goalId);
			ResultSet rs = pStmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("ID");
				int dbGoalId = rs.getInt("GOAL_ID");
				String title = rs.getString("TITLE");
				boolean completed = rs.getBoolean("IS_COMPLETED");
				
				Task task = new Task(id, dbGoalId, title, completed);
				taskList.add(task);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return taskList;
	}
	
	public boolean create(Task task) {
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}
		
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
			String sql = """
					INSERT INTO TASKS(GOAL_ID, TITLE) 
					VALUES(?, ?)
					""";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, task.getGoalId());
			pStmt.setString(2, task.getTitle());
			
			int result = pStmt.executeUpdate();
			
			if (result != 1) {
				return false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean updateCompleted(int taskId) {
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}
		
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
			
			String sql = """
					UPDATE TASKS 
					SET IS_COMPLETED = TRUE
					WHERE ID = ?
					""";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, taskId);
			
			int result = pStmt.executeUpdate();
			
			if (result != 1) {
				return false;
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
				return false;
		}
			return true;
	}
	
	public boolean delete(int taskId) {
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}
		
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
			String sql = "DELETE FROM TASKS WHERE ID = ?";
			
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, taskId);
			
			int result = pStmt.executeUpdate();
			if (result != 1) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean updateTitle(int taskId, String title) {
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}
		
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
			String sql = """
					UPDATE TASKS SET TITLE = ? WHERE ID = ?
					""";
			
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			pStmt.setString(1, title);
			pStmt.setInt(2, taskId);
			
			int result = pStmt.executeUpdate();
			
			if (result != 1) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	// 全タスク数を取得
	public int countAllByGoalId(int goalId) {
		int count = 0;
		
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}
		
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
			
			String sql = """
					SELECT COUNT(*) FROM TASKS WHERE GOAL_ID = ?
					""";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, goalId);
			
			ResultSet rs = pStmt.executeQuery();
			
			if (rs.next()) {
				count = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	// 完了タスク数を取得
	public int countCompletedByGoalId(int goalId) {
		
		int count = 0;
		
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}
		
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
			String sql = """
					SELECT COUNT(*) FROM TASKS WHERE GOAL_ID = ? 
					AND IS_COMPLETED = TRUE
					""";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			pStmt.setInt(1, goalId);
			
			ResultSet rs = pStmt.executeQuery();
			
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
}
