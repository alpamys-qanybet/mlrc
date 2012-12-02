package kz.sdu.microelectronicslab.action.user;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kz.sdu.microelectronicslab.action.OperationService;
import kz.sdu.microelectronicslab.model.user.Role;
import kz.sdu.microelectronicslab.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

@Name("userManager")
@Scope(ScopeType.PAGE)
public class UserManager implements Serializable
{
	@Logger Log log;
	@In protected Identity identity;
    
	@In("entityManager")
	private EntityManager em;
	
	@In(create=true)
	private RoleManagementBean roleManagementBean;
	
	@In(create=true)
	private OperationService operationService;
	
	@Out(scope=ScopeType.PAGE, required=false)
	private List<User> users;
	
	@Out(required=false)
	private User user;
	
	@Out(scope=ScopeType.PAGE,required=false)
	private List<RoleBean> userRoles;
	
	public void preparePage(String content)
	{
		if (content.equals("userList"))
			users = em.createQuery("FROM User").getResultList();
		
		else if (content.equals("manageUser"))
		{
			user = em.find(User.class, roleManagementBean.getUserId());
			List<Role> roles = em.createQuery("FROM Role").getResultList();
		
			if (!roleManagementBean.isRequestFromModal())
			{
				userRoles = new ArrayList<RoleBean>();
			
				for (Role role: roles)
				{
					String rolename = role.getName();
					if ( !identity.hasRole("admin") && rolename.equals("admin") )
						continue;
					
					RoleBean roleBean = new RoleBean();
					roleBean.setId( role.getId() );
					roleBean.setName( rolename );
					roleBean.setEnabled( hasUserRole(user, rolename) );
					userRoles.add(roleBean);
				}
			}
		}
	}
	
	private boolean hasUserRole(User user, String rolename)
	{
		for (Role userRole: user.getRoles())
			if ( userRole.getName().equals(rolename) )
				return true;
		
		return false;
	}
	
	public boolean isUser(long userId, String rolename)
	{
		return hasUserRole( em.find(User.class, userId), rolename);
	}

	public void setRoleEnabled(long roleId, boolean isEnabled)
	{
		for (RoleBean roleBean: userRoles)
			if (roleBean.getId() == roleId)
			{	
				roleBean.setEnabled(isEnabled);
				break;
			}
	}
	
	public void edit()
	{
		log.info("*** saving changes {0}", "Haha");
		user = em.find(User.class, user.getId());
		
		for (RoleBean roleBean:userRoles)
		{
			Role role = em.find( Role.class, roleBean.getId() );
			
			boolean userHasRole = false;
			
			for (Role r: user.getRoles())
				if (r.getId() == roleBean.getId())
				{
					userHasRole = true;
					break;
				}
			
			if (roleBean.isEnabled())
			{
				if (!userHasRole)
				{
					user.getRoles().add(role);
					em.persist(user);
				}
			}
			else
			{	
				if (userHasRole)
				{
					user.getRoles().remove(role);
					em.persist(user);
				}
			}	
		}
	}
	
	public void delete()
	{
		user = em.find(User.class, user.getId());
		em.remove(user);
	}
}