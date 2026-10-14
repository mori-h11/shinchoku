package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.CheckGoalOwnerLogic;
import model.DeleteTaskLogic;
import model.EditTaskLogic;
import model.GetTaskListLogic;
import model.PostTaskLogic;
import model.Task;
import model.UpdateTaskLogic;
import model.User;

@WebServlet("/TaskMain")
public class TaskMain extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// セッションスコープからログインユーザーを取得
		HttpSession session = request.getSession();
		
		User loginUser = (User) session.getAttribute("loginUser");
		
		// 未ログインの場合はログイン画面へ戻す
		if (loginUser == null) {
			response.sendRedirect("index.jsp");
			return;
		}
		// URLからgoalIdを取得
		String goalIdParam = request.getParameter("goalId");
		
		// goalIdがない場合は目標一覧へ戻す
		if (goalIdParam == null) {
			response.sendRedirect("GoalMain");
			return;
		}
		
		int goalId;
		
		try {
			goalId = Integer.parseInt(goalIdParam);
			
		} catch (NumberFormatException e) {
			
			response.sendRedirect("GoalMain");
			return;
		}
		
		// この目標がログインユーザーのものか確認
		CheckGoalOwnerLogic checkGoalOwnerLogic = new CheckGoalOwnerLogic();
		boolean isOwner = checkGoalOwnerLogic.execute(goalId, loginUser.getName());
		
		// 他ユーザーの目標・存在しない目標なら戻す
		if (!isOwner) {
			response.sendRedirect("GoalMain");
			return;
		}
		// 選択した目標のタスク一覧を取得
		GetTaskListLogic getTaskListLogic = new GetTaskListLogic();
		
		List<Task> taskList = getTaskListLogic.execute(goalId);
		
		// JSPへ戻す
		request.setAttribute("taskList", taskList);
		request.setAttribute("goalId", goalId);
		
		// タスク管理画面へフォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/taskMain.jsp");
		dispatcher.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		// ログイン確認
		HttpSession session = request.getSession();
		
		User loginUser = (User) session.getAttribute("loginUser");
		
		if (loginUser== null) {
			response.sendRedirect("index.jsp");
			return;
		}
		// goalIdを取得
		String goalIdParam = request.getParameter("goalId");
		
		if (goalIdParam == null) {
			response.sendRedirect("GoalMain");
			return;
		}
		
		int goalId;
		
		try {
			goalId = Integer.parseInt(goalIdParam);
		} catch (NumberFormatException e) {
			response.sendRedirect("GoalMain");
			return;
		}
		// この目標がログインユーザーのものか確認
		CheckGoalOwnerLogic checkGoalOwnerLogic =new CheckGoalOwnerLogic();
		boolean isOwner = checkGoalOwnerLogic.execute(goalId, loginUser.getName());
		
		if (!isOwner) {
			response.sendRedirect("GoalMain");
			return;
		}
		// 何の処理科取得
		String action = request.getParameter("action");
		
		// タスク追加
		if ("add".equals(action)) {
			
			String title = request.getParameter("title");
			
			if (title != null && title.length() != 0) {
				Task task = new Task(goalId, title);
				
				PostTaskLogic postTaskLogic = new PostTaskLogic();
				postTaskLogic.execute(task);
				
			} else {
				
				request.setAttribute("errorMsg", "タスクが入力されていません");
	
			}
		// タスク完了
		} else if ("complete".equals(action)) {
			String taskIdParam = request.getParameter("taskId");
			
			if (taskIdParam != null) {
				try {
					int taskId = Integer.parseInt(taskIdParam);
					
					UpdateTaskLogic updateTaskLogic = new UpdateTaskLogic();
					updateTaskLogic.execute(taskId);
				} catch (NumberFormatException e) {
					request.setAttribute("errorMsg", "たすくIDが不正です");
				}
			}
		// タスク削除
		} else if ("delete".equals(action)) {
			String taskIdParam = request.getParameter("taskId");
			
			if (taskIdParam != null) {
				try {
					int taskId = Integer.parseInt(taskIdParam);
					
					DeleteTaskLogic deleteTaskLogic = new DeleteTaskLogic();
					deleteTaskLogic.execute(taskId);
				} catch (NumberFormatException e) {
					request.setAttribute("errorMsg", "タスクIDが不正です");
				}
			}
		// タスク名編集			
		} else if ("edit".equals(action)) {
			String taskIdParam = request.getParameter("taskId");
			String title = request.getParameter("title");
			
			if (title != null && title.length() != 0 && taskIdParam != null) {
				
				try {
					int taskId = Integer.parseInt(taskIdParam);
					
					EditTaskLogic editTaskLogic = new EditTaskLogic();
					editTaskLogic.execute(taskId, title);
				} catch (NumberFormatException e) {
					request.setAttribute("errorMsg", "タスクIDが不正です");
				}
			} else {
				request.setAttribute("errorMsg", "タスク名が入力されていません");
			}
		}
		// 処理後にタスク一覧を再取得
		GetTaskListLogic getTaskListLogic = new GetTaskListLogic();
		
		List<Task> taskList = getTaskListLogic.execute(goalId);
		
		// JSPへ戻す
		request.setAttribute("taskList", taskList);
		request.setAttribute("goalId", goalId);
		
		// タスク管理画面へ戻る
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/taskMain.jsp");
		dispatcher.forward(request, response);
	}
}
