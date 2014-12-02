package edu.uwm.cs361;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class AuthenticationServlet extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String username = req.getParameter("login");
		String password = req.getParameter("password");

		DatastoreServ ds = new DatastoreServ();

		// admin?
		if (username.equals("admin@uwm.edu")) {

			if (password.equals(ds.getAdminPassword())) {

				Cookie c = new Cookie("username", username);
				resp.addCookie(c);

				resp.sendRedirect("/project");
			} else {
				resp.sendRedirect("loginError.html");
			}
		}

		else {

			// get all users
			List<Staff> staffList = ds.getAllStaff();

			boolean validUser = false;

			// any users have given login(email)?
			// System.out.println("Checking: " + username + " / " + password);
			for (Staff staff : staffList) {

				// System.out.println("against: " + staff.getEmail() + " / " +
				// staff.getPassword());

				if (staff.getEmail().equalsIgnoreCase(username)) {
					//System.out.println("" + password.length());
					//System.out.println("" + staff.getPassword().length());

					if (staff.getPassword().equals(password)) {
						// System.out.println("Passes matched");
						validUser = true;
						Cookie c = new Cookie("username", username);
						resp.addCookie(c);
						resp.sendRedirect("/project");
						break;
					} else {
						// System.out.println("Passes failed");
						resp.sendRedirect("loginError.html");
						break;
					}
				}
			}
			if (validUser == false)
				resp.sendRedirect("loginError.html");
		}
	}
}