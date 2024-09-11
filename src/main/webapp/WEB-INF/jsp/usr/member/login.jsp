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

    <style>
        /* 공통 스타일 */
        body {
            font-family: 'Inter', sans-serif;
            background-color: #f9f9f9;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        .container {
            text-align: center;
            background-color: #fff;
            padding: 40px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }

        h1 {
            margin-bottom: 20px;
        }

        .button {
            width: 100%;
            padding: 12px;
            margin-bottom: 15px;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .google-button {
            background-color: #4285f4;
            color: white;
        }

        .spotify-button {
            background-color: #1db954;
            color: white;
        }

        .local-login-form input {
            width: 100%;
            padding: 12px;
            margin-bottom: 15px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
        }

        .local-login-form button {
            width: 100%;
            padding: 12px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .local-login-form button:hover {
            background-color: #45a049;
        }

        /* 아이콘 스타일 */
        .icon {
            width: 20px;
            height: 20px;
            margin-right: 8px;
        }

        .link {
            display: block;
            margin-top: 10px;
            font-size: 14px;
            color: #007BFF;
            text-decoration: none;
        }

        .link:hover {
            text-decoration: underline;
        }

        .logout-btn {
            margin-top: 20px;
            padding: 10px;
            background-color: #e74c3c;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
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
                        .then(response => response.json())
                        .then(data => {
                            if (data.ResultCode.startsWith('S-')) {
                                console.log("로그인 성공: ", data.data1);  // 로그인된 사용자 정보 콘솔 출력
                                window.location.href = "/usr/home/main";  // 로그인 성공 후 메인 페이지로 리다이렉트
                            } else {
                                console.error("로그인 실패: ", data.msg);
                                alert(data.msg);  // 실패 메시지 표시
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
<div class="container">
    <h1>Log into your account</h1>

    <!-- Google 로그인 버튼 -->
    <button class="button google-button" onclick="googleLogin()">
        <img src="/path/to/your/google-logo.png" alt="Google Logo" class="icon"/>
        Continue with Google
    </button>

    <!-- Spotify 로그인 버튼 -->
    <button class="button spotify-button">
        <img src="/images/spofitylogo7377-9doi-200h.png" alt="Spotify Logo" class="icon"/>
        Continue with Spotify
    </button>

    <!-- 구분선 -->
    <div>or</div>

    <!-- 로컬 로그인 폼 -->
    <form class="local-login-form" id="loginForm">
        <input type="email" name="email" placeholder="Email address" required/>
        <input type="password" name="loginPw" placeholder="Password" required/>
        <button type="submit">Continue</button>
    </form>

    <a href="/usr/member/join" class="link">Don't have an account? Sign up</a>

    <!-- 로그아웃 버튼 -->
    <button id="logoutBtn" class="logout-btn">로그아웃</button>
</div>
</body>
</html>
