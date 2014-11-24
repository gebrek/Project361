package edu.uwm.cs361;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.*;

@SuppressWarnings("serial")
public class EditSectionServlet extends HttpServlet {

	ProjectServlet page = new ProjectServlet();
	
	DatastoreServ ds = new DatastoreServ();
	
	HttpServletRequest _req;
	
	HttpServletResponse _resp;
	
	List<String> _errors;
	
	String sectionid;
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		_req = req;
		
		_resp = resp;
		
		_errors = new ArrayList<String>();
		
		page.checkLogin(req, resp);
		
		displayForm();
		
		handleSubmit();
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		doGet(req, resp);
	}

	private void displayForm() throws IOException {

		page.banner(_req, _resp);
		
		startForm();
		
		diplayCourses();
		
		displayStaff();
		
		endForm();
		
		page.menu(_req, _resp);
	}

	private void startForm() throws IOException {

		_resp.getWriter().println("<div class=\"layout background-style\">"
				+ "<div class=\"page-after-banner\">"
				+ (_req.getParameter("submit") != null ? "<div>Successfully Saved<div>" : "")
				+ "<form id='ccf' method='POST' action='editSection'>"
				+ "<div id='title-create-staff'>");
	}
	
	private void endForm() throws IOException {
		
		_resp.getWriter().println("<input class='submit' name='submit' type='submit' value='Submit' />"
										+ "</div></form></div></div>");
		
	}

	private void displayStaff() throws IOException {
		
		String html = "<select id='staff' name='staff' class='staff-select'>";
		
		for(int i = 1; i < 10; i++) {
			
			html += "<option value='"+i+"'>"+i+"</option>";
		}
		
		html += "</select>";
		
		_resp.getWriter().println(html);
	}

	private void diplayCourses() throws IOException {
		
		List<Course> myCourses = ds.getAllCourses();
		
		String html = "<select id='section' name='section' class='staff-select'>";
		
		for (Course course : myCourses) {
			
			List<Section> sections = ds.getSection("courseid=='"+course.getID()+"'");
			
			html += "<option disabled> CS " + course.getNumber() + "</option>";
			
			for (Section section : sections) {
				
				html += "<option value='"+section.getID()+"'>"
						+ section.getType() + " " + section.getSection()
						+ "</option>";
			}
		}
		
		html += "</select>";
		
		_resp.getWriter().println(html);
	}
	
	private void handleSubmit() {
		
		if(_req.getParameter("submit") != null) {
			
			ds.editSection(_req.getParameter("section"), _req.getParameter("staff"));
		}
	}
}