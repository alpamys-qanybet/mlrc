package kz.sdu.microelectronicslab.action;

import javax.persistence.EntityManager;

import kz.sdu.microelectronicslab.action.user.PasswordBean;
import kz.sdu.microelectronicslab.action.user.PasswordManager;
import kz.sdu.microelectronicslab.model.user.User;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.security.Identity;

@Name("register")
public class RegisterAction
{
	@In("entityManager")
	protected EntityManager em;
	@In protected FacesMessages facesMessages;
	@In(create=true) 
	protected PasswordManager passwordManager;
	
	@In protected User user;
	@In protected PasswordBean passwordBean;
	
	@In protected Identity identity;
    
	public String register()
	{
		if (!passwordBean.verify())
		{
			facesMessages.addToControl("confirm", "value does not match password");
			return null;
		}
		
		String username = user.getUsername();
		if ( !isUsernameAvailable(username) )
		{
			facesMessages.addToControl("username", "Username is already taken");
			return null;
		}
		
		if ( !isEmailAvailable(user.getEmail()) )
		{
			facesMessages.addToControl("email", "Email already exists");
			return null;
		}
		
		String password = passwordBean.getPassword();
		user.setPassword( passwordManager.hash(password) );
		em.persist(user);
		
		identity.setUsername(username);
		identity.setPassword(password);
		identity.login();
		
		return "/home.seam";
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