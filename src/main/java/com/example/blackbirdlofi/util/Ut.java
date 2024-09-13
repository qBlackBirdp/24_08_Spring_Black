package com.example.blackbirdlofi.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.Map;
import jakarta.servlet.http.HttpServletResponse;

public class Ut {

    public static String jsReplace(String resultCode, String msg, String replaceUri) {

        if (resultCode == null) {
            resultCode = "";
        }
        if (msg == null) {
            msg = "";
        }
        if (replaceUri == null) {
            replaceUri = "/";
        }

        String resultMsg = resultCode + " / " + msg;

        return Ut.f("""
					<script>
						let resultMsg = '%s'.trim();

						if(resultMsg.length > 0){
							alert(resultMsg);
						}
						location.replace('%s');
					</script>
				""", resultMsg, replaceUri);
    }

    public static String jsHistoryBack(String resultCode, String msg) {
        if (resultCode == null) {
            resultCode = "";
        }
        if (msg == null) {
            msg = "";
        }

        String resultMsg = resultCode + " / " + msg;

        return Ut.f("""
					<script>
						let resultMsg = '%s'.trim();

						if(resultMsg.length > 0){
							alert(resultMsg);
						}
						history.back();
					</script>
				""", resultMsg);
    }

    public static boolean isEmptyOrNull(String str) {

        return str == null || str.trim().isEmpty();
    }

    public static boolean isEmpty(Object obj) {
        if(obj == null) return true;

        if(obj instanceof String) return ((String)obj).trim().isEmpty();

        if (obj instanceof Map) return ((Map<?, ?>) obj).isEmpty();

        if (obj.getClass().isArray()) return Array.getLength(obj) == 0;

        return false;
    }

    public static String f(String format, Object... args) {
        return String.format(format, args);
    }

    // 새로 추가된 메서드: HttpServletResponse로 메시지를 출력하는 메서드
    public static void printHistoryBack(HttpServletResponse resp, String msg) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("<script>");
        if (!Ut.isEmpty(msg)) {
            out.println("alert('" + msg + "');");
        }
        out.println("history.back();");
        out.println("</script>");
        out.flush();
    }
}
