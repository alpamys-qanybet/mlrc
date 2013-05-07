package kz.sdu.mlrc.api.logic;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.drools.lang.DRLParser.or_constr_return;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.transaction.Transaction;
import org.jboss.seam.transaction.UserTransaction;

import kz.sdu.microelectronicslab.model.article.Article;
import kz.sdu.microelectronicslab.model.website.WebSite;
import kz.sdu.mlrc.api.bean.ArticleBean;
import kz.sdu.mlrc.api.bean.WebSiteBean;

public class ArticleLogic
{
	EntityManager em;
	
	public List<ArticleBean> loadArticlesByWebSiteId(Long id) throws Exception
	{
		UserTransaction txn = Transaction.instance();
		txn.begin();
		
		em = (EntityManager) Component.getInstance("entityManager");
		em.joinTransaction();
		 
		WebSite website = em.find(WebSite.class, id); 
		List<Article> articleEntityList = website.getArticles();
		List<ArticleBean> articleBeanList = new ArrayList<ArticleBean>();
		
		for (Article article : articleEntityList)
		{
			ArticleBean articleBean = new ArticleBean();
			articleBean.setId( article.getId() );
			articleBean.setShort_text( article.getTitle());
			articleBean.setFull_text( article.getContent() );
			articleBean.setIcon_host( article.getIcon() );
			
			articleBeanList.add(articleBean);
			
			System.out.println("articleId " + article.getId() );
		}

		em.flush();
		
		txn.commit();
		return articleBeanList;
	}
	
/*	public ArticleBean loadArticleById( Long id )
	{
		ArticleDAO articleDAO = new ArticleDAO();
		ArticleEntity articleEntity = articleDAO.getArticleById( id );
		
		ArticleBean articleBean = new ArticleBean();
		articleBean.setId( articleEntity.getId() );
		articleBean.setFull_text( articleEntity.getFull_text() );
		articleBean.setIcon_host( Configuration.imgHost + "/" + articleEntity.getWeb_site().getUrl() + "/" + articleEntity.getIcon_name() );
		
		return articleBean;
	}*/
	
	public WebSiteBean getWebSiteById( Long id ) throws Exception
	{
		UserTransaction txn = Transaction.instance();
		txn.begin();
		
		em = (EntityManager) Component.getInstance("entityManager");
		em.joinTransaction();
		
		WebSite webSite = em.find(WebSite.class, id);
		
		WebSiteBean webSiteBean = new WebSiteBean();
		webSiteBean.setId( webSite.getId() );
		webSiteBean.setDescription( webSite.getDescription() );
		webSiteBean.setCssUrl("asfdasf"); //webSite.getTheme().getCssUrl() );
		
		
		System.out.println("webSiteId " + webSite.getId());

		em.flush();

		txn.commit();
		return webSiteBean;
	}
}