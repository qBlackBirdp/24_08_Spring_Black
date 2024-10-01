<%@ include file="../common/head.jspf" %>


<h1 style="margin-top: 100px;">Upload your WAV file</h1>

<!-- 파일 업로드 폼 -->
<form action="${pageContext.request.contextPath}/upload" method="post" enctype="multipart/form-data">
    <%--@declare id="file"--%>
    <%--@declare id="name"--%>
    <%--@declare id="bpm"--%>
    <%--@declare id="genres"--%>
    <%--@declare id="isoneshot"--%>
    <label for="file">Select WAV File:</label>
    <input type="file" name="file" accept=".wav" required>
    <br><br>

    <!-- 악기 대분류를 선택하는 드롭다운 -->
    <select id="instrumentSelect" name="instrument">
        <option value="">Select an instrument</option>
        <c:forEach var="instrument" items="${instruments}">
            <option value="${instrument}">${instrument}</option>
            <!-- 대분류 악기 타입 표시 -->
        </c:forEach>
    </select>

    <!-- 세부 항목을 선택하는 드롭다운 -->
    <select id="instrumentItemsSelect" name="instrumentItem">
        <option value="">Select an instrument item</option>
    </select>
    <br><br>

    <label for="name">Sample Name:</label>
    <input type="text" name="name" required>
    <br><br>

    <label for="bpm">BPM (Optional):</label>
    <input type="number" name="bpm">
    <br><br>

    <label for="genres">Genre:</label>
    <input type="text" name="genres" required>
    <br><br>

    <label for="isOneShot">Is One-Shot:</label>
    <input type="checkbox" name="isOneShot">
    <br><br>

    <%--    <label for="price">Price (Optional):</label>--%>
    <%--    <input type="number" name="price" min="0" value="0">--%>
    <br><br>

    <button type="submit">Upload</button>
</form>

<script type="text/javascript">
    $(document).ready(function () {
        // 대분류 악기 선택 시 AJAX 요청
        $('#instrumentSelect').change(function () {
            var instrumentType = $(this).val(); // 대분류 악기 타입

            if (instrumentType) {
                $.ajax({
                    url: '/getInstrumentItems',
                    type: 'GET',
                    data: {instrumentType: instrumentType},  // 대분류 악기 타입을 서버에 전달
                    success: function (items) {
                        // 세부 항목 셀렉트 박스 비우기
                        $('#instrumentItemsSelect').empty();
                        $('#instrumentItemsSelect').append('<option value="">Select an instrument item</option>');
                        // 세부 항목 추가
                        $.each(items, function (index, item) {
                            $('#instrumentItemsSelect').append('<option value="' + item.id + '">' + item.itemsName + '</option>');
                        });
                    }
                });
            } else {
                // 악기 선택 해제 시 세부 항목 비우기
                $('#instrumentItemsSelect').empty();
                $('#instrumentItemsSelect').append('<option value="">Select an instrument item</option>');
            }
        });
    });
</script>

<!-- 푸터 불러오기 -->
<%@ include file="../common/foot.jspf" %>
