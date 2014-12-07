package edu.uwm.cs361;

import java.awt.Window;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.*;

import edu.uwm.cs361.*;

@SuppressWarnings("serial")
public class ProjectServlet extends HttpServlet {
	
	/**
	 * Constuctor for page
	 */
	public ProjectServlet(){};

	DatastoreServ data = new DatastoreServ();
	private Staff user = null;
	String username = null;
	
	/**
	 * 
	 * @return Staff which is currently logged in
	 */
	public Staff getCurrentUser()
	{
		return user;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */ 
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		//form to show the names of the staff in the system for now
		String http = "";
		http += "<form id=\"ccf\">"
		+			"<div id=\"title-create-staff\">"
		+				"Staff List"
		+			"</div>"
		+ 			"<div id=\"sub\">";
		
		ArrayList<Staff> users = data.getAllStaff();
		http += "There is " + users.size() + " users.<br><br>";
		for(Staff user:users){
			http += "Name: " + user.getName() + "<br>";
			
		}
		http += "</div>"
		+		"</form>";
		
		user = null;
		username = null;
		user = checkLogin(req, resp);
		banner(req,resp);
		layout(http,req,resp);
		menu(req,resp);
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	}
	
	/**
	 * create a banner for the page and include the CSS file
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	public void banner(HttpServletRequest req, HttpServletResponse resp)throws IOException{
		
		//Staff loggedIn = checkLogin(req, resp);

		user = checkLogin(req, resp);
		if (user == null && username.isEmpty())
		{
			resp.sendRedirect("/index.html");
		}
		
		resp.setContentType("text/html");
		
		resp.getWriter().println("<head>"
		+							"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"
		+							"<title>UW Milwaukee</title>"
		+							"<link href=\"main.css\" rel=\"stylesheet\" type=\"text/css\"/>"
		+							"<style type=\"text/css\"></style>"
		+						"</head>");
		
		resp.getWriter().println("<div class=\"banner\">"
		+							"<a class=\"plbrand mainlogo-link\" href=\"Admin_Home_Page.html\" title=\"UW-Milwaukee D2L\">"
		+								"<img class=\"mainlogo-img\" src=\"Images/UWM_D2L_banner_960w1.png\" alt=\"UW-Milwaukee D2L\">"
		+							"</a>"
		+						"</div>");
	}
	

	/**
	 * takes a string which will create the Contents of page
	 * 
	 * @param http
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	public void layout(String http, HttpServletRequest req, HttpServletResponse resp)throws IOException{
		resp.setContentType("text/html");
		 
		resp.getWriter().println("<div class=\"layout\">"
		+							"	<div class=\"page-after-banner\">"
		+									http
		+							"	</div>"
		+							"</div>");
	}
	

	/**
	 * takes a string which will create the main Contents of page
	 * @param http
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	public void courseListLayout(String http, HttpServletRequest req, HttpServletResponse resp)throws IOException{
		resp.setContentType("text/html");
		
		resp.getWriter().println("<div class=\"courselayout\">"
		+							"	<div class=\"page-after-banner\">"
		+									http
		+							"	</div>"
		+							"</div>");
	}
	
	/**
	 * menu methods create the menu on side of the page
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	public void menu(HttpServletRequest req, HttpServletResponse resp)throws IOException{
		resp.setContentType("text/html");
		resp.getWriter().println("<div class=\"menu\">");					
		resp.getWriter().println("	<div class=\"buttons\">");
									if(username.equals("admin@uwm.edu")){
		resp.getWriter().println("		<ul class=\"buttons-outline\">");
		resp.getWriter().println("			<li> <a href=\"/project\"> Home</a></li>");
		resp.getWriter().println("		</ul>");
		resp.getWriter().println("		<ul class=\"buttons-outline\">");
		resp.getWriter().println("			<li id=\"b1\"> <a href=\"#\">Admin Tools</a>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li> <a href=\"/scrape\"> Scrape</a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li><a href=\"/emailRequest\"> Contact Request</a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("			</li>");
		resp.getWriter().println("		</ul>");
		resp.getWriter().println("		<ul class=\"buttons-outline\">");
		resp.getWriter().println("			<li> <a href=\"#\">Manage Staff</a>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li><a href=\"/createStaff\"> Create Staff</a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li><a href=\"/viewStaff\"> View Staff</a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li><a href=\"/editStaff\"> Edit Staff</a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li><a href='/editStaffContact'> Edit Staff Contact</a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("			</li>");
		resp.getWriter().println("		</ul>");									
		resp.getWriter().println("		<ul class=\"buttons-outline\">");
		resp.getWriter().println("			<li> <a href=\"#\">Manage Courses</a>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li><a href=\"/courseList\">Course List</a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li><a href=\"/editSection\">Edit Section</a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("			</li>");
		resp.getWriter().println("		</ul>");
		resp.getWriter().println("		</ul>");
		resp.getWriter().println("	</div>");
		resp.getWriter().println("</div>");
									}
									else if(user.getPermissions().equals("Instructor")){
		resp.getWriter().println("		<ul class=\"buttons-outline\">");
		resp.getWriter().println("			<li> <a href=\"#\">Staff</a>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li><a href=\"/viewStaff\"> View Staff</a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("			</li>");
		resp.getWriter().println("		</ul>");	
		resp.getWriter().println("		<ul class=\"buttons-outline\">");
		resp.getWriter().println("			<li> <a href=\"#\"> Course</a>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li><a href=\"/courseList\">Course List</a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li><a href=\"/editSection\">Edit Section</a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("			</li>");
		resp.getWriter().println("		</ul>");
		resp.getWriter().println("		<ul class=\"buttons-outline\">");
		resp.getWriter().println("			<li> <a href=\"#\"> Account</a>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li> <a href=\"/viewMyContact\">View Account</a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li><a href=\"/editMyContact\">Edit Account</a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("			</li>");
		resp.getWriter().println("		</ul>");
		resp.getWriter().println("	</div>");
		resp.getWriter().println("</div>");
									}
									else {	
		resp.getWriter().println("		<ul class=\"buttons-outline\">");
		resp.getWriter().println("			<li> <a href=\"/courseList\"> My Sections</a>");
		resp.getWriter().println("			</li>");
		resp.getWriter().println("		</ul>");
		resp.getWriter().println("		</ul>");
		resp.getWriter().println("		<ul class=\"buttons-outline\">");
		resp.getWriter().println("			<li> <a href=\"#\"> Account</a>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li> <a href=\"/viewMyContact\">View Account</a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li><a href=\"/editMyContact\">Edit Account</a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("			</li>");
		resp.getWriter().println("		</ul>");
		resp.getWriter().println("	</div>");
		resp.getWriter().println("</div>");
									}
		
		resp.getWriter().println("<div class=\"menu-logout\">");					
		resp.getWriter().println("	<div class=\"buttons-logout\">");		
		resp.getWriter().println("		<ul class=\"buttons-outline\">");
		resp.getWriter().println("			<li> <a href='#'>Logout</a>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
												if(user != null){
		resp.getWriter().println("					<li><a href='/logout'>" + user.getEmail().toString() + "</a></li>");
												}
												else
		resp.getWriter().println("					<li><a href='/logout'> admin@uwm.edu </a></li>");
												
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("			</li>");
		resp.getWriter().println("		</ul>");
		resp.getWriter().println("	</div>");
		resp.getWriter().println("</div>");

	}

	/**
	 * Checks cookies to see if user is currently logged in.
	 * returns boolean, true if logged in
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws IOException
	 */
	public Staff checkLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		Cookie[] cookies = req.getCookies();

		if (cookies != null) {
			for (Cookie c : cookies) {
				if (c.getName().equals("username")) {
					username = c.getValue();
				}
			}
		}
		if(username == null)
			return null;

		List<Staff> staffList = data.getAllStaff();
		for(Staff usern:staffList){
			if(usern.getEmail().toString().equals(username)){
				return usern;
			}
		}

		return null;

	}
}
