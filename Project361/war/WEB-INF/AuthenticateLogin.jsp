<%@ page import="java.util.List" %>
<%@ page import="edu.uwm.cs361.DatastoreServ" %>
<%@ page import="edu.uwm.cs361.Staff" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Authenticating...</title>
    </head>
    <body>
        <%
        
        String username = request.getParameter("login");
        String password = request.getParameter("password");
        
        DatastoreServ ds = new DatastoreServ();
        
        //admin?
        if (username.equals("admin@uwm.edu")) {
        	
        	if ( password.equals(ds.getAdminPassword()) ) {
        		
        		Cookie c = new Cookie("username", username);
    			response.addCookie(c);
    			
        		response.sendRedirect("/project");
        	}
        	else {
        		response.sendRedirect("loginError.html");
        	}
        }
        
        else {
        	
        	//get all users
        	List<Staff> staffList = ds.getAllStaff();
        
        	boolean validUser = false;
        	
        	//any users have given login(email)?
        	System.out.println("Checking: " + username + " / " + password);
	        for (Staff staff:staffList) {
	        	
	        	System.out.println("against: " + staff.getEmail() + " / " + staff.getPassword());
	        	
	        	if (staff.getEmail().equalsIgnoreCase(username)) {
	        		
	        		if (staff.getPassword().equals(password)) {
	        			
	            		Cookie c = new Cookie("username", username);
	        			response.addCookie(c);
						response.sendRedirect("/project");
	                    break;
	        		}
	        		else {
	        			response.sendRedirect("loginError.html");
	        			break;
	        		}
        		}
        	}
	        response.sendRedirect("loginError.html");
        }
        
        
        %>
    </body>
</html>
