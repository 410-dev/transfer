<%@ page import="me.hysong.dev.modules.Logger" %>
<%@ page import="me.hysong.dev.apps.transfer.backend.DeletionTrackers" %>
<%@ page import="me.hysong.libhyextended.utils.QRCode" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Transfer</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type='text/css' href="style.css">
    <script>
        window.onload = function() {
            document.getElementById('file').addEventListener('change', function() {
                document.getElementById('filename').textContent = this.files[0].name;
            });
        };
    </script>
</head>
<body>
<%
    Logger.accessLogger(request, response);
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    DeletionTrackers.scanAndDeleteAsynchronously();
    String URL = request.getParameter("url");
    String FILENAME = URL.substring(URL.lastIndexOf('/') + 1);
    String UID = URL.replaceAll("/" + FILENAME, "").substring(URL.replaceAll("/" + FILENAME, "").lastIndexOf('/') + 1);
%>

<div class="container">
    <%
        if (URL != null && !URL.isEmpty() && !URL.equals("null") && !URL.equals("error")) {
    %>
        <h1 class="title">Done</h1>
        <p class="description">
            File is uploaded to server, and you may access to the file via link below:
        </p>
        <div class="url">
            <a class="url" href="<%=URL%>"><%=URL%></a>
        </div>
        <br>
        <p class="description">
            You may also download using curl:
        </p>
        <div class="url">
            <code>curl -L "<%=URL%>" -o <%=FILENAME%></code>
        </div>
        <p class="description">
            Or wget:
        </p>
        <div class="url">
            <code>wget "<%=URL%>" -O <%=FILENAME%></code>
        </div>
        <p class="description">
            Or by scanning the QR code:
        </p>
        <div>
            <img src="<%=QRCode.toBase64(URL, 200, 200)%>" alt="QRCode">
        </div>
        <br>
        <p class="description">
            The file stays in server for 72 hours, and will be deleted after that.
        </p>
        <br><br>
        <p class="description">
            If you want to delete the file immediately, please click the button below:
        </p>
        <div class="url">
            <form action="delete" method="get">
                <input type="hidden" name="u" value="<%=UID%>">
                <input type="hidden" name="f" value="<%=FILENAME%>">
                <button type="submit" id="upload-button" class="upload-button">Delete</button>
            </form><br>
            <code>curl -L "<%=request.getRequestURL().substring(0, request.getRequestURL().indexOf("/done.jsp"))%>/delete?u=<%=UID%>&f=<%=FILENAME%>"</code><br>
            <code>wget "<%=request.getRequestURL().substring(0, request.getRequestURL().indexOf("/done.jsp"))%>/delete?u=<%=UID%>&f=<%=FILENAME%>"</code>
        </div>
        <p class="description">
            You can also delete the file by scanning the following QR code:
        </p>
        <div>
            <img src="<%=QRCode.toBase64(request.getRequestURL().substring(0, request.getRequestURL().indexOf("/done.jsp")) + "/delete?u=" + UID + "&f=" + FILENAME, 200, 200)%>" alt="QRCode">
        </div>
    <%
        } else {
    %>
        <h1 class="title">Error</h1>
        <p class="description">
            There is an error occurred while uploading the file.
        </p>
        <p class="description">
            Please try again later.
        </p>
    <%
        }
    %>
</div>
</body>
</html>
