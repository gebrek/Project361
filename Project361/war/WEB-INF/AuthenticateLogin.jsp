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
        if (username.equals("admin@uwm.edu") && password.equals(ds.getAdminPassword())) {
        	
        	response.sendRedirect("/project");

        }
        
        else {
        	//get all users
        	List<Staff> staffList = ds.getAllStaff();
        
        	boolean validUser = false;
        	System.out.println("Checking " + username + " / " + password);
        	//any users have given login(email)?
	        for (Staff staff:staffList) {
	        	
        		System.out.println("vs - " + staff.getEmail() + " / " + staff.getPassword());
	        	
	        	if (staff.getEmail().equalsIgnoreCase(username)) {
	        		
	        		if (staff.getPassword().equals(password)) {
	        			
						response.sendRedirect("/project");
	                    break;
	        		}
        		}
	        	else {
	        		response.sendRedirect("loginError.html");
	        	}
        	}
        }
        
        
        %>
    </body>
</html>
