package edu.uwm.cs361;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.*;


@SuppressWarnings("serial")
public class EditMyContactServlet extends HttpServlet{
	ProjectServlet page = new ProjectServlet();
	DatastoreServ data = new DatastoreServ();
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		page.banner(req,resp);
		page.layout(displayForm(req,resp, new ArrayList<String>(), page.getCurrentUser().getName()), req, resp);
		page.menu(req,resp);
		
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
		

		//get all the inputs
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String password2 = req.getParameter("passwordConfirm");
		String firstname = req.getParameter("firstname");
		String officePhone = req.getParameter("officePhone");
		String office = req.getParameter("office");
		String homeAddress = req.getParameter("homeAddress");
		String homePhone = req.getParameter("homePhone");
		
		List<String> errors = new ArrayList<String>();
		
		officePhone = formatPhone(officePhone);
		homePhone = formatPhone(homePhone);
		
		//check for the empty inputs
		if(username != null ){
			username = username.toLowerCase();
			if (password.isEmpty()) {
				errors.add("Password is required.");
			} 
			if (firstname.isEmpty()) {
				errors.add("First is required.");
			} 
		}
		if (officePhone.length() != 10)
			errors.add("Enter 10 digit phone number for office phone");
		if (office == null || office.length() < 4)
			errors.add("Office location or \"none\" is required.");
		if (homeAddress == null || homeAddress.isEmpty())
			errors.add("Home address is required.");
		if (homePhone.length() != 10)
			errors.add("Enter 10 digit phone number for home phone");
		
		//check passwords
		if (!password.equals(password2))
			errors.add("Passwords do not match.");
		
		if (errors.size() > 0) {
			page.banner(req,resp);
			page.layout(displayForm (req,resp,errors, page.getCurrentUser().getName()),req,resp);
			page.menu(req,resp);
		} else {
			data.updateStaff(username, firstname, password, null);
			data.updateStaffContact(page.getCurrentUser().getName(), office, officePhone, homeAddress, homePhone);
			
			String http = "";
			
			http += "<form id=\"ccf\" method=\"GET\" action=\"/viewMyContact\">"
			+			"<div id=\"title-create-staff\">"
			+				"Edit Contact info: " + username
			+			"</div>"
			+ 			"<div id=\"sub\">"
			+				"UserName: " + username + "<br>" 
			+				"Name: " + firstname + "<br><br>"   
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
		
		http += "<form id=\"ccf\" method=\"POST\" action=\"/editMyContact\">"
		+			"<div id=\"title-create-staff\">"
		+				"Edit Contact info: " + staff
		+			"</div>";
		

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
		+							"Username *: <input readonly class='createStaffInput' type=\"text\" id='username' name='username' value='" + staffToUpdate.getEmail() + "'/><br>"
		+							"Password *: <input class='createStaffInput' type=\"password\" id='password' name='password' value='" + staffToUpdate.getPassword() + "'required/><br>"
		+							"Confirm pass *: <input class='createStaffInput' type=\"password\" id='passwordConfirm' name='passwordConfirm' required/><br>"
		+							"Name *: <input class='createStaffInput' type=\"text\" id='firstname' name='firstname' value='" + staffToUpdate.getName() + "'required/><br>"
		+							"Office: <input class='createStaffInput' type=\"text\" id='officeLoc' name='office' value='" + staffToUpdate.getOfficeLoc() + "'required/><br>"
		+							"Office Phone: <input class='createStaffInput' type=\"text\" id='officePhone' name='officePhone' value='" + staffToUpdate.getOfficePhone() + "'required/><br>"
		+							"Home Address: <input class='createStaffInput' type=\"text\" id='homeAddress' name='homeAddress' value='" + staffToUpdate.getHomeAddress() + "'required/><br>"
		+							"Home Phone: <input class='createStaffInput' type=\"text\" id='homePhone' name='homePhone' value='" + staffToUpdate.getHomePhone() + "'required/><br>"
		+						"</td>"
		+					"</tr>"
		+				"</table>"
		+				"<input class=\"submit\" type=\"submit\" value=\"Submit\" />"
		+			"</div>"
		+		"</form>";
		
		
		
		return http;
	}

}
