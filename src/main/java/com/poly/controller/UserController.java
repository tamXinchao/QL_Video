package com.poly.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.poly.constant.SessionAttr;
import com.poly.entity.User;
import com.poly.service.EmailService;
import com.poly.service.UserService;
import com.poly.service.impl.EmailServiceImpl;
import com.poly.service.impl.UserServiceImpl;
import com.poly.util.SendEmailUtil;


@WebServlet(urlPatterns = {"/login", "/logout","/register", "/forgotPass","/changePass"})
public class UserController extends HttpServlet {

	private static final long serialVersionUID = -5860351843059541642L;
	private EmailService emailService = new EmailServiceImpl();
	private UserService userService = new UserServiceImpl();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession() ;
		String path = req.getServletPath(); // localhost:8080/asm-java4/register?id = a... >>> path = "/register"
		
		switch (path) {
			case "/login":
				doGetLogin(req, resp);
			break;
			case "/register":
				doGetRegister(req, resp);
			break;
			case "/logout":
				doGetLogout(session,req, resp);
			break;
			case "/forgotPass":
				doGetForgotPass(req, resp);
			break;
		}
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession() ;
		String path = req.getServletPath(); // localhost:8080/asm-java4/register?id = a... >>> path = "/register"
		switch (path) {
			case "/login":
				doPostLogin(session,req, resp);
			break;
			case "/register":
				doPostRegister(session,req, resp);
			break;
			case "/forgotPass":
				doPostForgotPass(req, resp);
			break;
			case "/changePass":
				doPostChangePass(session, req, resp);
			break;
		}
	}
	
	private void doGetLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher resDispatcher = req.getRequestDispatcher("/views/user/login.jsp");
		resDispatcher.forward(req, resp);
	}
	private void doGetRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher resDispatcher = req.getRequestDispatcher("/views/user/register.jsp");
		resDispatcher.forward(req, resp);
	}
	private void doGetLogout(HttpSession session,  HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		session.removeAttribute(SessionAttr.CURRENT_USER);
		resp.sendRedirect("index");
	}
	private void doPostLogin(HttpSession session, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		User user = userService.login(username, password);
		if(user != null) {
			session.setAttribute(SessionAttr.CURRENT_USER, user);
			resp.sendRedirect("index");
		}else {
			resp.sendRedirect("login");
		}
	}
	private void doGetForgotPass(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher resDispatcher = req.getRequestDispatcher("/views/user/forgot-password.jsp");
		resDispatcher.forward(req, resp);
	}
	private void doPostRegister(HttpSession session, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String email  = req.getParameter("email");
		User user = userService.register(username, password, email);
		if(user != null) {
			
			emailService.SendMail(getServletContext(), user, "Welcome"); //ENUM
			session.setAttribute(SessionAttr.CURRENT_USER, user);
			resp.sendRedirect("index");
		}else {
			resp.sendRedirect("register");
		}
	}
	
	private void doPostForgotPass( HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		String email = req.getParameter("email");
		
		User userWithNewPass = userService.resetPassword(email);
		
		if(userWithNewPass != null) {
			emailService.SendMail(getServletContext(), userWithNewPass, "Forgot");
			resp.setStatus(204);
		}
		else {
			resp.setStatus(400);
		}
	}
	
	private void doPostChangePass(HttpSession session, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		String currentPass = req.getParameter("currentPass");
		String newPass = req.getParameter("newPass");
		
		User currentUser  = (User) session.getAttribute(SessionAttr.CURRENT_USER);
		
		if(currentUser.getPassword().equals(currentPass)) {
			currentUser.setPassword(newPass);
			User updateUser = userService.update(currentUser);
			if(updateUser != null) {
				session.setAttribute(SessionAttr.CURRENT_USER, updateUser);
				resp.setStatus(204);
			}else {
				resp.setStatus(400);
			}
		}else {
			resp.setStatus(400);
		}
	}

}
