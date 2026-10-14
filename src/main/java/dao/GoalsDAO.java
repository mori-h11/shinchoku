package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Goal;

public class GoalsDAO {
	private final String JDBC_URL = "jdbc:h2:tcp://localhost/~/dokoTsubu";
	private final String DB_USER = "sa";
	private final String DB_PASS =  "";
	
	public List<Goal> findAll() {
		List<Goal> goalList = new ArrayList<>();
		
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}
		
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
			
			String sql = "SELECT ID, USER_NAME, TITLE FROM GOALS ORDER BY ID DESC";
			
			PreparedStatement pStmt =  conn.prepareStatement(sql);
			
			ResultSet rs = pStmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("ID");
				String userName =rs.getString("USER_NAME");
				String title = rs.getString("TITLE");
				
				Goal goal = new Goal(id, userName, title);
				goalList.add(goal);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return goalList;
	}
	
	public List<Goal> findByUserName(String userName) {
		List<Goal> goalList = new ArrayList<>();
		
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}
		
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
			String sql = """
					SELECT ID, USER_NAME, TITLE FROM GOALS 
					WHERE USER_NAME = ? ORDER BY ID DESC
					""";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, userName);
			
			ResultSet rs = pStmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("ID");
				String dbUserName = rs.getString("USER_NAME");
				String title = rs.getString("TITLE");
				
				Goal goal = new Goal(id, dbUserName, title);
				goalList.add(goal);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
			return goalList;
	}
	
	public boolean create(Goal goal) {
		
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}
		
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS )) {
			
			String sql = "INSERT INTO GOALS(USER_NAME, TITLE) VALUES(?, ?)";
			
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			pStmt.setString(1, goal.getUserName());
			pStmt.setString(2, goal.getTitle());
			
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
	
	// 所有者確認メソッド
	public boolean belongsToUser(int goalId, String userName) {
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}
		
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
			String sql = """
					SELECT COUNT(*) FROM GOALS WHERE ID = ?
					 AND USER_NAME = ?
					""";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, goalId);
			pStmt.setString(2, userName);
			
			ResultSet rs = pStmt.executeQuery();
			
			if (rs.next()) {
				return rs.getInt(1) == 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
