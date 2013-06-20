package kz.sdu.microelectronicslab.action.article;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kz.sdu.microelectronicslab.action.ConfigurationBean;
import kz.sdu.microelectronicslab.action.FileService;
import kz.sdu.microelectronicslab.action.OperationService;
import kz.sdu.microelectronicslab.action.group.GroupManagementBean;
import kz.sdu.microelectronicslab.model.article.Article;
import kz.sdu.microelectronicslab.model.group.Group;
import kz.sdu.microelectronicslab.model.user.User;
import kz.sdu.microelectronicslab.model.website.WebSite;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

@Name("articleManager")
@Scope(ScopeType.PAGE)
public class ArticleManager implements Serializable
{
	@Logger Log log;
	
	@In protected Identity identity;
    
	@In("entityManager")
	private EntityManager em;
	
	@In(create=true)
	protected ConfigurationBean configurationBean;
	
	@In(create=true)
	private FileService fileService;
	
	@In(create=true)
	protected OperationService operationService;
	
	@In(create=true)
	private ArticleService articleService;
	
	@In(create=true)
	private ArticleManagementBean articleManagementBean;

	
	@Out(scope=ScopeType.PAGE,required=false)
	protected Article article;
	
	private List<Article> articles;
	
	@In(create=true)
	protected ArticleBean articleBean;
	
	@In(create=true)
	private GroupManagementBean groupManagementBean;
	
	public void preparePage(String content)
	{
		articleService.setGalleryLeft(configurationBean.getFileServerHost() + "/static/img/gallery/left-arrow.png");
		articleService.setGalleryRight(configurationBean.getFileServerHost() + "/static/img/gallery/right-arrow.png");
		
		if (content.equals("createArticle"))
		{
			article = new Article();
		}
		
		else if (content.equals("editArticle"))
		{
			article = em.find( Article.class, articleBean.getArticleId() );
		}
		
		else if (content.equals("viewArticle"))
		{
			if (articleBean.isRequestFromModal())
				article = em.find(Article.class, articleBean.getArticleId());
			else
			{
				HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
				
				long articleId = Long.parseLong( req.getParameter("articleId") );
				article = em.find(Article.class, articleId);
			}
		}
		else if (content.equals("searchArticle"))
		{
			log.info("searchArticle " + articleBean.getSearchText());
			String searchText = articleBean.getSearchText();
			boolean searchIsRelatedWithAuthor = false;
			List<BigInteger> searchByAuthorList = new ArrayList<BigInteger>();
			try {
				User user = (User) em.createQuery("FROM User "+
												  "WHERE realname like '%" +searchText+ "%' ")
								   .getSingleResult();
				
				log.info("username #0 userId #1", user.getUsername(), user.getId());
				
				searchByAuthorList = em.createNativeQuery("SELECT article_id " +
														  "FROM ARTICLE_AUTHOR " +
						                                  "WHERE user_id = :userId")
						                                  .setParameter("userId", user.getId())
						                                  .getResultList();
				
				searchIsRelatedWithAuthor = true;
			}
			catch(Exception e) {
				e.printStackTrace();
				searchIsRelatedWithAuthor = false;
			}	
			articles = em.createQuery("FROM Article " +
									  "WHERE content like '%"+searchText+"%' " + 
									     "OR title like '%"+searchText+"%' ORDER BY date")
						  .getResultList();
			
			log.info("articles size A " + articles.size());
			
			if (searchIsRelatedWithAuthor)
			{
				if (articles.isEmpty())
				{
					for (BigInteger articleId : searchByAuthorList)
						articles.add( em.find(Article.class, articleId.longValue()) );
					
					log.info("articles size B " + articles.size());
				}
				else
				{
					List<Article> listTemp = new ArrayList<Article>();
					
					for (Article art: articles)
					{
						boolean exists = false;
						long artId = 0;
						for (BigInteger articleId : searchByAuthorList)
							if (art.getId() == articleId.longValue())
							{
								exists = true;
								artId = articleId.longValue();
								break;
							}
				
						if (!exists)
							listTemp.add( em.find(Article.class, artId) );
					}
					
					articles.addAll(listTemp);
					
					log.info("articles size C " + articles.size());
				}
			}
		}
	}
	
	public List<Article> getArticles() {
		if (articles != null)
			return articles;
		articles = em.createQuery("FROM Article ORDER BY date").getResultList();
		
		return articles;
	}
	
	public List<WebSite> retrieveWebSites()
	{
		User user = (User) em.createQuery("from User where username = :username")
				.setParameter("username", identity.getUsername())
				.getSingleResult();

		List<Group> groups = new ArrayList<Group>();
		List<Group> allGroups = (List<Group>) em.createQuery("from Group").getResultList();
		
		for (Group g: allGroups)
			if (g.getParticipants().contains(user))
				groups.add(g);
		
		List<WebSite> websites = new ArrayList<WebSite>();
		
		for (Group g:groups)
			websites.addAll(g.getWebSites());
		
		return websites;
	}
	
	public void linkWebSite()
	{
		article.setWebSite( em.find(WebSite.class, groupManagementBean.getWebSiteId()) );
		em.merge(article);
		
		articleBean.setRequestFromModal(true);
		articleBean.setArticleId(article.getId());
	}
	
	public boolean isAuthor(long articleId)
	{
		if (identity.getUsername() == null)
			return false;
		
		Article a = em.find(Article.class, articleId);
		
		User user = (User)em.createQuery("from User where username = :username")
							.setParameter("username", identity.getUsername())
							.getSingleResult();
		
		return isAuthorOfArticle(user.getId(), a.getAuthors());
	}
	
	public void create()
	{
		log.info("create {0}", article.getContent() );
		
		User user = (User) em.createQuery("FROM User WHERE username=:username")
							.setParameter("username", identity.getUsername())
							.getSingleResult();
		
		if ( !isAuthorOfArticle(user.getId(), article.getAuthors()) )
			article.getAuthors().add(user);
		
		String url = configurationBean.getFileServerHost() + "/article/" + fileService.saveArticleIcon();
		article.setIcon(url);
		article.setDate(new Date());
		
		em.persist(article);
	}
	
	public boolean isAuthorOfArticle(long userId, List<User> list) {
		for (User author: list)
			if (author.getId() == userId)
				return true;
		
		return false;
	}
	
	public List<User> retrieveAuthors(boolean belongs) {
		List<User> users = em.createQuery("FROM User").getResultList();
		List<User> list = new ArrayList<User>();
		
		for (User user: users)
			if ( !( belongs ^ isAuthorOfArticle(user.getId(), article.getAuthors()) ) )
				list.add(user);
		
		return list;
	}
	
	public void addAuthor()
	{
		article.getAuthors().add( em.find(User.class, articleManagementBean.getAuthorId()) );
		em.merge(article);
	}
	
	public void removeAuthor(long userId)
	{
		log.info(" remove author {0} ", userId);
		
		article = em.find(Article.class, article.getId());
		
		if (article.getAuthors().size() == 1) return;
		
		article.getAuthors().remove( em.find(User.class, userId) );
		em.persist(article);
	}
	
	
	public void edit()
	{
		String title = article.getTitle();
		String content = article.getContent();
//		User author = article.getAuthor();
		String icon = article.getIcon();
		Date date = article.getDate();
		
		article = em.find(Article.class, article.getId());
		
		article.setTitle(title);
		article.setContent(content);
//		article.setAuthor(author);
		article.setIcon(icon);
		article.setDate(date);
		
		em.merge(article);
		
		articleBean.setRequestFromModal(true);
		articleBean.setArticleId(article.getId());
	}
}