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
    <%--악기선택--%>
    <select id="instrumentSelect">
        <c:forEach var="instrument" items="${instruments}">
            <option value="${instrument.id}">${instrument.istmName}</option>
        </c:forEach>
    </select>
    <%--항목선택--%>
    <select id="instrumentItemsSelect">
        <option value="">악기 항목을 선택하세요</option>
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

<script>
    $(document).ready(function () {
        $('#instrumentSelect').change(function () {
            var instrumentId = $(this).val();
            if (instrumentId) {
                $.ajax({
                    url: '/getInstrumentItems',
                    data: {instrumentId: instrumentId},
                    success: function (items) {
                        $('#instrumentItemsSelect').empty();
                        $.each(items, function (index, item) {
                            $('#instrumentItemsSelect').append('<option value="' + item.id + '">' + item.itemsName + '</option>');
                        });
                    }
                });
            } else {
                $('#instrumentItemsSelect').empty().append('<option value="">악기 항목을 선택하세요</option>');
            }
        });
    });
</script>
<!-- 푸터 불러오기 -->
<%@ include file="../common/foot.jspf" %>
