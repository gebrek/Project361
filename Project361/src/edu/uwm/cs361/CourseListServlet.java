package edu.uwm.cs361;

// cargo cult imports from CreateStaffServlet. clean up later
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

import edu.uwm.cs361.ProjectServlet;

@SuppressWarnings("serial")
public class CourseListServlet extends HttpServlet{
	ProjectServlet page = new ProjectServlet();
	DatastoreServ ds = new DatastoreServ();
	 	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/html");
		page.banner(req,resp);
		page.courseListLayout(buildPage(),req,resp);
		page.menu(req,resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	}
	
	/**
	 * Build the main page content 
	 * @return String containing built html code
	 */
	private String buildPage(){
		String http = "";
		http += "<form id=\"ccflist\">"
		+			"<div id=\"title-create-staff\">";
		if (page.getCurrentUser().equals("admin@uwm.edu"))
			http +=				"Course List";
		else
			http +=				"My Sections";
		http +=			"</div>";
		http += 	"<div id=\"sub1\">"
		+				"<table class='courselist'>";
		List<Course> courses = ds.getAllCourses();
		for (Course c : courses) {
			http += c.toHtmlTable();
		}
		http +=				"</table>"
		+			"</div>"
		+		"</form>";
		return http;
	}

}
