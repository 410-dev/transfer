package me.hysong.dev.apps.transfer.servlets;

import me.hysong.dev.apps.transfer.backend.DeletionTrackers;
import me.hysong.dev.modules.Logger;
import me.hysong.dev.modules.PathFactory;
import me.hysong.libhycore.CoreSHA;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;


@WebServlet("/transfer_fupload")
@MultipartConfig(maxFileSize = 1024 * 1024 * 1024, // 1024 MB
        maxRequestSize = 1024 * 1024 * 1100, // 1100 MB
        fileSizeThreshold = 1024 * 1024 * 1024) // 1024 MB
public class Upload extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(404);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if there's any parameter
        Logger.accessLogger(request, response);
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try{
            DeletionTrackers.scanAndDeleteAsynchronously();
            // Get file part from the request
            Part filePart = request.getPart("file");
            String fileName = filePart.getSubmittedFileName();
            if (fileName == null || fileName.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/done.jsp?url=error");
            }

            // Create input stream from file part
            InputStream inputStream = filePart.getInputStream();

            // Specify the path to save the uploaded file
            String randomURL = CoreSHA.hash512(UUID.randomUUID().toString() + System.currentTimeMillis()).substring(0, 5);
            String uploadPath = PathFactory.getPath() + "/files/" + randomURL + "/" + fileName;
            Logger.simpleLog("[Transfer] Upload path: " + uploadPath);
            String webURL = request.getRequestURL().toString().split(request.getContextPath())[0] + request.getContextPath() + "/f/" + randomURL + "/" + fileName;
            webURL = URLEncoder.encode(webURL, StandardCharsets.UTF_8);

            // Create a file output stream to write the uploaded file to disk
            File file = new File(uploadPath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileOutputStream outputStream = new FileOutputStream(uploadPath);

            // Read the bytes of the uploaded file and write them to the output stream
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            Logger.simpleLog("[Transfer] Upload path: " + uploadPath);

            // Close the streams
            inputStream.close();
            outputStream.close();

            // Set deletion time at /metas/<randomURL>/<fileName>.meta
            File metaFile = new File(PathFactory.getPath() + "/metas/" + randomURL + "." + fileName + ".meta");
            if (!metaFile.getParentFile().exists()) {
                metaFile.getParentFile().mkdirs();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(metaFile));
            int days = 3;
            writer.write(String.valueOf(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * days));
            writer.close();

            // Redirect to done page
            response.sendRedirect(request.getContextPath() + "/done.jsp?url=" + webURL);
        }catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/done.jsp?url=error");
        }
    }
}
