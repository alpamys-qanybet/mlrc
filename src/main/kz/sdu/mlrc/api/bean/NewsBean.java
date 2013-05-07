package kz.sdu.mlrc.api.bean;

import java.util.List;

public class NewsBean
{
	List<ArticleBean> articles;
	int count;
	WebSiteBean web_site;
	
	public NewsBean() {}
	
	public List<ArticleBean> getArticles()
	{
		return articles;
	}
	
	public void setArticles( List<ArticleBean> articles )
	{
		this.articles = articles;
		setCount( articles.size() );
	}
	
	public int getCount()
	{
		return count;
	}
	
	public void setCount( int count )
	{
		this.count = count;
	}

	public WebSiteBean getWeb_site() {
		return web_site;
	}

	public void setWeb_site(WebSiteBean web_site) {
		this.web_site = web_site;
	}
}