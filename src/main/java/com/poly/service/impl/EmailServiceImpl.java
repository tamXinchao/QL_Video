package com.poly.service.impl;

import javax.servlet.ServletContext;

import com.poly.entity.User;
import com.poly.service.EmailService;
import com.poly.util.SendEmailUtil;

public class EmailServiceImpl implements EmailService {

	private static final String EMAIL_WELCOME_SUBJECT = "Welcome to my channel";
	private static final String EMAIL_FORGOT_PASSWORD = "Twich - New Password";
	
	@Override
	public void SendMail(ServletContext context, User recipient, String type) {
		
        String host = context.getInitParameter("host");
        String port = context.getInitParameter("port");
        String user = context.getInitParameter("user");
        String pass = context.getInitParameter("pass");
        
        try {
        	String content = null;
        	String subject = null;
        	switch (type) {
			case "welcome":
				subject = EMAIL_WELCOME_SUBJECT;
				content = "Dear " + recipient.getUsername() + ", hope you have a good time!";
				break;
			case "forgot":
				subject = EMAIL_FORGOT_PASSWORD;
				content = "This is your new password: " + recipient.getPassword();
				break;

			default:
				subject = "TWICH";
				content = "Maybe this email is wrong, don't care about it";
				break;
			}
        	SendEmailUtil.sendEmail(host, port, user, pass, recipient.getEmail(), subject, content);
        }catch(Exception ex){
        	ex.printStackTrace();
        }
		
	}
	
}
