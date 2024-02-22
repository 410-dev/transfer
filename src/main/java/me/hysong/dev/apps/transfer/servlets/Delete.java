package me.hysong.dev.apps.transfer.servlets;

import com.google.gson.JsonObject;
import me.hysong.dev.apps.transfer.backend.DeletionTrackers;
import me.hysong.dev.modules.PathFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@WebServlet("/delete")
public class Delete extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        String unique = request.getParameter("u");
        String file = request.getParameter("f");

        JsonObject responseObject = new JsonObject();

        if (unique == null || file == null) {
            responseObject.addProperty("status", "error");
            responseObject.addProperty("message", "Missing parameters");
            response.getWriter().write(responseObject.toString());
            response.sendError(400);
            return;
        }

        if (unique.isEmpty() || file.isEmpty()) {
            responseObject.addProperty("status", "error");
            responseObject.addProperty("message", "Empty parameters");
            response.getWriter().write(responseObject.toString());
            response.sendError(400);
            return;
        }

        if (unique.contains("/") || file.contains("/") || unique.contains("\\") || file.contains("\\") || unique.contains("../") || file.contains("../")) {
            responseObject.addProperty("status", "error");
            responseObject.addProperty("message", "Invalid parameters");
            response.getWriter().write(responseObject.toString());
            response.sendError(400);
            return;
        }

        File targetFile = new File(PathFactory.getPath() + "files/" + unique + "/" + file);
        File metaFile = new File(PathFactory.getPath() + "metas/" + unique + "." + file + ".meta");

        if (!targetFile.exists()) {
            System.out.println("[Transfer] Deletion request, but file does not exist: " + targetFile.getAbsolutePath());
            responseObject.addProperty("status", "error");
            responseObject.addProperty("message", "File does not exist");
            response.getWriter().write(responseObject.toString());
            response.sendError(404);
            return;
        }

        if (!metaFile.exists()) {
            System.out.println("[Transfer] Deletion request, but meta file does not exist: " + metaFile.getAbsolutePath());
            responseObject.addProperty("status", "error");
            responseObject.addProperty("message", "Meta file does not exist");
            response.sendError(404);
            return;
        }

        if (!targetFile.delete()) {
            System.out.println("[Transfer] Deletion request, but failed to delete file: " + targetFile.getAbsolutePath());
            responseObject.addProperty("status", "error");
            responseObject.addProperty("message", "Failed to delete file");
            response.getWriter().write(responseObject.toString());
            response.sendError(500);
            return;
        }

        if (!metaFile.delete()) {
            System.out.println("[Transfer] Deletion request, but failed to delete meta file: " + metaFile.getAbsolutePath());
            responseObject.addProperty("status", "error");
            responseObject.addProperty("message", "Failed to delete meta file");
            response.sendError(500);
            return;
        }

        File[] files = targetFile.getParentFile().listFiles();
        if (files == null || files.length == 0) {
            if (!targetFile.getParentFile().delete()) {
                System.out.println("[Transfer] Deletion request, but failed to delete directory: " + targetFile.getParentFile().getAbsolutePath());
                responseObject.addProperty("status", "warning");
                responseObject.addProperty("message", "Failed to delete empty directory");
                response.setStatus(200);
                return;
            }
        }

        responseObject.addProperty("status", "success");
        responseObject.addProperty("message", "File deleted");
        response.getWriter().write(responseObject.toString());
        response.setStatus(200);

        DeletionTrackers.scanAndDeleteAsynchronously();

    }

}
