package kz.sdu.microelectronicslab.model.group;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
import kz.sdu.microelectronicslab.model.user.User;
import kz.sdu.microelectronicslab.model.website.WebSite;

@Entity
@Table(name="groups")
public class Group implements Serializable
{
	private long id;
	private String name;
	private List<WebSite> webSites;
	private List<User> participants = new ArrayList<User>();
	
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

	@Column(nullable = false, unique = true)
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "group", cascade = CascadeType.ALL)
	public List<WebSite> getWebSites()
	{
		return webSites;
	}
	
	public void setWebSites(List<WebSite> webSites)
	{
		this.webSites = webSites;
	}

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="GROUP_USER",
			joinColumns=@JoinColumn(name="group_id"),
			inverseJoinColumns=@JoinColumn(name="user_id"))
	public List<User> getParticipants()
	{
		return participants;
	}

	public void setParticipants(List<User> participants)
	{
		this.participants = participants;
	}
}