<%@ page import="me.hysong.dev.modules.Logger" %>
<%@ page import="me.hysong.dev.apps.transfer.backend.DeletionTrackers" %>
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
%>

<div class="container">
    <h1 class="title">Transfer</h1>
    <p class="versionstr">Build 22Feb2024.A</p>
    <form action="transfer_fupload" method="post" enctype="multipart/form-data">
        <div class="upload-box">
            <input type="file" name="file" id="file" class="inputfile" />
            <label for="file" id="filename" class="file-label">Choose a file</label>
        </div>
        <br>
        <button type="submit" id="upload-button" class="upload-button">Upload</button>
    </form>
    <p class="description">
        File lasts for 3 days.<br>
        Size is 100MB or less.
    </p>
</div>
</body>
</html>
