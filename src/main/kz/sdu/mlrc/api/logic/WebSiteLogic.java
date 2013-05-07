package kz.sdu.mlrc.api.logic;

import javax.persistence.EntityManager;
import kz.sdu.microelectronicslab.model.website.WebSite;
import kz.sdu.mlrc.api.bean.WebSiteBean;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

@Name("webSiteLogic")
public class WebSiteLogic
{
	@In("entityManager")
	EntityManager em;
	
	public WebSiteBean getWebSiteById( Long id )
	{
		WebSite webSite = em.find(WebSite.class, id);
		
		WebSiteBean webSiteBean = new WebSiteBean();
		webSiteBean.setId( webSite.getId() );
		webSiteBean.setDescription( webSite.getDescription() );
		webSiteBean.setCssUrl( webSite.getTheme().getCssUrl() );
		
		return webSiteBean;
	}
}
