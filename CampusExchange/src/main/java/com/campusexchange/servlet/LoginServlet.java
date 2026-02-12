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

import org.mindrot.jbcrypt.BCrypt;

import com.campusexchange.db.DBConnection;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try (Connection con = DBConnection.getConnection()) {

            // 1️⃣ Fetch user by email ONLY
            String sql = "SELECT name, email, password FROM users WHERE email = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String dbHashedPassword = rs.getString("password");

                // 2️⃣ BCrypt password verification
                if (org.mindrot.jbcrypt.BCrypt.checkpw(password, dbHashedPassword)) {

                    // ✅ LOGIN SUCCESS
                    HttpSession session = request.getSession();
                    session.setAttribute("userEmail", rs.getString("email"));
                    session.setAttribute("userName", rs.getString("name"));

                    response.sendRedirect("dashboard.jsp");
                    return;
                }
            }

            // ❌ LOGIN FAILED
            response.sendRedirect("login.jsp?error=invalid");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Login Error");
        }
    }
}
