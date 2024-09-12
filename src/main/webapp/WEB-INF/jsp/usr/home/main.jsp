<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <!-- JSP에서 자바스크립트로 loginedMember 객체 전달 -->
    <script>
        // JSP에서 넘어온 loginedMember 객체를 JavaScript 객체로 변환
        const loginedMember = ${loginedMember != null ? loginedMember : 'null'};

        document.addEventListener("DOMContentLoaded", function () {
            // loginedMember 정보가 있는지 확인
            if (loginedMember !== null) {
                console.log("Logged in User: ", loginedMember);
            } else {
                console.log("No logged in user");
            }
        });
    </script>
</head>
<body>
asdfasdf
</body>
</html>
