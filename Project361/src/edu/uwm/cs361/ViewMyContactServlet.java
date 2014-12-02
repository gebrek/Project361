package edu.uwm.cs361;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.*;

import edu.uwm.cs361.ProjectServlet;

@SuppressWarnings("serial")
public class ViewMyContactServlet extends HttpServlet{
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
		
		//get the value from the dropdown
		
		page.banner(req,resp);
		page.layout(displayForm(req,resp,page.getCurrentUser().getName()),req,resp);
		page.menu(req,resp);
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		
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

		http += "<form id=\"ccf\" method=\"POST\" action=\"/viewMyContact\">"
		+			"<div id=\"title-create-staff\">"
		+				"My contact info"
		+			"</div>";

		http += 	"<div id=\"sub\">"
		+				"<table>";
		
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
				+			"<td class='view-mycontact-result'>"
				+				user.getName() + "<br>"
				+				user.getEmail() + "<br>"
				+				user.getPassword() + "<br>"
				+				user.getPermissions() + "<br>"
				+			"</td>"
				+			"<td class='view-mycontact'>"
				+				"Office:<br>"
				+				"Office Phone:<br>"
				+				"Address:<br>"
				+				"Home Phone:<br>"
				+			"</td>"
				+			"<td class='view-mycontact-result1'>"
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
		
//		http += "<form method='Get' action='/editStaff'>"
//		+			"<table>"
//		+				"<tr>"
//		+					"<td>"
//		+						"<input class='view-edit-staff' type='submit' value='Edit' />"
//		+					"</td>"
//		+				"</tr>"
//		+			"</table>"
//		+		"</form>";
		return http;
	}

}
