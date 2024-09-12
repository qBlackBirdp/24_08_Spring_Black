<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <!-- JSP에서 자바스크립트로 loginedMember 객체 전달 -->
    <script>
        // JSP에서 넘어온 loginedMember의 이메일을 JavaScript 변수로 저장
        const loginedMemberEmail = '${loginedMember != null ? loginedMember.email : "null"}';

        document.addEventListener("DOMContentLoaded", function () {
            // loginedMemberEmail 정보가 있는지 확인
            if (loginedMemberEmail !== "null") {
                console.log("Logged in User Email: ", loginedMemberEmail);
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
