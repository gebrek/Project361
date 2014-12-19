package edu.uwm.cs361;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.*;

import edu.uwm.cs361.ProjectServlet;

@SuppressWarnings("serial")
public class StaffScheduleServlet extends HttpServlet{
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
		
		
		http += "<form id=\"ccf\" method=\"POST\" action=\"/staffSchedule\">"
		+			"<div id=\"title-create-staff\">"
		+				"Staff Schedule"
		+			"</div>";
		

		http += 	"<div id=\"sub\">"
		+				"<table>"
		+					"<tr>"
		+						"<td class='form'>"
		+							"Staff:"
		+							"<select id='staffname' name='staffname' class='staff-select staff-view-margin' required>"
		+									"<option value = '' selected> Select a Person </option>";
										if(page.username.equals("admin@uwm.edu")){
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
		+						"<input class='view-submit-staff' type='submit' value='View' />";
		http +=				"</td>" 
		+				"</tr><br>";
		
		Staff user = null;
		if (!staff.isEmpty() && staff != null)
			user = data.getStaff(staff);
		
		if(user != null){	
			List<Section> sections = user.getSectionsTaught();
			http+= "<tr class='border_bottom'><td>Course</td><td>Section</td><td>Units</td><td>Hours</td><td>Days</td><td>Instructor</td><td>Room</td></tr>\n";

			for (Section s : sections) {
				http += s.toHtmlTRwithCourse();
			}

			http+=	"<tr>"
			+			"<td class='view-staff-hours'>"
			+				"Office Hours:";
							List<String> listhours = user.getOfficeHours();
							if(listhours != null && !listhours.isEmpty()){
								for(String i: listhours){
									http += "<div class='view-mycontact-hours'>" + i + "</div>";
								}
							}
			http+=		"</td>"
			+		"</tr>";
		}
		http+= 		"</table>"
		+		"</div>"
		+	"</form>";
		
		return http;
	}

}
