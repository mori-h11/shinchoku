package dao;

import java.sql.*;
import model.TitleInput;

public class RegressionTest {
    private static int checks;
    private static void check(boolean condition, String name) {
        if (!condition) throw new AssertionError(name);
        checks++;
    }
    public static void main(String[] args) throws Exception {
        String url = "jdbc:h2:mem:regression;DB_CLOSE_DELAY=-1";
        TasksDAO dao = new TasksDAO(url, "sa", "");
        try (Connection conn = DriverManager.getConnection(url, "sa", "");
             Statement sql = conn.createStatement()) {
            sql.execute("CREATE TABLE TASKS(ID INTEGER PRIMARY KEY, GOAL_ID INTEGER NOT NULL, TITLE VARCHAR(255) NOT NULL, IS_COMPLETED BOOLEAN NOT NULL DEFAULT FALSE)");
            sql.execute("INSERT INTO TASKS VALUES(1,10,'A',FALSE),(2,20,'B',FALSE)");
            check(!dao.updateTitle(2,10,"bad"), "cross-goal edit rejected");
            check(!dao.updateCompleted(2,10), "cross-goal complete rejected");
            check(!dao.delete(2,10), "cross-goal delete rejected");
            try (ResultSet rs = sql.executeQuery("SELECT TITLE, IS_COMPLETED FROM TASKS WHERE ID=2")) {
                check(rs.next() && rs.getString(1).equals("B") && !rs.getBoolean(2), "B unchanged");
            }
            check(!dao.updateTitle(99,10,"bad"), "missing edit rejected");
            check(!dao.updateCompleted(99,10), "missing complete rejected");
            check(!dao.delete(99,10), "missing delete rejected");
            check(dao.updateTitle(1,10,"A edited"), "own edit allowed");
            check(dao.updateCompleted(1,10), "own complete allowed");
            try (ResultSet rs = sql.executeQuery("SELECT TITLE, IS_COMPLETED FROM TASKS WHERE ID=1")) {
                check(rs.next() && rs.getString(1).equals("A edited") && rs.getBoolean(2), "own state saved");
            }
            check(dao.delete(1,10), "own delete allowed");
            check(dao.findByGoalId(10).isEmpty(), "own deletion persisted");
            for (String blank : new String[]{"", "   ", "\u3000\u3000", "\t\r\n", "\u00a0"}) {
                check(TitleInput.error(TitleInput.normalize(blank), "name") != null, "blank rejected");
            }
            check(TitleInput.error(TitleInput.normalize(null), "name") != null, "null rejected");
            check(TitleInput.normalize(" \u3000A B\u3000 ").equals("A B"), "trim edges only");
            String emoji = new String(Character.toChars(0x1f600));
            for (String unit : new String[]{"a", "あ"}) {
                String accepted = unit.repeat(255);
                check(TitleInput.error(accepted,"name") == null, "255 codepoints accepted");
                check(TitleInput.error(unit.repeat(256),"name") != null, "256 codepoints rejected");
                check(dao.updateTitle(2,20,accepted), "255 stored by H2");
                check(dao.findByGoalId(20).getFirst().getTitle().equals(accepted), "255 roundtrip");
            }
            String emojiBoundary = emoji.repeat(127) + "a";
            check(TitleInput.error(emojiBoundary,"name") == null, "255 UTF-16 units accepted");
            check(TitleInput.error(emoji.repeat(128),"name") != null, "256 UTF-16 units rejected");
            check(dao.updateTitle(2,20,emojiBoundary), "emoji boundary stored");
            check(dao.findByGoalId(20).getFirst().getTitle().equals(emojiBoundary), "emoji roundtrip");
        }
        System.out.println("PASS: " + checks + " checks; isolated in-memory H2 only");
    }
}
