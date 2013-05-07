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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import kz.sdu.microelectronicslab.model.article.Article;
import kz.sdu.microelectronicslab.model.group.Group;
import kz.sdu.microelectronicslab.model.user.User;

@Entity
public class WebSite implements Serializable{
	private long id;
	private String url;
	private String host;
	private String description;
	private List<Article> articles;
	private Group group;
	private Theme theme;
	
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
	
	@Column
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	@Column
	public String getUrl()
	{
		return url;
	}
	
	public void setUrl(String url)
	{
		this.url = url;
	}
	
	@Column
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "webSite", cascade = CascadeType.ALL)
	public List<Article> getArticles()
	{
		return articles;
	}

	public void setArticles(List<Article> articles)
	{
		this.articles = articles;
	}

	@ManyToOne
	@JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
	public Group getGroup()
	{
		return group;
	}

	public void setGroup(Group group)
	{
		this.group = group;
	}

	@ManyToOne
	@JoinColumn(name = "theme_id", referencedColumnName = "id")
	public Theme getTheme()
	{
		return theme;
	}

	public void setTheme(Theme theme)
	{
		this.theme = theme;
	}
}