package com.campusexchange.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.campusexchange.db.DBConnection;

@WebServlet("/markSold")
public class MarkSoldServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userEmail") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int itemId = Integer.parseInt(request.getParameter("itemId"));

        try (Connection con = DBConnection.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                "UPDATE items SET status='SOLD' WHERE id=?"
            );
            ps.setInt(1, itemId);
            ps.executeUpdate();

            response.sendRedirect("dashboard.jsp?msg=sold");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error marking item as sold");
        }
    }
}
