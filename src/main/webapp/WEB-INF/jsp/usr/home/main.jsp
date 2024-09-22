<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<html>
<head>
    <title>Main</title>

    <!-- 번들된 JS 파일을 불러오기 -->
    <script src="http://localhost:8082/bundle.js"></script>

    <!-- JavaScript에서 사용할 Access Token 전달 -->
    <script type="text/javascript">
        var accessToken = "${accessToken}";  // Controller에서 전달된 Access Token
        console.log("Access Token: ", accessToken);
    </script>

    <script type="text/javascript">
        // Spotify API로 Lofi 트랙 검색
        function searchLofiTracks() {
            fetch(`https://api.spotify.com/v1/search?q=lofi&type=track&limit=10`, {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + accessToken
                }
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`HTTP error! status: ${response.status}, message: ${response.statusText}`);
                    }
                    return response.json();
                })
                .then(data => {
                    console.log('Lofi Tracks: ', data);

                    if (data.tracks && data.tracks.items) {
                        const tracks = data.tracks.items;
                        let trackListHTML = '';  // HTML을 저장할 변수

                        tracks.forEach(track => {
                            // 아티스트 정보 확인
                            let artistNames = "Unknown Artist";
                            if (track.artists && track.artists.length > 0) {
                                artistNames = track.artists.map(artist => artist.name).join(', ');
                            }

                            // 트랙 이름과 트랙 ID
                            let trackName = track.name ? track.name : "Unknown Track";
                            let trackId = track.id ? track.id : "Invalid ID";

                            console.log('Track ID: ', trackId);
                            console.log('Artist Names: ', artistNames);
                            console.log('Track Name: ', trackName);

                            trackListHTML += `
                    <div>
                        <p><strong>${'${track.name}'}</strong> by ${'${artistNames}'}</p>
                        <button onclick="playTrack('${'${trackId}'}')">Play</button>
                    </div>
                `;
                        });

                        // 생성된 HTML을 실제 페이지에 삽입
                        document.getElementById('track-list').innerHTML = trackListHTML;

                    } else {
                        console.error('No tracks found in the API response.');
                        document.getElementById('track-list').innerHTML = "<p>No tracks found</p>";
                    }
                })
                .catch(error => {
                    console.error('Error fetching Lofi tracks:', error);
                });
        }

        // 선택한 트랙을 재생하는 함수
        function playTrack(trackId) {
            console.log("Playing track with ID: ", trackId);  // 트랙 ID 확인
            if (!trackId || trackId === "Invalid ID") {
                console.error("Invalid track ID");
                return;
            }

            document.getElementById('spotify-player').innerHTML = `
        <iframe src="https://open.spotify.com/embed/track/${'${trackId}'}" width="100%" height="80" frameborder="0" allowtransparency="true" allow="encrypted-media"></iframe>
    `;
        }

        // 페이지 로드 시 기본적으로 Lofi 트랙 검색
        window.onload = function () {
            searchLofiTracks();  // 'lofi' 검색어로 트랙 검색
        };
    </script>


    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">

</head>
<body>

<!-- 헤더 불러오기 -->
<%@ include file="../common/head.jspf" %>

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
        </div>

        <h3>Browse instruments</h3>
        <div>
            <button class="instrument">Drums</button>
            <button class="instrument">Vocals</button>
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
    <!-- 트랙 목록이 표시될 영역 -->
    <div id="track-list">
        <!-- JavaScript에서 생성된 트랙 목록이 여기에 표시. -->
    </div>

    <!-- Spotify 플레이어가 표시될 영역 -->
    <div id="spotify-player" style="width: 100%; text-align: center; margin-top: 20px;">
        <!-- 선택된 트랙 여기서 재생. -->
    </div>
</main>


<!-- 푸터 불러오기 -->
<%@ include file="../common/foot.jspf" %>

</body>
</html>
