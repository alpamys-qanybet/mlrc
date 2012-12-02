package kz.sdu.microelectronicslab.model.project;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.validator.NotNull;


import kz.sdu.microelectronicslab.model.user.User;

@Entity
public class Project implements Serializable
{
	private long id;
	private String name;
	private String description;
	private User manager;
	private List<User> developers;
	private ProjectStatus status;
	
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

	@Column(nullable=false)
	@NotNull
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	@Column(columnDefinition="TEXT")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne
	@JoinColumn(name = "MANAGER_ID", referencedColumnName = "ID", nullable = false)
	public User getManager()
	{
		return manager;
	}

	public void setManager(User manager)
	{
		this.manager = manager;
	}
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="PROJECT_DEV",
			joinColumns=@JoinColumn(name="project_id"),
			inverseJoinColumns=@JoinColumn(name="rc_user_id"))
	public List<User> getDevelopers()
	{
		return developers;
	}

	public void setDevelopers(List<User> developers)
	{
		this.developers = developers;
	}

	@ManyToOne
	@JoinColumn(name = "STATUS_ID", referencedColumnName = "ID", nullable = false)
	public ProjectStatus getStatus()
	{
		return status;
	}

	public void setStatus(ProjectStatus status)
	{
		this.status = status;
	}
}