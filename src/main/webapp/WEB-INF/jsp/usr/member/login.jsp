<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Log in</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta charset="utf-8"/>
    <meta property="twitter:card" content="summary_large_image"/>

    <!-- 번들된 JS 파일을 불러오기 -->
    <script src="http://localhost:8082/bundle.js"></script>

    <!-- CSS 추가 -->
    <style>
        /* 전체 레이아웃 설정 */
        body {
            font-family: 'Inter', sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            background-color: #f5f5f5;
            margin: 0;
        }

        .login-container {
            background-color: #ffffff;
            padding: 2rem;
            border-radius: 12px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            text-align: center;
            width: 400px;
        }

        .login-container img {
            margin-bottom: 15px;
        }

        /* 메인 로고 크기 조정 */
        .login-container img[alt="Main Logo"] {
            width: 250px;
            height: auto;
            margin-bottom: 20px;
        }

        .login-container h2 {
            margin-bottom: 20px;
            font-size: 1.75rem;
            font-weight: bold;
        }

        /* 로그인 버튼 공통 스타일 */
        .login-button {
            width: 100%;
            padding: 12px;
            border: none;
            border-radius: 8px;
            font-size: 1rem;
            cursor: pointer;
            margin-bottom: 15px;
            display: flex;
            justify-content: center;
            align-items: center;
            font-weight: bold;
            text-align: center;
        }

        /* 구글 및 스포티파이 버튼 색상 */
        .google-btn {
            background-color: #357ae8;
            color: white;
        }

        .spotify-btn {
            background-color: #1db954;
            color: white;
        }

        /* 버튼 안의 이미지 스타일 */
        .login-button img {
            width: 20px;
            height: 20px; /* 아이콘의 크기를 고정 */
            margin-right: 10px; /* 이미지와 텍스트 사이 간격 */
            display: inline-block;
            vertical-align: middle; /* 이미지와 텍스트를 수평으로 맞춤 */
            position: relative;
            top: 7px; /* 텍스트와 이미지를 더 잘 맞추기 위해 미세 조정 */
        }

        /* 로컬 로그인 폼 스타일 */
        .local-login input {
            width: 100%;
            padding: 12px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 8px;
            box-sizing: border-box;
            font-size: 1rem;
        }

        .local-login button {
            background-color: #1db954;
            color: white;
            border: none;
            width: 100%;
            padding: 12px;
            border-radius: 8px;
            cursor: pointer;
            font-size: 1rem;
            font-weight: bold;
        }

        .login-container a {
            color: #007bff;
            text-decoration: none;
            font-size: 0.9rem;
        }

        .login-container a:hover {
            text-decoration: underline;
        }

        .logout-btn {
            margin-top: 20px;
            background-color: #e74c3c;
            color: white;
            border: none;
            width: 100%;
            padding: 12px;
            border-radius: 8px;
            cursor: pointer;
            font-size: 1rem;
            font-weight: bold;
        }

        hr {
            margin: 20px 0;
            border: none;
            border-top: 1px solid #ddd;
        }

        .divider-text {
            position: relative;
            font-size: 0.9rem;
            color: #aaa;
            margin: 20px 0;
            text-align: center;
        }

        .divider-text::before,
        .divider-text::after {
            content: '';
            position: absolute;
            top: 50%;
            width: 45%;
            height: 1px;
            background: #ddd;
        }

        .divider-text::before {
            left: 0;
        }

        .divider-text::after {
            right: 0;
        }

    </style>
    <script>
        // 구글 OAuth2 로그인 처리 (Spring Security를 통해 처리)
        function googleLogin() {
            // Spring Security의 OAuth2 로그인 엔드포인트로 리다이렉트
            window.location.href = "/oauth2/authorization/google";
        }

        // 로그아웃 함수는 유지
        function socialLogout() {
            fetch('/usr/member/doLogout', {
                method: 'POST',
            })
                .then(response => {
                    if (response.ok) {
                        console.log("로그아웃 성공");
                        window.location.href = "/usr/member/login";
                    } else {
                        console.error("로그아웃 실패: ", response.status);
                    }
                })
                .catch(error => {
                    console.error("로그아웃 에러 발생: ", error);
                });
        }
    </script>


</head>
<body>

<div class="login-container">
    <!-- 메인 로고 -->
    <img src="/images/Main_logo.png" alt="Main Logo" width="250" height="auto">

    <h2>Log into your account</h2>

    <!-- 구글 로그인 버튼 -->
    <button class="login-button google-btn" onclick="googleLogin()">
        <img src="/images/google_logo.png" alt="Google Logo">
        Continue with Google
    </button>

    <!-- 스포티파이 로그인 버튼 -->
    <button class="login-button spotify-btn">
        <img src="/images/spofitylogo7377-9doi-200h.png" alt="Spotify Logo">
        Continue with Spotify
    </button>

    <div>
        <span>Don't have an account?
        <p> If you don't have account, try google, or spotify login!! </p>
        </span>
    </div>

    <!-- 로그아웃 버튼 -->
    <button class="logout-btn" onclick="socialLogout()">로그아웃</button>
</div>
</body>
</html>
