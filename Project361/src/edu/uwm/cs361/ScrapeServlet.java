
package edu.uwm.cs361;

//cargo cult imports from CreateStaffServlet. clean up later
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
public class ScrapeServlet extends HttpServlet{
	ProjectServlet page = new ProjectServlet();
	 	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/html");
		Scrape.getCourseListandStore();
		resp.sendRedirect("/courseList");
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	}
	
		
}
