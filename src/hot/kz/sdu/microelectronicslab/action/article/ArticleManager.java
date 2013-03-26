package kz.sdu.microelectronicslab.action.article;

import java.io.Serializable;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kz.sdu.microelectronicslab.action.ConfigurationBean;
import kz.sdu.microelectronicslab.action.FileService;
import kz.sdu.microelectronicslab.action.OperationService;
import kz.sdu.microelectronicslab.action.project.ProjectManagementBean;
import kz.sdu.microelectronicslab.model.article.Article;
import kz.sdu.microelectronicslab.model.project.Project;
import kz.sdu.microelectronicslab.model.project.ProjectStatus;
import kz.sdu.microelectronicslab.model.user.User;

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
	
	@Out(scope=ScopeType.PAGE,required=false)
	protected Article article;
	
	@Out(scope=ScopeType.PAGE,required=false)
	private List<Article> articles;
	
	@In(create=true)
	protected ArticleBean articleBean;
	
	public void preparePage(String content)
	{
		articleService.setGalleryLeft(configurationBean.getFileServerHost() + "/static/img/gallery/left-arrow.png");
		articleService.setGalleryRight(configurationBean.getFileServerHost() + "/static/img/gallery/right-arrow.png");
		
		if (content.equals("articleList"))
		{
			articles = em.createQuery("FROM Article").getResultList();
		}
		
		else if (content.equals("createArticle"))
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
	}
	
	public boolean isAuthor(long articleId)
	{
		return em.find(Article.class, articleId).getAuthor().getUsername().equals( identity.getUsername() );
	}
	
	public void create()
	{
		log.info("create {0}", article.getContent() );
		
		User user = (User) em.createQuery("FROM User WHERE username=:username")
							.setParameter("username", identity.getUsername())
							.getSingleResult();
		
		article.setAuthor(user);
		
		String url = configurationBean.getFileServerHost() + "/article/" + fileService.saveArticleIcon();
		article.setIcon(url);
		
		em.persist(article);
	}
	
	public void edit()
	{
		String title = article.getTitle();
		String content = article.getContent();
		User author = article.getAuthor();
		String icon = article.getIcon();
		
		article = em.find(Article.class, article.getId());
		
		article.setTitle(title);
		article.setContent(content);
		article.setAuthor(author);
		article.setIcon(icon);
		
		em.merge(article);
		
		articleBean.setRequestFromModal(true);
		articleBean.setArticleId(article.getId());
	}
}