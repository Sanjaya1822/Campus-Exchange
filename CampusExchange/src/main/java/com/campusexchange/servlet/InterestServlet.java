package com.campusexchange.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.campusexchange.db.DBConnection;

@WebServlet("/interest")
public class InterestServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userEmail") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String buyerEmail = (String) session.getAttribute("userEmail");
        int itemId = Integer.parseInt(request.getParameter("itemId"));

        try {
            Connection con = DBConnection.getConnection();

            // 🔍 CHECK DUPLICATE INTEREST
            String checkSql =
                "SELECT 1 FROM item_requests WHERE item_id=? AND buyer_email=?";
            PreparedStatement checkPs = con.prepareStatement(checkSql);
            checkPs.setInt(1, itemId);
            checkPs.setString(2, buyerEmail);

            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                // Already interested
                response.sendRedirect("itemDetails.jsp?id=" + itemId + "&success=already");
                con.close();
                return;
            }

            // ✅ INSERT INTEREST
            String insertSql =
                "INSERT INTO item_requests (item_id, buyer_email) VALUES (?, ?)";
            PreparedStatement ps = con.prepareStatement(insertSql);
            ps.setInt(1, itemId);
            ps.setString(2, buyerEmail);
            ps.executeUpdate();

            con.close();

            response.sendRedirect("itemDetails.jsp?id=" + itemId + "&success=1");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Interest Error");
        }
    }
}
