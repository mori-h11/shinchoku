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

import model.GetGoalListLogic;
import model.GetProgressLogic;
import model.Goal;
import model.PostGoalLogic;
import model.User;
import model.TitleInput;

@WebServlet("/GoalMain")
public class GoalMain extends HttpServlet {
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
		
		// ログインユーザーの目標だけ取得
		GetGoalListLogic getGoalListLogic = new GetGoalListLogic();
		List<Goal> goalList = getGoalListLogic.execute(loginUser.getName());
		
		// 各目標の進捗率を計算
		GetProgressLogic getProgressLogic = new GetProgressLogic();
		
		for (Goal goal : goalList) {
			int progress = getProgressLogic.execute(goal.getId());
			goal.setProgress(progress);
		}
		
		// 目標一覧をリクエストスコープに保存
		request.setAttribute("goalList", goalList);
		
		// 目標管理画面へフォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/goalMain.jsp");
		dispatcher.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 文字コードを設定
		request.setCharacterEncoding("UTF-8");
		
		// セッションスコープからログインユーザーを取得
		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");
		
		// 未ログインの場合はログイン画面へ戻す
		if (loginUser == null) {
			response.sendRedirect("index.jsp");
			return;
		}
		
		// 入力された目標名を取得
		String title = TitleInput.normalize(request.getParameter("title"));
        String titleError = TitleInput.error(title, "目標");
		
		// 入力チェック
		if (titleError == null) {
		
			// Goalインスタンスを作成			
			Goal goal = new Goal(loginUser.getName(), title);
			// 目標をDBへ登録
			PostGoalLogic postGoalLogic = new PostGoalLogic();
			if (!postGoalLogic.execute(goal)) {
                request.setAttribute("errorMsg", "目標を登録できませんでした");
            }
			
		} else {
			// 未入力の場合
			request.setAttribute("errorMsg", titleError);
		}
		// ログインユーザーの目標だけ再取得
		GetGoalListLogic getGoalListLogic = new GetGoalListLogic();
		List<Goal> goalList = getGoalListLogic.execute(loginUser.getName());
		
		// 各目標の進捗率を計算
		GetProgressLogic getProgressLogic = new GetProgressLogic();
		
		for (Goal goal : goalList) {
			int progress = getProgressLogic.execute(goal.getId());
			goal.setProgress(progress);
		}
		
		// 目標一覧をリクエストスコープに保存
		request.setAttribute("goalList", goalList);
		
		// 目標管理画面へフォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/goalMain.jsp");
		
		dispatcher.forward(request, response);
	}
}