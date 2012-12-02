package kz.sdu.microelectronicslab.action.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import kz.sdu.microelectronicslab.action.ConfigurationBean;
import kz.sdu.microelectronicslab.model.user.Role;
import kz.sdu.microelectronicslab.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

@Name("profileManager")
@Scope(ScopeType.PAGE)
public class ProfileManager implements Serializable
{
	@Logger Log log;
	
	@In protected Identity identity;
    
	@In("entityManager")
	private EntityManager em;
	
	@In(create=true)
	protected ConfigurationBean configurationBean;

	@In(create=true)
	private RoleManagementBean roleManagementBean;
	
	@Out(required=true)
	protected User user;
	
	@Out(scope=ScopeType.PAGE,required=false)
	private List<Role> roles;
	
	public void preparePage()
	{
		user = (User) em.createQuery("FROM User WHERE username = :username")
				  .setParameter("username", identity.getUsername())
				  .getSingleResult();
		
		roles = new ArrayList<Role>();
		if (user.getRoles() != null)
        {
			for (Role role: user.getRoles())
        		roles.add(role);
        }
		
		String rolename = null;
		
		if ( identity.hasRole("admin") )
			rolename = "admin";
		else if ( identity.hasRole("manager") )
			rolename = "manager";
		else if ( identity.hasRole("developer") )
			rolename = "developer";
		
		Role role = (Role) em.createQuery("FROM Role WHERE name = :name")
						    .setParameter("name", rolename).getSingleResult();
		
		roleManagementBean.setRoleId( role.getId() );
	}
	
	public String changeAvatar(String filename)
	{
		// old
		String oldAvatarHost = user.getAvatar();
		String oldAvatarName = oldAvatarHost.substring(oldAvatarHost.lastIndexOf('/') + 1);
		
		// new
		String url = configurationBean.getFileServerHost() + "/user/avatar/" + filename;
		
		user.setAvatar(url);
		em.merge(user);
		
		return oldAvatarName;
	}
	
	public void save()
	{
		log.info("*** profileManager save() {0}", user.getRealname());
		em.merge(user);
		
		if (user.getRoles() != null)
        {
        	for (Role role: user.getRoles())
        		identity.removeRole(role.getName());
        	
        	Role role = em.find(Role.class, roleManagementBean.getRoleId() );
        	identity.addRole( role.getName() );
        }
	}
}