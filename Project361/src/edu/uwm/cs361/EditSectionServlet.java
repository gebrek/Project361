package edu.uwm.cs361;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.BaseDatastoreService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;

@SuppressWarnings("serial")
public class EditSectionServlet extends HttpServlet {

	private String courseKey = null;

	ProjectServlet page = new ProjectServlet();
	DemeritDatastoreService data = new DemeritDatastoreService();
	DatastoreService ds = data.getDatastore();

	DatastoreServ myData = new DatastoreServ();
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		ArrayList<Course> myCourses = myData.getAllCourses();
		
		String http = "";

		if (courseKey == null)
			courseKey = "";

		String sectionKey;
		sectionKey = req.getParameter("sectionKey");
		if (sectionKey == null) {
			page.banner(req, resp);
			http += "<form id=\"ccf\" method=\"GET\" action=\"/editSection\">"
					+ "<div id=\"title-create-staff\">"
					+ "Edit Section"
					+ "</div>"
					+ "<div id=\"sub\">"
					+ "<table>"
					+ "<tr>"
					+ "<td class='form'>"
					+ "Courses:"
					+ "<select id='staff' name='sectionKey' class='staff-select'>";
			for (Course course : myCourses) {
				
				courseKey = course.key().toString();
				http += "<option disabled>" + courseKey + "</option>";
				ArrayList<Section> list = course.getSections();
				//String[] listArray = data.makeDelStringToArray(list);
				if(list != null){
					System.out.println("Size of my list is: HQ "+list.size());
					for (Section i : list) {
						System.out.println("My section keys happen to be HQ "+i.key());
						http += "<option>  "
								+ i.key()
								+ "</option>";
					}
				}else {
					System.out.println("My list must have been null HQ");
				}
			}

			http += "</select><br><br>" + "</td>" + "</tr>";
			http += "</table>"
					+ "<input class=\"submit\" type=\"submit\" value=\"Submit\" />"
					+ "</div>" + "</form>";
			page.layout(http, req, resp);
			page.menu(req, resp);
		} else {
			page.banner(req, resp);
			page.layout(
					displayForm(req, resp, new ArrayList<String>(),sectionKey), req, resp);
			page.menu(req, resp);
		}
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		String staff = req.getParameter("staff");
		//String mainKey = req.getParameter("mainKey");
		//System.out.println("staff: " + staff + "KEy: " + mainKey);
		
		/*Entity sectionEn = null;
		try {
			sectionEn = data.getSection(mainKey);
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		Section mySection = myData.getSection(req.getParameter("sectionKey"));
		
		List<String> errors = new ArrayList<String>();

		if (staff == null)
			staff = "";

		if (errors.size() > 0) {
			page.banner(req, resp);
			page.layout(displayForm(req, resp, errors, staff), req, resp);
			page.menu(req, resp);
		} else {

			Entity e = null;
			mySection.setInstructor(staff);
			
			myData.setSection(mySection);

			String http = "";

			http += "<form id=\"ccf\" method=\"GET\" action=\"/editSection\">"
					+			"<div id=\"title-create-staff\">"
					+				"Edit Course Section Conformation"
					+			"</div>"
					+ 			"<div id=\"sub\">"
					+ 				"New section instructor: "
					+ 					e.getProperty(data.STAFF).toString()
					+ 					"<br>"
					+ 					"This section has been updated.<br><br><br><br><br><br>"
					+				"<input class=\"submit\" type=\"submit\" value=\"Back\" />"
					+			"</div>"
					+		"</form>";
			page.banner(req, resp);
			page.layout(http, req, resp);
			page.menu(req, resp);
		}
	}

	private String displayForm(HttpServletRequest req,
			HttpServletResponse resp, List<String> errors, String mainKey)
			throws IOException {

		//System.out.println(mainKey);

		//System.out.println(mainKey);


		resp.setContentType("text/html");
		String http = "";

		Section mySection = myData.getSection(req.getParameter("sectionKey"));
		
		String type = mySection.getType();

		http += "<form id=\"ccf\" method=\"POST\" action=\"/editSection\">"
				+ "<div id=\"title-create-staff\">" + "CompSci " + mainKey
				+ " - " + type + " " + "<br>" + "</div>";

		if (errors.size() > 0) {
			http += "<ul class='errors'>";

			for (String error : errors) {
				http += "  <li>" + error + "</li>";
			}

			http += "</ul>";
		}

		ArrayList<Staff> users = myData.getAllStaff();
		
		http += "<div id=\"sub\">" + "<table>" + "<tr>"
				+ "<td class=\"form\" >" + "Staff:"
				+ "<select id='staff' name='staff' class='staff-select'>";
		http += "<option disabled>Instructor's</option>";
		for (Staff user : users) {
			if (!user.getPermissions().equals("TA"))
				http += "<option>" + data.getOurKey(user.key())
						+ "</option>";

		}
		http += "<option disabled>TA's</option>";
		for (Staff user : users) {
			if (user.getPermissions().equals("TA"))
				http += "<option>" + data.getOurKey(user.key())
						+ "</option>";
		}
		http += "<input class='createStaffInput' type=\"hidden\" id='staff' name='mainKey' value='" + mainKey + "'/><br>";
		http += "</select><br><br>" + "</td>" + "</tr>" + "</table>"
				+ "<input class=\"submit\" type=\"submit\" value=\"Submit\" />"
				+ "</div>" + "</form>";
		
		
		return http;

	}

}