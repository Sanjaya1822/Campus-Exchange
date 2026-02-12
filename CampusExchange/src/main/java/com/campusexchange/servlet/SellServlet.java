package com.campusexchange.servlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.campusexchange.db.DBConnection;

@WebServlet("/sellItem")
@MultipartConfig
public class SellServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String sellerEmail = (String) session.getAttribute("userEmail");

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String price = request.getParameter("price");
        String category = request.getParameter("category");

        Part imagePart = request.getPart("image");
        String fileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();

        String uploadPath = getServletContext().getRealPath("") + "uploads";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdir();

        imagePart.write(uploadPath + File.separator + fileName);

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO items(title, description, price, category, seller_email, image) VALUES (?, ?, ?, ?, ?, ?)"
            );

            ps.setString(1, title);
            ps.setString(2, description);
            ps.setDouble(3, Double.parseDouble(price));
            ps.setString(4, category);
            ps.setString(5, sellerEmail);
            ps.setString(6, fileName);

            ps.executeUpdate();
            con.close();

            response.sendRedirect("buy.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Item upload failed");
        }
    }
}
