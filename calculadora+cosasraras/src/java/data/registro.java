/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author system
 */
@WebServlet(name = "registro", urlPatterns = {"/registro"})
public class registro extends HttpServlet {

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
            /* TODO output your page here. You may use following sample code. */
            
            out.print("<head>");
            out.print("<title>");
            out.print("Notificacion");
            out.print("</title>");
            out.print("</head>");
            String u = request.getParameter("uss");
            String p = request.getParameter("pww");
            String a = request.getParameter("apodo");
            
               Crypto.Crypter16914 cryp = new Crypto.Crypter16914(); //Clase que cifra mediante cifrado cesar Distinguiendo Mayusculas de Minusculas
            
            String u_cryp = cryp.code(u);
            String p_cryp = cryp.code(p);
            String a_cryp = cryp.code(a);
            
            BD.cDatos sql = new BD.cDatos();
            
            try {
                sql.conectar();
                
                ResultSet rs = sql.consulta("call sp_alta('"+u_cryp+"','"+p_cryp+"','"+a_cryp+"')");
                
                if(rs.next())
                {
                    String id = rs.getString("id_getted");
                    
                    if(id.equals("Ya existe el usuario"))
                    {
                        out.print("<h2> El usuario ya esta registrado </h2>");
                    }
                    else
                    {
                         out.print("<h2> Registro exitoso :D </h2>");
                         out.print("Eres el usuario #"+id+" en registrarse");
                    }
                }
                
               
            } catch (Exception e) {
                
                out.print("Algo salio mal");
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
