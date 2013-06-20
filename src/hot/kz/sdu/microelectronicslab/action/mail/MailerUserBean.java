package kz.sdu.microelectronicslab.action.mail;

import java.util.ArrayList;
import java.util.List;

import kz.sdu.microelectronicslab.action.user.RoleBean;
import kz.sdu.microelectronicslab.model.project.Project;
import kz.sdu.microelectronicslab.model.user.User;

import org.jboss.seam.annotations.Name;

@Name("mailerUserBean")
public class MailerUserBean {
	
	private User from;
	private User to;
	private Project project;
	private List<RoleBean> userRoles = new ArrayList<RoleBean>();

	public User getFrom() {
		return from;
	}
	
	public void setFrom(User from) {
		this.from = from;
	}
	
	public User getTo() {
		return to;
	}
	
	public void setTo(User to) {
		this.to = to;
	}

	public Project getProject() {
		return project;
	}
	
	public void setProject(Project project) {
		this.project = project;
	}

	public List<RoleBean> getUserRoles() {
		return userRoles;
	}
	public void setUserRoles(List<RoleBean> userRoles) {
		this.userRoles = userRoles;
	}
}