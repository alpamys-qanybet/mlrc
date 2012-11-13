package kz.sdu.microelectronicslab.action;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import kz.sdu.microelectronicslab.model.user.PasswordManager;
import kz.sdu.microelectronicslab.model.user.Role;
import kz.sdu.microelectronicslab.model.user.User;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;

@Name("authenticator")
public class Authenticator
{
    @Logger private Log log;

    @In protected Identity identity;
    
    @In protected Credentials credentials;
    
    @In("entityManager")
    protected EntityManager em;
    
    @In(create=true) 
	protected PasswordManager passwordManager;
	
    public boolean authenticate()
    {
    	try
    	{
	        log.info("authenticating {0}", credentials.getUsername());
	        //write your authentication logic here,
	        //return true if the authentication was
	        //successful, false otherwise
/*	        
	        if ("admin".equals(credentials.getUsername()))
	        {
	            identity.addRole("admin");
	            return true;
	        }
	        return false;
*/
	        User user = (User) em.createQuery("FROM User WHERE username=:username AND password=:password")
	        			.setParameter("username", credentials.getUsername())
	        			.setParameter("password", passwordManager.hash(credentials.getPassword()) )
	        			.getSingleResult();
	        
	        if (user.getRoles() != null)
	        {
	        	for (Role mr: user.getRoles())
	        		identity.addRole(mr.getName());
	        }
	        
	        return true;
    	}
    	catch (NoResultException ex)
    	{ return false; }
    }

}
