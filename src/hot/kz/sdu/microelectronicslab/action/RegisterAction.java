package kz.sdu.microelectronicslab.action;

import javax.persistence.EntityManager;

import kz.sdu.microelectronicslab.model.user.PasswordBean;
import kz.sdu.microelectronicslab.model.user.PasswordManager;
import kz.sdu.microelectronicslab.model.user.User;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

@Name("register")
public class RegisterAction
{
/*	@In
	private Member member;
	
	@In("entityManager")
	private EntityManager em;
	
	public String register()
	{
		Query query = em.createQuery("SELECT username FROM User WHERE username = :username")
						.setParameter("username", member.getUsername());
		
		List existing = query.getResultList();
		if (existing.size() == 0)
		{
			em.persist(member);
			
			return "/registration/registered.xhtml";
		}
		else
		{
			FacesMessages.instance().add("User " + member.getUsername() + " already exists");
			return null;
		}
	}
*/
	@Logger private Log log;
	
	@In("entityManager")
	protected EntityManager em;
	@In protected FacesMessages facesMessages;
	@In(create=true) 
	protected PasswordManager passwordManager;
	@In protected User user;
	@In protected PasswordBean passwordBean;
	
	public String register()
	{
		if (!passwordBean.verify())
		{
			facesMessages.addToControl("confirm", "value does not match password");
			return "failed";
		}
		
		String username = user.getUsername();
		if ( !isUsernameAvailable(username) )
		{
			facesMessages.addToControl("username", "Username is already taken");
		}
		
		user.setPassword( passwordManager.hash(passwordBean.getPassword()) );
		em.persist(user);
		facesMessages.add("Welcome, #{user.username}");
		return "success";
	}
	
	public boolean isUsernameAvailable(String username)
	{
		return em.createQuery("SELECT u FROM User u WHERE u.username=:username")
				.setParameter("username", username)
				.getResultList().size() == 0;
	}
}