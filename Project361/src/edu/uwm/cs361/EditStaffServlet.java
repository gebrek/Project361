package edu.uwm.cs361;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;

import edu.uwm.cs361.ProjectServlet;

@SuppressWarnings("serial")
public class EditStaffServlet extends HttpServlet{
	ProjectServlet page = new ProjectServlet();
	DatastoreServ data = new DatastoreServ();
	
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		List<Staff> staffList = data.getAllStaff();
		
		String http = "";

		String staff = " ";
		staff = req.getParameter("staff");
		if(staff==null){
			page.banner(req,resp);
			http += "<form id=\"ccf\" method=\"GET\" action=\"/editStaff\">"
			+			"<div id=\"title-create-staff\">"
			+				"Edit Staff"
			+			"</div>"
			+			"<div id=\"sub\">"
			+				"<table>"
			+					"<tr>"
			+						"<td class='form'>"
			+							"Staff:"
			+							"<select id='staff' name='staff' class='staff-select'>"
			+									"<option value = '' selected> Select a Person </option>";
											http += "<option disabled>Instructor's</option>";		
											for(Staff user:staffList){
												if(!user.getPermissions().equals("TA"))
														http += "<option>" + user.getName() + "</option>";
											}
											http += "<option disabled>TA's</option>";
											for(Staff user:staffList){
												if(user.getPermissions().equals("TA"))
													http += "<option>" + user.getName() + "</option>";
											}
			http +=						"</select><br><br>"
			+						"</td>"
			+					"</tr>";
			http+=				"</table>"
			+				"<input class=\"submit\" type=\"submit\" value=\"Submit\" />"
			+			"</div>"
			+		"</form>";
			page.layout(http,req,resp);
			page.menu(req,resp);
		}
		else{
			page.banner(req,resp);
			page.layout(displayForm(req,resp,new ArrayList<String>(),staff), req, resp);
			page.menu(req, resp);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		//get all the inputs
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String firstname = req.getParameter("firstname");
		String lastname = req.getParameter("lastname");
		String stafftype = req.getParameter("stafftype");

		List<String> errors = new ArrayList<String>();

		//check for the empty inputs
		if(username != null ){
			username = username.toLowerCase();
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
		
		//any error, reprint the form
		if (errors.size() > 0) {
			page.banner(req,resp);
			page.layout(displayForm(req,resp,errors,username),req,resp);
			page.menu(req,resp);
		} else {
			
			PersistenceManager pm = PMF.get().getPersistenceManager();
			List<Staff> staffList = data.getAllStaff();
			for (Staff staff : staffList) {
				if (staff.getEmail().equalsIgnoreCase(username))
				{
					staff.setName(firstname + lastname);
					staff.setPassword(password);
					staff.setPermissions(stafftype);
					pm.makePersistent(staff);
				}
			}
			
			//update conformation form
			String http = "";
			
			http += "<form id=\"ccf\" method=\"get\" action=\"/editStaff\">"
			+			"<div id=\"title-create-staff\">"
			+				"Staff Update Conformation"
			+			"</div>"
			+ 			"<div id=\"sub\">"
			+				"UserName: " + username + "<br>" 
			+				"First Name: " + firstname + "<br>" 
			+				"Last Name: " + lastname + "<br><br>" 
			+				"Staff Type: " + stafftype + "<br>" 
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
	 * @param errors any errors encountered
	 * @param staff staff editing
	 * @return html of page
	 * @throws IOException
	 */
	private String displayForm(HttpServletRequest req, HttpServletResponse resp, List<String> errors,String staff) throws IOException
	{
		resp.setContentType("text/html");
		String http = "";
		
		List<Staff> users = data.getAllStaff();
		
		http += "<form id=\"ccf\" method=\"POST\" action=\"/editStaff\">"
		+			"<div id=\"title-create-staff\">"
		+				"Edit Staff"
		+			"</div>"
		+			"<div id=\"sub\">";
//		+				"<table>"
//		+					"<tr>"
//		+						"<td class='form'>"
//		+							"Staff:"
//		+							"<select id='staff' name='staff' class='staff-select'>"
//		+									"<option value = '' selected> Select a Person </option>";
//										http += "<option disabled>Instructor's</option>";		
//										for(Entity user:users){
//											if(!user.getProperty(data.TYPE).equals("TA"))
//													http += "<option>" + data.getOurKey(user.getKey()) + "</option>";
//										}
//										http += "<option disabled>TA's</option>";
//										for(Entity user:users){
//											if(user.getProperty(data.TYPE).equals("TA"))
//												http += "<option>" + data.getOurKey(user.getKey()) + "</option>";
//										}
//		http +=						"</select><br><br>"
//		+						"</td>"
//		+					"</tr>";
		
		for(Staff user:users){
			if(user.getEmail().equals(staff)){
				if (errors.size() > 0) {
					http += "<tr><td><ul class='errors'>";

					for (String error : errors) {
						http +="  <li>" + error + "</li>";
					}

					http += "</ul></td></tr>";
				}
				String[] na = user.getName().split(" ");

				http+=				"<tr>"
				+						"<td class=\"form\">"
				+							"Username *: <input readonly class='createStaffInput' type=\"text\" id='username' name='username' value='" + user.getEmail() + "'/><br>"
				+							"Password *: <input class='createStaffInput' type=\"password\" id='password' name='password' value='" + user.getPassword() + "'required/><br>"
				+							"First Name *: <input class='createStaffInput' type=\"text\" id='firstname' name='firstname' value='" + na[0].toString() + "'required/><br>"
				+							"Last Name *: <input class='createStaffInput' type=\"text\" id='lastname' name='lastname' value='" + na[1].toString() + "'required/><br>"
				+							"Staff Type: <select class='staff-select createStaffInput' id='stafftype' name='stafftype' value='" + user.getPermissions() + "'>"
				+											"<option value = '' selected> Select a Type </option>"
				+											"<option> Instructor </option>"
				+											"<option> TA </option>"
				+										"</select><br>"
				+						"</td>"
				+					"</tr>";

			}
		}
		http+=				"</table>"
		+				"<input class=\"submit\" type=\"submit\" value=\"Submit\" />"
		+			"</div>"
		+		"</form>";
		return http;
	}

}