package edu.uwm.cs361;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class EmailRequestServlet extends HttpServlet
{
	ProjectServlet page = new ProjectServlet();
	DatastoreServ data = new DatastoreServ();
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		page.banner(req,resp);
		page.layout(displayForm(req,resp,""),req,resp);
		page.menu(req,resp);

	}

	/**
	 * Sends email
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		
		String requestSelection = req.getParameter("requesttype");
		
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		
		String msgBody = "...";
		
		List<Staff> users = data.getAllStaff();

		//option 1
		if (requestSelection.equalsIgnoreCase("missing contact info"))
		{
			System.out.println("Got option 1");
			
			/*
			try {
				Message msg = new MimeMessage(session);
				msg.setFrom(new InternetAddress("demeritsquad@gmail.com", "Demerit Squad"));
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(req.getParameter("email"), req.getParameter("email")));
				msg.setSubject("Test Email");
				msg.setText(msgBody);
				Transport.send(msg);
			} catch (Exception e) {
				resp.getWriter().println("Problem sending email to " + req.getParameter("email") + ".");
				return;
			}
			*/
		}
		//option 2
		else {
			System.out.println("Got option 2");
			/*
			try {
				Message msg = new MimeMessage(session);
				msg.setFrom(new InternetAddress("eric@eric-fritz.com", "Eric Fritz"));
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(req.getParameter("email"), req.getParameter("email")));
				msg.setSubject("Test Email");
				msg.setText(msgBody);
				Transport.send(msg);
			} catch (Exception e) {
				resp.getWriter().println("Problem sending email to " + req.getParameter("email") + ".");
				return;
			}
			*/
		}
		page.banner(req,resp);
		page.layout(displayForm(req,resp,requestSelection),req,resp);
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
	private String displayForm(HttpServletRequest req, HttpServletResponse resp, String request) throws IOException
	{
		resp.setContentType("text/html");
		String http = "";
		
		
		//List<Staff> users = data.getAllStaff(); 
		
		
		http += "<form id=\"ccf\" method=\"POST\" action=\"/emailRequest\">"
		+			"<div id=\"title-create-staff\">"
		+				"Email Request for Information"
		+			"</div>";

		http += 	"<div id=\"sub\">"
		+				"<table>"
		+					"<tr>"
		+						"<td class='form'>"
		+							"<select id='requesttype' name='requesttype' class='staff-select staff-view-margin' required>"
		+									"<option value = '' selected> Select a request </option>";		
											http += "<option>missing contact info</option>";
											http += "<option>outdated contact info</option>";
		http +=						"</select>"
		+						"</td>"
		+					"</tr>"
		+					"<tr>"
		+					"<td></td>"
		+					"<td>"
		+						"<input class='view-submit-staff' type='submit' value='Submit' />"
		+					"</td>"
		+				"</tr>";
		
		if (!request.isEmpty())
		{			
			http+=	"<tr>"
			+			"<td class='view-staff'>"
			+				"Email request for " + request + " has been sent.<br><br>"
			+			"</td>"
			+		"</tr>";
		}
					
		http+= 		"</table>";
		http+= 		"<table>"
		+				"<tr>"
		+					"<td class='view-staff'>"
		+						"Missing contact info: automated email message requesting all staff who have missing info to update<br>"
		+ 						"Outdated contact info: automated email message requesting all staff verify current info is up to date"
		+					"</td>"
		+				"</tr>";
		http+= 		"</table>";
		
		http +=		"</div>"
		+	"</form>";
		
		
		
		return http;
	}
}
