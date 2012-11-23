package kz.sdu.microelectronicslab.action.user;

import java.io.Serializable;

import javax.persistence.EntityManager;

import kz.sdu.microelectronicslab.action.user.PasswordBean;
import kz.sdu.microelectronicslab.action.user.PasswordManager;
import kz.sdu.microelectronicslab.model.user.Role;
import kz.sdu.microelectronicslab.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

@Name("registrationManager")
@Scope(ScopeType.PAGE)
public class RegistrationManager implements Serializable
{
	@Logger Log log;
	
	@In protected Identity identity;
	
	@In("entityManager")
	protected EntityManager em;
	
	@Out(required=true)
	private User user;
	
	@In protected FacesMessages facesMessages;
	
	@In(create=true) 
	protected PasswordManager passwordManager;
	
	@In(create=true)
	protected PasswordBean passwordBean;
	
	public void preparePage()
	{
		user = new User();
	}
	
	public String register()
	{
		if (!passwordBean.verify())
		{
//			facesMessages.addToControl("confirm", "value does not match password");
			return null;
		}
		
		String username = user.getUsername();
		if ( !isUsernameAvailable(username) )
		{
//			facesMessages.addToControl("username", "Username is already taken");
			return null;
		}
		
		if ( !isEmailAvailable(user.getEmail()) )
		{
//			facesMessages.addToControl("email", "Email already exists");
			return null;
		}
		
		String password = passwordBean.getPassword();
		user.setPassword( passwordManager.hash(password) );
		
		if ( needsAdmin() )
		{
			Role roleAdmin = new Role();
			roleAdmin.setName("admin");
			em.persist(roleAdmin);
			
			user.getRoles().add(roleAdmin);
			log.info("Role {0} is added to User {1}", roleAdmin.getName(), user.getUsername() );
			
			Role roleManager = new Role();
			roleManager.setName("manager");
			em.persist(roleManager);
			
			Role roleDeveloper = new Role();
			roleDeveloper.setName("developer");
			em.persist(roleDeveloper);
		}
		em.persist(user);
		
		identity.setUsername(username);
		identity.setPassword(password);
		identity.login();
		
		return "/home.seam";
	}
	
	public boolean needsAdmin()
	{
		return em.createQuery("FROM User") // SELECT u FROM User u
				.getResultList().isEmpty();
	}
	
	public boolean isUsernameAvailable(String username)
	{
		return em.createQuery("FROM User WHERE username=:username") // SELECT u FROM User u WHERE u.username=:username
				.setParameter("username", username)
				.getResultList().isEmpty(); // size() == 0
	}
	
	public boolean isEmailAvailable(String email)
	{
		return em.createQuery("FROM User WHERE email=:email")
				.setParameter("email", email)
				.getResultList().isEmpty();
	}
}