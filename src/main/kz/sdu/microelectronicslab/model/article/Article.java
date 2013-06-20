package kz.sdu.microelectronicslab.model.article;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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

import kz.sdu.microelectronicslab.model.user.User;
import kz.sdu.microelectronicslab.model.website.WebSite;

@Entity
public class Article implements Serializable
{
	private long id;
	private String title;
	private String content;
	private List<User> authors = new ArrayList<User>();
	private String icon;
	private WebSite webSite;
	private Date date;

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
	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	@Column(columnDefinition="TEXT", nullable=false)
	public String getContent()
	{
		return content;
	}
	
	public void setContent(String content)
	{
		this.content = content;
	}
	
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="ARTICLE_AUTHOR",
			joinColumns=@JoinColumn(name="article_id"),
			inverseJoinColumns=@JoinColumn(name="user_id"))
	public List<User> getAuthors()
	{
		return authors;
	}

	public void setAuthors(List<User> authors)
	{
		this.authors = authors;
	}

	public String getIcon()
	{
		return icon;
	}

	public void setIcon(String icon)
	{
		this.icon = icon;
	}

	@ManyToOne
	@JoinColumn(name = "web_site_id", referencedColumnName = "id", nullable = true)
	public WebSite getWebSite()
	{
		return webSite;
	}

	public void setWebSite(WebSite webSite)
	{
		this.webSite = webSite;
	}

	@Column(name="DATE_", nullable=true)
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}