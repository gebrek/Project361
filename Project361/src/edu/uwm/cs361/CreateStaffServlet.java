package edu.uwm.cs361;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.*;

import edu.uwm.cs361.ProjectServlet;

@SuppressWarnings("serial")
public class CreateStaffServlet extends HttpServlet{
	/*
	 * Create a variable to call project servlet methods for HTTP
	 * create insistence of datastore service 
	 */
	ProjectServlet page = new ProjectServlet();
	DatastoreServ data = new DatastoreServ();
	
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		page.banner(req,resp);
		page.layout(displayForm(req,resp,new ArrayList<String>()),req,resp);
		page.menu(req,resp);
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		// get all the parameter from the form
		String username = req.getParameter("username");
		username = username.toLowerCase();
		String password = req.getParameter("password");
		String password2 = req.getParameter("passwordConfirm");
		String firstname = req.getParameter("firstname");
		String lastname = req.getParameter("lastname");
		String stafftype = req.getParameter("stafftype");

		List<String> errors = new ArrayList<String>();
		
		//check if exists
		List<Staff> staffList = data.getAllStaff();
		boolean exist = false;
		for(Staff staff:staffList){
			System.out.println("Checking: " + staff.getName());
			if (staff.getName().equalsIgnoreCase(firstname + " " + lastname) || staff.getEmail().equalsIgnoreCase(username)) {
				errors.add("User '"+ username +"' Already Exist.");
				exist = true;
				break;
			}
		}
		
		//checking for blanks
		if(!exist){
			if (username.isEmpty()) {
				errors.add("Username is required.");
			}
			if (password.isEmpty()) {
				errors.add("Password is required.");
			} 
			if (firstname.isEmpty()) {
				errors.add("First is required.");
			} 
			if (lastname.isEmpty()) {
				errors.add("Lastname is required.");
			} 
			if (stafftype.isEmpty()) {
				errors.add("Staff Type is required.");
			} 
		}
		
		//check passwords
		if (!password.equals(password2))
			errors.add("Passwords do not match.");
		
		//if there is any error then print the form again
		if (errors.size() > 0) {
			page.banner(req,resp);
			page.layout(displayForm(req,resp,errors),req,resp);
			page.menu(req,resp);
		} else {	

			Staff newStaff = new Staff(username, firstname + " " + lastname, null, stafftype, password);
			data.createStaff(newStaff);
			String http = "";
			//Staff created confirmation page.
			http += "<form id=\"ccf\" method=\"GET\" action=\"/createStaff\">"
			+			"<div id=\"title-create-staff\">"
			+				"Staff Created Conformation"
			+			"</div>"
			+ 			"<div id=\"sub\">"
			+				"UserName: " + username + "<br>" 
			+				"First Name: " + firstname + "<br>" 
			+				"Last Name: " + lastname + "<br><br>" 
			+				"Staff Type: " + stafftype + "<br><br>" 
			+				"The User has been Created.<br><br><br><br><br><br>"
			+				"<input class=\"submit\" type=\"submit\" value=\"Back\" />"
			+			"</div>"
			+		"</form>";
			page.banner(req,resp);
			page.layout(http,req,resp);
			page.menu(req,resp);
		}
	}
	

	/**
	 * display form will get a list for errors 
	 * print the form with errors.
	 * 
	 * @param errors Any errors encountered
	 * @return String of the page html
	 * @throws IOException
	 */
	private String displayForm(HttpServletRequest req, HttpServletResponse resp, List<String> errors) throws IOException
	{
		resp.setContentType("text/html");
		String http = "";
		
		http += "<form id=\"ccf\" method=\"POST\" action=\"/createStaff\">"
		+			"<div id=\"title-create-staff\">"
		+				"Create Staff"
		+			"</div>";
		
		String username = req.getParameter("username") != null ? req.getParameter("username") : "";
		username = username.toLowerCase();
		String password = req.getParameter("password") != null ? req.getParameter("password") : "";
		String firstname = req.getParameter("firstname") != null ? req.getParameter("firstname") : "";
		String lastname = req.getParameter("lastname") != null ? req.getParameter("lastname") : "";
		String stafftype = req.getParameter("stafftype") != null ? req.getParameter("stafftype") : "";

		if (errors.size() > 0) {
			http += "<ul class='errors'>";

			for (String error : errors) {
				http +="  <li>" + error + "</li>";
			}

			http += "</ul>";
		}

		http += 	"<div id=\"sub\">"
		+				"<table>"
		+					"<tr>"
		+						"<td class=\"form\">"
		+							"Username *: <input class='createStaffInput' type=\"text\" id='username' name='username' value='" + username + "' placeholder=\"format: you@uwm.edu\" pattern=\"^[_a-zA-Z0-9-]+@uwm.edu\" required/><br>"
		+							"Password *: <input class='createStaffInput' type=\"password\" id='password' name='password' value='" + password + "'required/><br>"
		+							"Confirm password *: <input class='createStaffInput' type=\"password\" id='passwordConfirm' name='passwordConfirm' required/><br>"
		+							"First Name *: <input class='createStaffInput' type=\"text\" id='firstname' name='firstname' value='" + firstname + "'required/><br>"
		+							"Last Name *: <input class='createStaffInput' type=\"text\" id='lastname' name='lastname' value='" + lastname + "'required/><br>"
		+							"Staff Type: <select class='staff-select createStaffInput' id='stafftype' name='stafftype' value='" + stafftype + "'required>"
				+									"<option value = '' selected> Select a Type </option>"
		+											"<option> Instructor </option>"
		+											"<option> TA </option>"
		+										"</select><br>"
		+						"</td>"
		+					"</tr>"
		+				"</table>"
		+				"<input class=\"submit\" type=\"submit\" value=\"Submit\" />"
		+			"</div>"
		+		"</form>";
		
		return http;
	}

}