package kz.sdu.microelectronicslab.model.user;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import kz.sdu.microelectronicslab.model.group.Group;
import kz.sdu.microelectronicslab.model.project.Project;
import kz.sdu.microelectronicslab.model.website.WebSite;

import org.hibernate.validator.Email;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.security.management.UserRoles;

@Entity
@Table(name="rc_user", uniqueConstraints={
		@UniqueConstraint(columnNames="username"),
		@UniqueConstraint(columnNames="email")
})
public class User implements Serializable
{
	private long id;
	private String username;
	private String password;
	private Set<Role> roles = new HashSet<Role>();
	private String realname;
	private String email;
	private String avatar;
	private String bio;
	private List<Project> projectsManage;
	private boolean emailNotificationEnabled = false;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}
	
	@Column(name="username",nullable=false)
	@NotNull
	@Length(min=6)
	public String getUsername()
	{
		return username;
	}
	
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	@Column(name="password", nullable=false)
	@NotNull
	public String getPassword()
	{
		return password;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public String getRealname()
	{
		return realname;
	}

	public void setRealname(String realname)
	{
		this.realname = realname;
	}

	@Column(name="email", nullable=false)
	@NotNull
	@Email
	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	@UserRoles
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="USER_ROLE",
			joinColumns=@JoinColumn(name="rc_user_id"),
			inverseJoinColumns=@JoinColumn(name="role_id"))
	public Set<Role> getRoles()
	{
		return roles;
	}
	
	public void setRoles(Set<Role> roles)
	{
		this.roles = roles;
	}

	/* media */
	@Column(name="avatar")
	public String getAvatar()
	{
		return avatar;
	}

	public void setAvatar(String avatar)
	{
		this.avatar = avatar;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "manager", cascade = CascadeType.ALL)
	public List<Project> getProjectsManage()
	{
		return projectsManage;
	}

	public void setProjectsManage(List<Project> projectsManage)
	{
		this.projectsManage = projectsManage;
	}

	@Column(columnDefinition="TEXT")
	public String getBio()
	{
		return bio;
	}

	public void setBio(String bio)
	{
		this.bio = bio;
	}

	@Column(name="emailnotification")
	public boolean isEmailNotificationEnabled() {
		return emailNotificationEnabled;
	}

	public void setEmailNotificationEnabled(boolean emailNotificationEnabled) {
		this.emailNotificationEnabled = emailNotificationEnabled;
	}
}