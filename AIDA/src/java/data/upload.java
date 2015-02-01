/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author system
 */
@WebServlet(name = "upload", urlPatterns = {"/upload"})
public class upload extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {

            if (request.getSession().getAttribute("usr") == null) {
                response.sendError(403);
            }

            if (request.getSession().getAttribute("usr") != null) {

                String usr = (String) request.getSession().getAttribute("usr");
                String pwd = (String) request.getSession().getAttribute("pwd");

                BD.cDatos sql = new BD.cDatos();

                String name = null;
                String ubicacionArchivo = "/home/system/NetBeansProjects/LPTI3/AIDA/web/files"; //para la casa
//String ubicacionArchivo = "C://Users/Alumno/Documents/NetBeansProjects/LPTI3/AIDA/web/files"; //para la escuela
                //Esta ruta se debe de cambiar a donde este la carpeta profile_images del pryecto, es ideal si esta en un servidor ya que la ruta seria fija
                DiskFileItemFactory factory = new DiskFileItemFactory();
                factory.setSizeThreshold(4096);
                factory.setRepository(new File(ubicacionArchivo));

                ServletFileUpload upload = new ServletFileUpload(factory);

                try {
                    List<FileItem> partes = upload.parseRequest(request);

                    for (FileItem item : partes) {
                        File file = new File(ubicacionArchivo, item.getName());
                        item.write(file);
                        name = item.getName();
                    }

                    out.print("<script>alert('El archivo se subio correctamente');</script>");

                } catch (FileUploadException ex) {
                    out.write("Error al subir archivo " + ex.getMessage());
                } catch (Exception ex) {
                    Logger.getLogger(upload.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    sql.conectar();

                    sql.consulta("call sp_alta_doc('" + usr + "','" + pwd + "','" + name + "')");

                    sql.cierraConexion();

                } catch (SQLException ex) {
                    System.out.println("SQL ERROR: " + ex.toString());
                }
                
                response.sendRedirect("./menu.jsp");
                

            }

        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
