package model;

/** 目標名・タスク名に共通の正規化と文字数検査。 */
public final class TitleInput {
    private TitleInput() { }

    private static boolean isSpace(int value) {
        return Character.isWhitespace(value) || Character.isSpaceChar(value);
    }

    public static String normalize(String value) {
        if (value == null) return "";
        int start = 0;
        int end = value.length();
        while (start < end && isSpace(value.codePointAt(start))) {
            start += Character.charCount(value.codePointAt(start));
        }
        while (end > start && isSpace(value.codePointBefore(end))) {
            end -= Character.charCount(value.codePointBefore(end));
        }
        return value.substring(start, end);
    }

    public static String error(String title, String label) {
        if (title.isEmpty()) return label + "が入力されていません";
        // H2 VARCHARの上限と同じUTF-16単位。補助文字（絵文字等）は2単位。
        if (title.length() > 255) {
            return label + "は255文字以内で入力してください";
        }
        return null;
    }
}
