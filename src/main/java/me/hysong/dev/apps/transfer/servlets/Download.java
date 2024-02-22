package me.hysong.dev.apps.transfer.servlets;

import me.hysong.dev.apps.transfer.backend.DeletionTrackers;
import me.hysong.dev.modules.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@WebServlet("/f/*")
public class Download extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if there's any parameter
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        Logger.accessLogger(request, response);

        DeletionTrackers.scanAndDelete();

        String[] paths = request.getRequestURI().split("/");

        // 3: Unique ID
        // 4: File name
        if (paths.length != 5) {
            response.sendError(404);
            return;
        }

        String uniqueID = paths[3];
        String fileName = paths[4];


        // Redirect to the file
        String url = request.getRequestURL().toString().split(request.getContextPath())[0] + request.getContextPath() + "/files/" + uniqueID + "/" + fileName;

        response.sendRedirect(URLDecoder.decode(url, StandardCharsets.UTF_8));
    }
}
