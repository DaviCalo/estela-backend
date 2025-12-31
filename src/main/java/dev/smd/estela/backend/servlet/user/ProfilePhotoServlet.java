package dev.smd.estela.backend.servlet.user;

import static dev.smd.estela.backend.config.Config.PATH_FILES_USERS;
import dev.smd.estela.backend.service.UserService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

@WebServlet(name = "ProfilePhotoServlet", urlPatterns = {"/api/profilephoto"})
@MultipartConfig(fileSizeThreshold = 1048576, maxFileSize = 52428800, maxRequestSize = 62914560)
public class ProfilePhotoServlet extends HttpServlet {

        private static final int BUFFER_SIZE = 4096;
        private final UserService userService = new UserService();

        @Override
        protected void doPut(HttpServletRequest request, HttpServletResponse response)
                  throws ServletException, IOException {
                response.setContentType("application/json");

                String userIdParam = request.getParameter("userid");
                Long userId;

                if (userIdParam == null || userIdParam.trim().isEmpty()) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parâmetro 'userid' é obrigatório.");
                        return;
                }

                try {
                        userId = Long.valueOf(userIdParam);
                } catch (NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato do ID de usuário inválido.");
                        return;
                }

                Part filePart = null;
                String newFileName = null;

                try {
                        filePart = request.getPart("profilephoto");

                        if (filePart == null || filePart.getSize() == 0 || filePart.getSubmittedFileName().isEmpty()) {
                                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Arquivo 'profilephoto' é obrigatório.");
                                return;
                        }

                        String submittedFileName = filePart.getSubmittedFileName();

                        String fileExtension = "";
                        int dotIndex = submittedFileName.lastIndexOf('.');
                        if (dotIndex > 0 && dotIndex < submittedFileName.length() - 1) {
                                fileExtension = submittedFileName.substring(dotIndex);
                        }

                        newFileName = userId + fileExtension;

                        String uploadPath = PATH_FILES_USERS;

                        File uploadDir = new File(uploadPath);
                        if (!uploadDir.exists()) {
                                uploadDir.mkdirs();
                        }

                        String filePath = uploadPath + File.separator + newFileName;
                        filePart.write(filePath);

                        if (!userService.saveNewProfilePhoto(newFileName, userId)) {
                                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Usuário não encontrado ou não foi possível salvar o nome do arquivo.");
                                return;
                        }

                        response.setStatus(HttpServletResponse.SC_CREATED);
                        response.getWriter().write("{\"mensagem\": \"Upload realizado com sucesso\", \"fileName\": \"" + newFileName + "\"}");
                } catch (ServletException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falha na requisição: certifique-se de que o enctype é 'multipart/form-data'.");
                } catch (IOException e) {
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro interno ao salvar o arquivo.");
                }
        }

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                  throws ServletException, IOException {
                String userIdParam = request.getParameter("userid");
                Long userId;

                if (userIdParam == null || userIdParam.trim().isEmpty()) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parâmetro 'userid' é obrigatório.");
                        return;
                }

                try {
                        userId = Long.valueOf(userIdParam);
                } catch (NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato do ID de usuário inválido.");
                        return;
                }

                String filePath = userService.getProfilePhotoById(userId, request.getServletContext().getRealPath(""));

                if (filePath == null || filePath.trim().isEmpty()) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Foto de perfil não encontrada ou usuário sem foto cadastrada.");
                        return;
                }

                File file = new File(filePath);

                if (!file.exists() || file.isDirectory() || file.length() == 0) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Arquivo físico da foto de perfil não existe ou está vazio.");
                        return;
                }

                String mimeType = getServletContext().getMimeType(filePath);
                if (mimeType == null) {
                        mimeType = "application/octet-stream";
                }

                response.setContentType(mimeType);
                response.setContentLength((int) file.length());
                response.setHeader("Content-Disposition", "attachment; filename=\"" +file.getName() + "\""); 

                try (FileInputStream inputStream = new FileInputStream(file); OutputStream outputStream = response.getOutputStream()) {

                        byte[] buffer = new byte[BUFFER_SIZE];
                        int bytesRead;

                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                        }
                } catch (IOException e) {
                        System.err.println("Erro durante o streaming da imagem: " + e.getMessage());
                }
        }
}
