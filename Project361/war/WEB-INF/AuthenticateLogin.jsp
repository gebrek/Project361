<%@ page import="java.util.List" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Authenticating...</title>
    </head>
    <body>
        <%
    	System.out.println("Made it to AuthenticateLogin");//REMOVE/////
        
        
        String username = request.getParameter("login");
        String password = request.getParameter("password");
        
        System.out.println("Username: " + username + "\nPassword: " + password); //REMOVE///
        
        //get all users
        //List<Staff> staffList = ds.getAllStaff();
        //any users have given login(email)?
        	
        //passwords match?
       
       	
        if ( username.equals("") && password.equals("") )
        {
            //session.setAttribute("username",username);
            response.sendRedirect("/project");
        }
        else
            //response.sendRedirect("loginError.html");
        	response.sendRedirect("/project");
        %>
    </body>
</html>
