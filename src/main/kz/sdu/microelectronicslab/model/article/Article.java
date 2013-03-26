package kz.sdu.microelectronicslab.model.article;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kz.sdu.microelectronicslab.model.user.User;

@Entity
public class Article implements Serializable
{
	private long id;
	private String title;
	private String content;
	private User author;
	private String icon;

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
	
	public User getAuthor()
	{
		return author;
	}
	
	public void setAuthor(User author)
	{
		this.author = author;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}