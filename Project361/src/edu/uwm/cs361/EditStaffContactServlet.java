package edu.uwm.cs361;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.*;


@SuppressWarnings("serial")
public class EditStaffContactServlet extends HttpServlet{
	ProjectServlet page = new ProjectServlet();
	DatastoreServ data = new DatastoreServ();
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		
		List<Staff> staffList = data.getAllStaff();
		String http = "";

		String staff = "";
		staff = req.getParameter("staff");
		if(staff==null){
			page.banner(req,resp);
			http += "<form id=\"ccf\" method=\"GET\" action=\"/editStaffContact\">"
			+			"<div id=\"title-create-staff\">"
			+				"Edit Staff Contact"
			+			"</div>"
			+			"<div id=\"sub\">"
			+				"<table>"
			+					"<tr>"
			+						"<td class='form'>"
			+							"Staff:"
			+							"<select id='staff' name='staff' class='staff-select'>";
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
			page.layout(displayForm(req,resp, new ArrayList<String>(), staff), req, resp);
			page.menu(req,resp);
		}
	}
	
	
	
	/**
	 * Formats the phone for storage, removing all non-digits
	 * @param number 10 digit phone number
	 * @return Only numbers, from the original phone number given
	 */
	private String formatPhone(String number)
	{
		String toReturn = "";
		if(number != null)
		{
			for(int i = 0; i < number.length(); ++i)
			{
				char current = number.charAt(i);
				if( Character.isDigit(current)  ) 
						toReturn += current;
			}
		}
		return toReturn;
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		String toEdit = req.getParameter("staff");
		String officePhone = req.getParameter("officePhone");
		String office = req.getParameter("office");
		String homeAddress = req.getParameter("homeAddress");
		String homePhone = req.getParameter("homePhone");
		
		List<String> errors = new ArrayList<String>();
		
		officePhone = formatPhone(officePhone);
		homePhone = formatPhone(homePhone);
		
		if (officePhone.length() != 10)
			errors.add("Enter 10 digit phone number for office phone");
		if (office == null || office.length() < 4)
			errors.add("Office location or \"none\" is required.");
		if (homeAddress == null || homeAddress.isEmpty())
			errors.add("Home address is required.");
		if (homePhone.length() != 10)
			errors.add("Enter 10 digit phone number for home phone");
		
		
		if (errors.size() > 0) {
			page.banner(req,resp);
			page.layout(displayForm (req,resp,errors, toEdit),req,resp);
			page.menu(req,resp);
		} else {
	
			data.updateStaffContact(toEdit, office, officePhone, homeAddress, homePhone);
			
			String http = "";
			
			http += "<form id=\"ccf\" method=\"GET\" action=\"/editStaffContact\">"
			+			"<div id=\"title-create-staff\">"
			+				"Edit Contact info: " + req.getParameter("staff")
			+			"</div>"
			+ 			"<div id=\"sub\">"
			+				"Office: " + office + "<br>" 
			+				"Office Phone: " + officePhone + "<br>" 
			+				"Home Address: " + homeAddress + "<br>" 
			+				"Home Phone: " + homePhone + "<br><br>" 
			+				"The User's contact info has been updated.<br><br><br><br><br><br>"
			+				"<input class=\"submit\" type=\"submit\" value=\"Back\" />"
			+			"</div>"
			+		"</form>";
			page.banner(req,resp);
			page.layout(http,req,resp);
			page.menu(req,resp);
		}
	}
	
	/**
	 * Displays the form with fields used to edit staff contact info
	 * @param req
	 * @param resp
	 * @param errors
	 * @param staff Staff full name with space
	 * @return generated html code as string 
	 * @throws IOException
	 */
	private String displayForm(HttpServletRequest req, HttpServletResponse resp, List<String> errors, String staff) throws IOException
	{	
		
		List<Staff> staffList = data.getAllStaff();
		Staff staffToUpdate = null;
		for (Staff i : staffList) {
			if (i.getName().equalsIgnoreCase(staff))
			{
				staffToUpdate = i;
				break;
			}
		}
		
		resp.setContentType("text/html");
		String http = "";
		
		http += "<form id=\"ccf\" method=\"POST\" action=\"/editStaffContact\">"
		+			"<div id=\"title-create-staff\">"
		+				"Edit Staff Contact: " + staff
		+			"</div>";
		
		String office = staffToUpdate.getOfficeLoc();
		String officePhone = staffToUpdate.getOfficePhone();
		String homeAddress = staffToUpdate.getHomeAddress();
		String homePhone = staffToUpdate.getHomePhone();
		

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
		+						"<td class=\"form\" >"
		+							"<input class='createStaffInput' type=\"hidden\" id='staff' name='staff' value='" + staff + "'/><br>"
		+							"Office: <input class='createStaffInput' type=\"text\" id='officeLoc' name='office' value='" + office + "'required/><br>"
		+							"Office Phone: <input class='createStaffInput' type=\"text\" id='officePhone' name='officePhone' value='" + officePhone + "'required/><br>"
		+							"Home Address: <input class='createStaffInput' type=\"text\" id='homeAddress' name='homeAddress' value='" + homeAddress + "'required/><br>"
		+							"Home Phone: <input class='createStaffInput' type=\"text\" id='homePhone' name='homePhone' value='" + homePhone + "'required/><br>"
		+						"</td>"
		+					"</tr>"
		+				"</table>"
		+				"<input class=\"submit\" type=\"submit\" value=\"Submit\" />"
		+			"</div>"
		+		"</form>";
		
		
		
		return http;
	}

}
