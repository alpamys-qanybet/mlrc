package kz.sdu.microelectronicslab.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.validator.Email;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.security.management.UserEnabled;
import org.jboss.seam.annotations.security.management.UserFirstName;
import org.jboss.seam.annotations.security.management.UserLastName;
import org.jboss.seam.annotations.security.management.UserPassword;
import org.jboss.seam.annotations.security.management.UserPrincipal;
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
	private String passwordHash;
	private Set<Role> roles = new HashSet<Role>();
	private String firstname;
	private String lastname;
	private String email;
	private boolean enabled;
	
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
	
	@UserPrincipal
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
	
	@UserPassword(hash="md5")
	@Column(name="password", nullable=false)
	@NotNull
	public String getPasswordHash()
	{
		return passwordHash;
	}
	
	public void setPasswordHash(String passwordHash)
	{
		this.passwordHash = passwordHash;
	}
	
	@UserFirstName
	public String getFirstname()
	{
		return firstname;
	}

	public void setFirstname(String firstname)
	{
		this.firstname = firstname;
	}

	@UserLastName
	public String getLastname()
	{
		return lastname;
	}

	public void setLastname(String lastname)
	{
		this.lastname = lastname;
	}

	@UserEnabled
	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
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
}