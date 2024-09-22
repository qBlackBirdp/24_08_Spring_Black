<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    main {
        text-align: center;
        padding: 50px;
        background-color: #2e2e2e; /* 메인 콘텐츠 영역 배경 다크 */
    }

    h1, h3 {
        color: #fdd835; /* Lofi 색상 강조 */
    }

    input[type="text"] {
        padding: 10px;
        width: 300px;
        background-color: #3a3a3a;
        color: #fff;
        border: 1px solid #555;
    }

    button {
        padding: 10px 20px;
        background-color: #00bfff;
        color: #fff;
        border: none;
        cursor: pointer;
    }

    button:hover {
        background-color: #009fd4;
    }

    button.genre, button.instrument {
        margin: 5px;
        padding: 10px;
        background-color: #3a3a3a;
        border: 1px solid #f0f0f0;
        color: #fff;
    }

    button.genre:hover, button.instrument:hover {
        background-color: #555;
    }

    .flex-container {
        margin-top: 50px;
        display: flex;
        justify-content: space-around;
    }

    .flex-container div {
        background-color: #333;
        padding: 20px;
        border-radius: 10px;
        width: 30%;
        text-align: center;
    }

    .flex-container div p {
        color: #aaa;
    }

</style>

<html>
<head>
    <title>Main</title>

    <!-- 번들된 JS 파일을 불러오기 -->
    <script src="http://localhost:8082/bundle.js"></script>

</head>
<body>

<!-- 헤더 불러오기 -->
<%@ include file="../common/head.jspf"%>

<main>
    <h1>The Lofi best sample library</h1>
    <p>Try free for 14 days</p>

    <div style="margin: 20px 0;">
        <input type="text" placeholder="Search any sound like 808 kick">
        <button>Search</button>
    </div>

    <div style="margin-top: 20px;">
        <h3>Browse genres</h3>
        <div>
            <button class="genre">Hip Hop</button>
            <button class="genre">Drum and Bass</button>
            <!-- 더 많은 장르 추가 가능 -->
        </div>

        <h3>Browse instruments</h3>
        <div>
            <button class="instrument">Drums</button>
            <button class="instrument">Vocals</button>
            <!-- 더 많은 악기 추가 가능 -->
        </div>
    </div>

    <div class="flex-container">
        <div>
            <h3>100% royalty free</h3>
            <p>Use your sounds anywhere, cleared for commercial use.</p>
        </div>
        <div>
            <h3>Yours forever</h3>
            <p>Every sound you download is yours to keep.</p>
        </div>
        <div>
            <h3>Cancel anytime</h3>
            <p>No commitments. Cancel whenever you want.</p>
        </div>
    </div>
</main>

<!-- 푸터 불러오기 -->
<%@ include file="../common/foot.jspf"%>

</body>
</html>
