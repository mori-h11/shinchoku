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
import model.TitleInput;

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
        String action = request.getParameter("action");
        String error = null;
        if ("add".equals(action)) {
            String title = TitleInput.normalize(request.getParameter("title"));
            error = TitleInput.error(title, "タスク名");
            if (error == null && !new PostTaskLogic().execute(new Task(goalId, title))) {
                error = "タスクを登録できませんでした";
            }
        } else if ("complete".equals(action) || "edit".equals(action) || "delete".equals(action)) {
            try {
                int taskId = Integer.parseInt(request.getParameter("taskId"));
                if (taskId <= 0) throw new NumberFormatException();
                boolean changed = false;
                if ("edit".equals(action)) {
                    String title = TitleInput.normalize(request.getParameter("title"));
                    error = TitleInput.error(title, "タスク名");
                    if (error == null) changed = new EditTaskLogic().execute(taskId, goalId, title);
                } else if ("complete".equals(action)) {
                    changed = new UpdateTaskLogic().execute(taskId, goalId);
                } else {
                    changed = new DeleteTaskLogic().execute(taskId, goalId);
                }
                if (error == null && !changed) error = "対象タスクを変更できませんでした";
            } catch (NumberFormatException e) {
                error = "タスクIDが不正です";
            }
        } else {
            error = "操作が不正です";
        }
        if (error != null) request.setAttribute("errorMsg", error);
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
