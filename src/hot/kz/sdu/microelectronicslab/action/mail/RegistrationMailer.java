package kz.sdu.microelectronicslab.action.mail;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.faces.Renderer;
import org.jboss.seam.log.Log;

@Name("registrationMailer")
public class RegistrationMailer {
	@In(create=true)
	private Renderer renderer;
	
	@Logger Log log;
	
	public void sendWelcomeEmail() {
		System.out.println("send email");
		log.info("sendWelcomeEmail");
		renderer.render("/email/welcome.xhtml");
	}
	
	public void send() {
	    try {
	       renderer.render("/email/simple.xhtml");
	       System.out.println("email sent successfully!");
			
	       log.info("email sent successfully!");
		} catch (Exception e) {
			System.out.println("exception on sending email");
			
			log.info("exception on sending email");
		}
	}
	
	public void sendAddDeveloper() {
		try {
			renderer.render("/email/developer/add.xhtml");
			log.info("email sent successfully!");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("exception on sending email");
		}
	}
}