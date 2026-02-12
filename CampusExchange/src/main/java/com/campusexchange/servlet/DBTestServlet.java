package com.campusexchange.servlet;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.campusexchange.db.DBConnection;

@WebServlet("/dbtest")
public class DBTestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        Connection con = DBConnection.getConnection();

        if (con != null) {
            response.getWriter().println("<h2 style='color:green;'>Database Connected Successfully ✅</h2>");
        } else {
            response.getWriter().println("<h2 style='color:red;'>Database Connection Failed ❌</h2>");
        }
    }
}
