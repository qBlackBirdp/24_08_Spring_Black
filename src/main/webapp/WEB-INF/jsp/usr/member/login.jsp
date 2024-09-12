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
        document.addEventListener("DOMContentLoaded", function () {
            const form = document.getElementById('loginForm');

            if (form) {
                form.addEventListener('submit', function (event) {
                    event.preventDefault();

                    const email = document.querySelector('input[name="email"]').value;
                    const password = document.querySelector('input[name="loginPw"]').value;

                    // Ajax 요청으로 로그인 처리
                    fetch('/usr/member/doLocalLogin', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: new URLSearchParams({
                            email: email,
                            loginPw: password
                        })
                    })
                        .then(response => {
                            // JSON인지 HTML인지 확인하여 처리
                            const contentType = response.headers.get('content-type');
                            if (contentType && contentType.indexOf('application/json') !== -1) {
                                return response.json();  // JSON 응답 처리
                            } else {
                                return response.text();  // HTML 응답 처리 (에러 페이지일 수 있음)
                            }
                        })
                        .then(data => {
                            // 데이터 타입이 object일 경우 JSON 응답이므로 처리
                            if (typeof data === 'object' && data.resultCode && data.resultCode.startsWith('S-')) {
                                // 로그인 성공 시 처리
                                console.log("로그인 성공: ", data.data1);  // 로그인된 사용자 정보 콘솔 출력
                                    window.location.href = "/usr/home/main";  // 5초 후 리디렉션
                                // window.location.href = "/usr/home/main";  // 로그인 성공 후 리다이렉트
                            } else if (typeof data === 'object' && data.resultCode) {
                                // 로그인 실패 시 처리 (JSON 응답인 경우)
                                console.error("로그인 실패: ", data.msg);  // 실패 메시지 출력
                                alert(data.msg);  // 에러 메시지 표시
                            } else {
                                // HTML 응답인 경우 (예: 에러 페이지)
                                console.error("예상치 못한 응답 형식: ", data);  // HTML 에러 메시지 출력
                                alert("Unexpected response: " + data);  // HTML 응답을 표시
                            }
                        })
                        .catch(error => {
                            console.error("로그인 에러: ", error);
                        });
                });
            }

        // 로그아웃 버튼 클릭 이벤트
            const logoutButton = document.getElementById('logoutBtn');
            if (logoutButton) {
                logoutButton.addEventListener('click', function () {
                    fetch('/usr/member/doLogout', {
                        method: 'POST',
                    })
                        .then(response => response.json())
                        .then(data => {
                            if (data.ResultCode.startsWith('S-')) {
                                console.log("로그아웃 성공");
                                window.location.href = "/usr/member/login";  // 로그아웃 후 로그인 페이지로 리다이렉트
                            } else {
                                console.error("로그아웃 실패: ", data.msg);
                            }
                        })
                        .catch(error => {
                            console.error("로그아웃 에러: ", error);
                        });
                });
            }
        });

        function googleLogin() {
            signInWithPopup(auth, provider)
                .then((result) => {
                    console.log("로그인 성공: ", result.user);
                    window.location.href = "/usr/home/main";
                })
                .catch((error) => {
                    console.error("Google 로그인 에러: ", error);
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

    <!-- 구분선 및 or 텍스트 -->
    <div class="divider-text">or</div>

    <!-- 로컬 로그인 폼 -->
    <form class="local-login" id="loginForm">
        <input type="email" name="email" placeholder="Email address" required>
        <input type="password" name="loginPw" placeholder="Password" required>
        <button type="submit">Continue</button>
    </form>

    <div>
        <p>Don't have an account? <a href="/usr/member/join">Sign up</a></p>
    </div>

    <!-- 로그아웃 버튼 -->
    <button class="logout-btn" onclick="socialLogout()">로그아웃</button>
</div>
</body>
</html>
