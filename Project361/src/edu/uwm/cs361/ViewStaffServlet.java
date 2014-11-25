package edu.uwm.cs361;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.*;

import edu.uwm.cs361.ProjectServlet;

@SuppressWarnings("serial")
public class ViewStaffServlet extends HttpServlet{
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
		page.layout(displayForm(req,resp,""),req,resp);
		page.menu(req,resp);
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		//get the value from the dropdown
		String staffName = req.getParameter("staffname");
		
		page.banner(req,resp);
		page.layout(displayForm(req,resp,staffName),req,resp);
		page.menu(req,resp);
		
	}
	
	/**
	 * display form will list all the information for the staff
	 * 
	 * @param req
	 * @param resp
	 * @param staff
	 * @return
	 * @throws IOException
	 */
	private String displayForm(HttpServletRequest req, HttpServletResponse resp, String staff) throws IOException
	{
		resp.setContentType("text/html");
		String http = "";
		
		
		List<Staff> users = data.getAllStaff(); 
		
		
		http += "<form id=\"ccf\" method=\"POST\" action=\"/viewStaff\">"
		+			"<div id=\"title-create-staff\">"
		+				"View Staff"
		+			"</div>";

		http += 	"<div id=\"sub\">"
		+				"<table>"
		+					"<tr>"
		+						"<td class='form'>"
		+							"Staff:"
		+							"<select id='staffname' name='staffname' class='staff-select staff-view-margin'>"
		+									"<option value = '' selected> Select a Person </option>";
										http += "<option disabled>Instructor's</option>";		
										for(Staff user:users){
											if(!user.getPermissions().equals("TA"))
												http += "<option>" + user.getName() + "</option>";
										}
										http += "<option disabled>TA's</option>";
										for(Staff user:users){
											if(user.getPermissions().equals("TA"))
												http += "<option>" + user.getName() + "</option>";
										}
		http +=						"</select>"
		+						"</td>"
		+					"</tr>"
		+					"<tr>"
		+					"<td></td>"
		+					"<td>"
		+						"<input class='view-submit-staff' type='submit' value='Submit' />"
		+					"</td>"
		+				"</tr>";
		
		for(Staff user:users){
			if(user.getName().equals(staff)){					
				http+=	"<tr>"
				+			"<td class='view-staff'>"
				+				"Name:<br>"
				+				"Username:<br>"
				+				"Password:<br>"
				+				"Staff Type:<br>"
				+			"</td>"
				+			"<td class='view-staff-result'>"
				+				user.getName() + "<br>"
				+				user.getEmail() + "<br>"
				+				user.getPassword() + "<br>"
				+				user.getPermissions() + "<br>"
				+			"</td>"
				+			"<td class='view-staff1'>"
				+				"Office:<br>"
				+				"Office Phone:<br>"
				+				"Address:<br>"
				+				"Home Phone:<br>"
				+			"</td>"
				+			"<td class='view-staff-result1'>"
				+				user.getOfficeLoc() + "<br>"
				+				user.getOfficePhone() + "<br>"
				+				user.getHomeAddress() + "<br>"
				+				user.getHomePhone() + "<br>"
				+			"</td>"
				+		"</tr>";
			}
		}
		
		http+= 		"</table>"
		+		"</div>"
		+	"</form>";
		return http;
	}

}
