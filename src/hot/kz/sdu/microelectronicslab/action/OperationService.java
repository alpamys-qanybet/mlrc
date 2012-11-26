package kz.sdu.microelectronicslab.action;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("operationService")
@Scope(ScopeType.APPLICATION)
public class OperationService
{
	HttpServletRequest req;
	HttpServletResponse res;
	
	
	public void redirectToSystemPage(String page)
	{
		try
		{
			req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			res = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
			res.sendRedirect( req.getContextPath() + "/" + page );
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void redirectToPage(String page)
	{
		try
		{
			req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			res = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
			res.sendRedirect( page );
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}