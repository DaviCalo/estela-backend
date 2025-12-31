package dev.smd.estela.backend.servlet.game;

import static dev.smd.estela.backend.config.Config.PATH_FILES_GAMES;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

@WebServlet(name = "DownloadMediaGameServlet", urlPatterns = {"/api/game/media/*"})
public class DownloadMediaGameServlet extends HttpServlet {

    private static final int BUFFER_SIZE = 4096;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.isEmpty() || "/".equals(pathInfo)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nome do arquivo ausente no caminho da URL (Ex: /api/game/cover/nome_do_arquivo.jpg).");
            return;
        }

        String fileName = pathInfo.startsWith("/") ? pathInfo.substring(1) : pathInfo;

        if (fileName.contains("..") || fileName.contains(File.separator)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Nome de arquivo inválido ou tentativa de acesso não autorizado.");
            return;
        }

        String fullFilePath = PATH_FILES_GAMES + File.separator + fileName;

        File arquivoDownload = new File(fullFilePath);

        if (!arquivoDownload.exists() || arquivoDownload.isDirectory() || arquivoDownload.length() == 0) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Arquivo de capa não encontrado ou está vazio: " + fileName);
            return;
        }

        String mimeType = getServletContext().getMimeType(fileName);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        response.setContentType(mimeType);
        response.setContentLengthLong(arquivoDownload.length());

        response.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");

        try (FileInputStream fis = new FileInputStream(arquivoDownload); OutputStream out = response.getOutputStream()) {

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();

        } catch (IOException e) {
            System.err.println("Erro durante o streaming do arquivo: " + fileName + ". Detalhes: " + e.getMessage());
        }
    }
}
