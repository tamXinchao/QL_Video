package com.poly.controller.admin;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poly.service.StatsService;
import com.poly.service.StatsServiceImpl;

@WebServlet(urlPatterns = "/admin", name="HomeControllerOfAdmin")
public class HomeController extends HttpServlet {

	private static final long serialVersionUID = -2192301184202943572L;
	
	private StatsService statsService = new StatsServiceImpl();
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher requestDispatcher =req.getRequestDispatcher("/views/admin/home.jsp");
		requestDispatcher.forward(req, resp);
	}
		
}
