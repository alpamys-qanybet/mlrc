package kz.sdu.microelectronicslab.action;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import kz.sdu.microelectronicslab.model.Member;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;

@Name("register")
public class RegisterAction
{
	@In
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
}