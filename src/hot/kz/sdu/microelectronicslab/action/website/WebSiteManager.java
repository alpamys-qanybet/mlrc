package kz.sdu.microelectronicslab.action.website;

import java.io.Serializable;
import java.util.ArrayList;
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

@Name("websiteManager")
@Scope(ScopeType.PAGE)
public class WebSiteManager implements Serializable
{
	@Logger Log log;
	
	@In protected Identity identity;
    
	@In("entityManager")
	private EntityManager em;
	
	@In(create=true)
	protected OperationService operationService;
	
	@Out(required=false)
	private WebSite website;
	
	public void preparePage(String content)
	{
		if (content.equals("viewWebsite"))
		{
			HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
				
			long websiteId = Long.parseLong( req.getParameter("websiteId") );
			website = em.find(WebSite.class, websiteId);
		}
	}
}