package kz.sdu.microelectronicslab.action.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

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
	
	@DataModel(scope=ScopeType.PAGE)
	private List<User> users;
	
	@DataModelSelection
	@Out(required=false)
	private User user;
	
	@Out(scope=ScopeType.PAGE,required=false)
	private List<RoleBean> userRoles;
	
	@In(create=true)
	private RoleManagementBean roleManagementBean;
	
	@Factory("users")
	public void retrieveUsers()
	{
		users = em.createQuery("FROM User").getResultList();
	}
	
	public void prepareManageUserValues()
	{
		user = em.find(User.class, roleManagementBean.getUserId());
	
		List<Role> roles = em.createQuery("FROM Role").getResultList();
		
		userRoles = new ArrayList<RoleBean>();
		for (Role role: roles)
		{
			RoleBean roleBean = new RoleBean();
			roleBean.setId(role.getId());
			roleBean.setName(role.getName());
			roleBean.setEnabled(isUserRoleEnabled(role));
			userRoles.add(roleBean);
		}
	}
	
	public void enableRole()
	{
		log.info("enabling role id {0}", roleManagementBean.getRoleId());
		
		user = em.find(User.class, roleManagementBean.getUserId());
		Role role = em.find(Role.class, roleManagementBean.getRoleId());
		
		user.getRoles().add(role);
		em.persist(user);
		
		prepareManageUserValues();
	}
	
	public void disableRole()
	{
		log.info("disabling role id {0}", roleManagementBean.getRoleId());
		
		user = em.find(User.class, roleManagementBean.getUserId());
		Role role = em.find(Role.class, roleManagementBean.getRoleId());
		
		user.getRoles().remove(role);
		em.persist(user);
		
		prepareManageUserValues();
	}
	
	private boolean isUserRoleEnabled(Role role)
	{
		for (Role userRole: user.getRoles())
			if (userRole.getId() == role.getId())
				return true;
				
    	return false;
	}
	
	public void delete()
	{
		log.info("deleting user {0}", user.getUsername());
		
		user = em.find(User.class, user.getId());
		em.remove(user);
		
		retrieveUsers();
	}
}