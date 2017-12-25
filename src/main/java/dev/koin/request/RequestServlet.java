/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.koin.request;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

import dev.koin.register.RegisterService;
import dev.koin.transaction.TransactionService;

/**
 * @author akargarm
 */
@WebServlet(name = "RequestServlet", urlPatterns = {"/RequestServlet"})
public class RequestServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RequestServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RequestServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

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
        RequestService req = new RequestService();
        TransactionService transaction = new TransactionService();
        
        //User input from HTML form
        String username = request.getParameter("username");
        String koin = request.getParameter("koin");
        BigInteger koinBigInteger = new BigInteger(koin);
        String password = request.getParameter("password");
        
        BigDecimal bigIntegerToBigDecimal = new BigDecimal(koinBigInteger);
        BigDecimal conversionRate = BigDecimal.valueOf(50000);
        
        BigDecimal etherToExchange = bigIntegerToBigDecimal.divide(conversionRate);
        
        System.out.println(koinBigInteger.toString());
        System.out.println(etherToExchange.toString());
        
        Connection con = RegisterService.connectToDB();
        
        //Defining parameters for addFileToKeystore function
        ResultSet rs = RegisterService.findUser(con, req, username);
        Credentials credentials = null;
        try {
                System.out.println("HEREEE");
                rs.next();
                credentials = req.connectToEthereumWallet(password, "src/main/resources/", rs.getString(6));
        } catch (SQLException ex) {
            Logger.getLogger(RequestServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        Web3j web3 = req.getWeb3();
        System.out.println("Connected");
        
        boolean koinSupplySufficient = req.koinSupplySufficient(koinBigInteger, credentials);
        boolean etherSupplySufficient = transaction.etherSupplySufficient(web3, credentials, etherToExchange);
        
        if(koinSupplySufficient == true && etherSupplySufficient == true) {
            transaction.sendEther(web3, credentials, etherToExchange);
            req.transferKoin(koinBigInteger, credentials);
            System.out.println("KOIN transfer completed");
        }
        else if(koinSupplySufficient == false) {
            System.out.println("Transfer failed...insufficient KOIN supply");
        }
        
        else if(etherSupplySufficient == false) {
            System.out.println("Transfer failed...insufficient Ether supply");
        }
        
        RegisterService.closeConnection(con);
        
    }
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
    
}