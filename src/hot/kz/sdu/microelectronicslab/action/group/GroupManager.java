package kz.sdu.microelectronicslab.action.group;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import kz.sdu.microelectronicslab.action.OperationService;
import kz.sdu.microelectronicslab.action.project.ProjectManagementBean;
import kz.sdu.microelectronicslab.model.group.Group;
import kz.sdu.microelectronicslab.model.project.Project;
import kz.sdu.microelectronicslab.model.project.ProjectStatus;
import kz.sdu.microelectronicslab.model.user.User;
import kz.sdu.microelectronicslab.model.website.Theme;
import kz.sdu.microelectronicslab.model.website.WebSite;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;


@Name("groupManager")
@Scope(ScopeType.PAGE)
public class GroupManager implements Serializable
{
	@Logger Log log;
	@In protected Identity identity;
	
	@In("entityManager")
	private EntityManager em;
	
	@In(create=true)
	private GroupManagementBean groupManagementBean;
	
	@In(create=true)
	protected OperationService operationService;
	
	private List<Group> groups;
	
	@Out(required=false)
	protected Group group;
	
	@Out(required=false)
	protected WebSite website;
	
	@Out(required=false)
	protected List<Theme> themes;
	
	public void preparePage(String content)
	{
		if (content.equals("createGroup"))
		{
			group = new Group();
			log.info("preparePage createGroup");
		}
		
		else if (content.equals("viewGroup"))
		{
			HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			
			long groupId = Long.parseLong( req.getParameter("groupId") );
			group = em.find(Group.class, groupId);
		}
		
		else if (content.equals("addWebSite"))
		{
			website = new WebSite();
		}
		
		else if (content.equals("editWebSite"))
		{
			themes = (List<Theme>) em.createQuery("from Theme").getResultList();
			
			log.info("editWebSite #0", groupManagementBean.getWebSiteId());
			website = em.find(WebSite.class, groupManagementBean.getWebSiteId());
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Group> getGroups() {
		User user = (User) em.createQuery("from User where username = :username")
							.setParameter("username", identity.getUsername())
							.getSingleResult();
		
		if (groups != null)
			return groups;
		else
		{
			groups = new ArrayList<Group>();
			List<Group> allGroups = (List<Group>) em.createQuery("from Group").getResultList();
			
			for (Group g: allGroups)
			{	
				if (g.getParticipants().contains(user))
					groups.add(g);
			}
			return groups;
		}
	}
	
	public boolean doesUserBelongToGroup(long groupId)
	{
		Group g = em.find(Group.class, groupId);
		User user = (User) em.createQuery("from User where username = :username")
				.setParameter("username", identity.getUsername())
				.getSingleResult();
		
		return g.getParticipants().contains(user);
	}
	
	public boolean hasGroupUserIn(long userId)
	{
		User user = em.find(User.class, userId);
		for (User u: group.getParticipants())
			if (u.getId() ==  user.getId())
				return true;
		
		return false;
	}
	
	public List<User> retrieveParticipants(boolean belongs)
	{
		List<User> users = em.createQuery("from User").getResultList();
		List<User> list = new ArrayList<User>();
		
		for (User user: users)
			if ( !( belongs ^ hasGroupUserIn(user.getId()) ) )
				list.add(user);
		
		return list;
	}
	
	public void addParticipant()
	{
		group.getParticipants().add( em.find(User.class, groupManagementBean.getParticipantId()) );
		em.merge(group);
	}
	
	public void removeParticipant(long userId)
	{
		log.info(" remove participant {0} ", userId);
		
		if (group.getParticipants().size() == 1) return;
		
		group = em.find(Group.class, group.getId());
		group.getParticipants().remove( em.find(User.class, userId) );
		em.persist(group);
	}
	
	public void addWebSite()
	{
		website.setGroup(group);
		em.persist(website);
		
		group.getWebSites().add(website);
		em.merge(group);
	}
	
	public void editWebSite()
	{
		website.setGroup(group);
		website.setTheme(em.find(Theme.class, groupManagementBean.getThemeId()));
		
		em.merge(website);
		
		group = em.find(Group.class, group.getId());
	}
	
	public void removeWebSite(long webSiteId)
	{
		WebSite website = em.find(WebSite.class, webSiteId);
		
		group = em.find(Group.class, group.getId());
		group.getWebSites().remove(website);
		
		em.remove(website);
		em.persist(group);
	}
	
	public void create()
	{
		log.info("create");
		User user = (User) em.createQuery("from User where username = :username")
				.setParameter("username", identity.getUsername())
				.getSingleResult();

		group.getParticipants().add(user);
		em.persist(group);
		groups.add(group);
	}
	
	public void edit()
	{
		String name = group.getName();
		
		group = em.find(Group.class, group.getId());
		
		group.setName(name);
		em.merge(group);
		
		operationService.redirectToSystemPage("/group/view.seam?groupId=" + group.getId());
	}
	
	public void delete()
	{
		group = em.find(Group.class, group.getId());
		em.remove(group);
		
		operationService.redirectToSystemPage("/group/home.seam");
	}
}