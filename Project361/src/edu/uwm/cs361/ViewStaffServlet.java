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
		page.layout(displayForm(req,resp,"",""),req,resp);
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
		String delete = req.getParameter("delete");
		String delConf = "";
		
		if(delete != null && !delete.isEmpty()){
			if(delete.equals("Delete")){
				//data.deleteStaff(staffname);
				delConf = staffName + " has been Deleted successfully.";
				staffName = "";
			}
		}
		
		page.banner(req,resp);
		page.layout(displayForm(req,resp,staffName, delConf),req,resp);
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
	private String displayForm(HttpServletRequest req, HttpServletResponse resp, String staff, String delconf) throws IOException
	{
		resp.setContentType("text/html");
		String http = "";
		
		
		List<Staff> users = data.getAllStaff(); 
		
		
		http += "<form id=\"ccf\" method=\"POST\" action=\"/viewStaff\">"
		+			"<div id=\"title-create-staff\">"
		+				"View Staff"
		+			"</div>";
		
		if (!delconf.isEmpty()) {
			http += "<ul class='errors'>"
					+	"  <li>" + delconf + "</li>"
					+ "</ul>";
		}

		http += 	"<div id=\"sub\">"
		+				"<table>"
		+					"<tr>"
		+						"<td class='form'>"
		+							"Staff:"
		+							"<select id='staffname' name='staffname' class='staff-select staff-view-margin'>"
		+									"<option value = '' selected> Select a Person </option>";
										if(page.username.equals("admin@uwm.edu")){
											http += "<option disabled>Instructor's</option>";		
											for(Staff user:users){
												if(!user.getPermissions().equals("TA"))
													http += "<option>" + user.getName() + "</option>";
											}
										}
										else if(!page.getCurrentUser().getPermissions().equals("Instructor")){
											http += "<option disabled>Instructor's</option>";		
											for(Staff user:users){
												if(!user.getPermissions().equals("TA"))
													http += "<option>" + user.getName() + "</option>";
											}
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
		+						"<input class='view-delete-staff' name='delete' type='submit' value='Delete' />"
		+					"</td>"
		+				"</tr>";
		
		Staff user = null;
		if (!staff.isEmpty() && staff != null)
			user = data.getStaff(staff);
		
		if(user != null){					
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
				+				"Address:<br><br>"
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
		
		http+= 		"</table>"
		+		"</div>"
		+	"</form>";
		
		return http;
	}

}
