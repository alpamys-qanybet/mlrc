package kz.sdu.microelectronicslab.model.website;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Theme implements Serializable
{
	private long id;
	private String name;
	private String cssUrl;
	private List<WebSite> websites;
	
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
	
	@Column(nullable = false, unique = true)
	public String getCssUrl()
	{
		return cssUrl;
	}
	
	public void setCssUrl(String cssUrl)
	{
		this.cssUrl = cssUrl;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "theme", cascade = CascadeType.ALL)
	public List<WebSite> getWebsites()
	{
		return websites;
	}

	public void setWebsites(List<WebSite> websites)
	{
		this.websites = websites;
	}
}