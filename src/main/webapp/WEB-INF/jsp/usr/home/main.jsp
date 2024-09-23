<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<html>
<head>
    <title>Main</title>

    <!-- 번들된 JS 파일을 불러오기 -->
    <script src="http://localhost:8082/bundle.js"></script>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">

    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css"
    />

</head>
<body>

<!-- 헤더 불러오기 -->
<%@ include file="../common/head.jspf" %>

<main>
    <h1>The Lofi best sample library</h1>

    <div style="margin: 20px 0;">
        <input type="text" placeholder="Search any sound like 808 kick">
        <button>Search</button>
    </div>

    <div style="margin-top: 20px;">
        <h3>Browse genres</h3>
        <div>
            <button class="genre">Hip Hop</button>
            <button class="genre">Drum and Bass</button>
        </div>

        <h3>Browse instruments</h3>
        <div>
            <button class="instrument">Drums</button>
            <button class="instrument">Vocals</button>
        </div>
    </div>

    <div class="flex-container" style="margin-bottom: 20px;">
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
    <%--    <!-- 트랙 목록이 표시될 영역 -->--%>
    <%--    <div id="track-list">--%>
    <%--        <!-- JavaScript에서 생성된 트랙 목록이 여기에 표시. -->--%>
    <%--    </div>--%>


    <div id="playlist-list-container" class="slider-container">
        <div id="playlist-list" class="slider">
            <!-- 각각의 플레이리스트 카드 -->
        </div>
    </div>
    <!-- Spotify 플레이어가 표시될 영역 -->
    <div id="spotify-player" style="width: 100%; text-align: center; margin-top: 30px;">
        <!-- 선택된 트랙 여기서 재생. -->
    </div>
</main>


<!-- 푸터 불러오기 -->
<%@ include file="../common/foot.jspf" %>

<!-- JavaScript에서 사용할 Access Token 전달 -->
<script type="text/javascript">
    var accessToken = "${accessToken}";  // Controller에서 전달된 Access Token
    var provider = "${provider}";  // Controller에서 전달된 로그인 제공자

    console.log("Access Token: ", accessToken);
    console.log("Provider: ", provider);

    // 사용자가 인증되지 않았을 경우 안내 메시지 표시
    if (!accessToken) {
        console.log("사용자가 인증되지 않았습니다. 트랙 목록을 표시할 수 없습니다.");
    }
</script>

<script>
    // Spotify API로 lofi 및 chillhop 관련 플레이리스트 검색하기
    function fetchPlaylists() {

        console.log('Access Token:', accessToken);
        console.log('Provider:', provider);

        if (!accessToken || provider === "google") {
            document.getElementById('playlist-list').innerHTML = "<p>Spotify 로그인 후 플레이리스트를 볼 수 있습니다.</p>";
            return;
        }

        console.log('Starting fetch for playlists...');

        // lofi 및 chillhop 키워드로 플레이리스트 검색, offset을 랜덤으로 설정
        const randomOffset = Math.floor(Math.random() * 950); // 0부터 950 사이의 랜덤 값 (50개씩 가져올 경우)

        fetch(`https://api.spotify.com/v1/search?q=lofi%20chillhop&type=playlist&limit=50&offset=${'${randomOffset}'}`, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + accessToken
            }
        })
            .then(response => {
                console.log('Response received: ', response.status);
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}, message: ${response.statusText}`);
                }
                return response.json();
            })
            .then(data => {
                console.log('Playlists: ', data); // 전체 플레이리스트 데이터를 출력

                if (data.playlists && data.playlists.items) {
                    const playlists = data.playlists.items;

                    // 플레이리스트가 없을 때
                    if (playlists.length === 0) {
                        document.getElementById('playlist-list').innerHTML = "<p>관련 플레이리스트가 없습니다.</p>";
                        return;
                    }

                    let playlistListHTML = '';  // HTML을 저장할 변수

                    playlists.forEach(playlist => {
                        let playlistName = playlist.name ? playlist.name : "Unknown Playlist";
                        let playlistId = playlist.id ? playlist.id : "Invalid ID";

                        // 이미지가 있는지 확인하고, 없다면 기본 이미지 사용
                        let playlistImage = (playlist.images && playlist.images.length > 0) ? playlist.images[0].url : 'https://via.placeholder.com/150'; // 기본 이미지 URL

                        playlistListHTML += `
                        <div class="playlist-card animate__animated animate__fadeInUp slide">
                            <img src="${'${playlistImage}'}" alt="${'${playlistName}'}" width="150" height="150" onclick="playPlaylist('${'${playlistId}'}')">
                            <p><strong>${'${playlistName}'}</strong></p>
                            <button onclick="playPlaylist('${'${playlistId}'}')">Play</button>
                        </div>
                    `;
                    });

                    document.getElementById('playlist-list').innerHTML = playlistListHTML;

                } else {
                    console.error('No playlists found in the API response.');
                    document.getElementById('playlist-list').innerHTML = "<p>No playlists found</p>";
                }
            })
            .catch(error => {
                console.error('Error fetching playlists:', error);
                document.getElementById('playlist-list').innerHTML = "<p>플레이리스트를 불러오는 도중 오류가 발생했습니다.</p>";
            });
    }

    // 선택한 플레이리스트 재생하기
    function playPlaylist(playlistId) {
        if (!playlistId || playlistId === "Invalid ID") {
            console.error("Invalid playlist ID");
            return;
        }

        document.getElementById('spotify-player').innerHTML = `
            <iframe src="https://open.spotify.com/embed/playlist/${'${playlistId}'}" width="100%" height="380" frameborder="0" allowtransparency="true" allow="encrypted-media"></iframe>
        `;
    }

    // 페이지 로드 시 플레이리스트를 불러오는 함수 호출
    window.onload = function () {
        console.log("Page loaded, executing functions...");
        fetchPlaylists();
    };
</script>

<style>
    #playlist-list-container {
        width: 100%;
        overflow: hidden; /* 넘치는 카드는 안 보이게 설정 */
    }

    #playlist-list {
        display: flex;
        gap: 20px;
        transition: transform 10s ease; /* 부드러운 이동 애니메이션 */
    }

    /* 다크 테마와 기본적인 카드 스타일 */
    .playlist-card {
        min-width: 200px;
        text-align: center;
        background-color: #1e1e1e; /* 다크 테마 배경색 */
        padding: 20px;
        border-radius: 10px;
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.3); /* 그림자 추가 */
        color: white; /* 텍스트 흰색 */
        position: relative;
        overflow: hidden; /* 이미지가 확대될 때 넘치지 않도록 설정 */
    }

    .playlist-card img {
        width: 100%;
        height: 150px; /* 원하는 높이를 설정 */
        object-fit: cover; /* 이미지의 비율을 유지하면서 카드 크기에 맞게 조정 */
        border-radius: 8px;
        transition: transform 0.5s ease; /* 이미지 확대 효과를 위해 transition 추가 */
        cursor: pointer;
    }

    .playlist-card:hover img {
        transform: scale(1.1); /* 호버 시 이미지 확대 */
        animation: animate__pulse; /* animate.css의 pulse 효과 */
    }

    /* 플레이 버튼 스타일 */
    .playlist-card button {
        background-color: #1db954; /* Spotify 색상 */
        color: white;
        padding: 10px 20px;
        font-size: 16px;
        border: none;
        border-radius: 20px;
        cursor: pointer;
        margin-top: 10px;
        transition: background-color 0.3s ease;
    }

    .playlist-card button:hover {
        background-color: #1aa34a; /* Hover 시 더 어두운 초록색 */
    }

    /* 텍스트 스타일 */
    .playlist-card p {
        font-size: 18px;
        margin-top: 10px;
        font-weight: bold;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis; /* 긴 텍스트 잘림 */
    }

    /* 슬라이더 관련 스타일 */
    .slider-container {
        width: 100%;
        overflow: hidden;
    }

    .slider {
        display: flex;
        width: 500%; /* 5개의 아이템을 한 줄에 */
        animation: slide 30s infinite;
        animation-delay: 5s; /* 5초 후에 시작 */
    }

    .slide {
        width: 20%; /* 5개의 아이템 각각 20% */
    }

    @keyframes slide {
        0% {
            transform: translateX(0%);
        }
        20% {
            transform: translateX(-20%);
        }
        40% {
            transform: translateX(-40%);
        }
        60% {
            transform: translateX(-60%);
        }
        80% {
            transform: translateX(-80%);
        }
        100% {
            transform: translateX(-100%);
        }
    }
</style>

</body>
</html>
