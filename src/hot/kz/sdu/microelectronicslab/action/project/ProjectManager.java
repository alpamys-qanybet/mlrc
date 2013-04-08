package kz.sdu.microelectronicslab.action.project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import kz.sdu.microelectronicslab.action.ConfigurationBean;
import kz.sdu.microelectronicslab.action.FileService;
import kz.sdu.microelectronicslab.action.OperationService;
import kz.sdu.microelectronicslab.model.article.Article;
import kz.sdu.microelectronicslab.model.project.Project;
import kz.sdu.microelectronicslab.model.project.ProjectStatus;
import kz.sdu.microelectronicslab.model.user.Role;
import kz.sdu.microelectronicslab.model.user.User;

import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

@Name("projectManager")
@Scope(ScopeType.PAGE)
public class ProjectManager implements Serializable
{
	@Logger Log log;
	
	@In protected Identity identity;
    
	@In("entityManager")
	private EntityManager em;
	
	@In(create=true)
	private ProjectManagementBean projectManagementBean;

	@Out(scope=ScopeType.PAGE,required=false)
	private List<ProjectStatus> projectStatuses;
	
	@Out(required=false)
	protected Project project;
	
	@Out(scope=ScopeType.PAGE,required=false)
	private List<User> managers;
	
	@In(create=true)
	protected ConfigurationBean configurationBean;
	
	@In(create=true)
	protected OperationService operationService;
	
	@In(create=true)
	private FileService fileService;
	
	private List<Project> projects;
	
	
	public void preparePage(String content)
	{
		if (content.equals("createProject"))
		{
			project = new Project();
			
			if (identity.hasRole("admin"))
				prepareManagers();
			
			log.info("preparePage createProject");
		}
		else if (content.equals("viewProject"))
		{
			HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
				
			long projectId = Long.parseLong( req.getParameter("projectId") );
			project = em.find(Project.class, projectId);
		}
		else if (content.equals("editProject"))
		{
			projectManagementBean.setManagerId( project.getManager().getId() );
			projectManagementBean.setStatusId( project.getStatus().getId() );
			
			projectStatuses = em.createQuery("FROM ProjectStatus").getResultList();
			
			if (identity.hasRole("admin"))
				prepareManagers();
		}
	}
	
	public boolean doesManagerBelongsToProject(long projectId)
	{
		return em.find(Project.class, projectId).getManager().getUsername().equals(identity.getUsername());
	}
	
	public boolean doesDeveloperBelongsToProject(long userId)
	{
		for (User user: project.getDevelopers())
			if (user.getId() == userId)
				return true;
		
		return false;
	}
	
	private void prepareManagers()
	{
		List<User> users = em.createQuery("FROM User").getResultList();
		managers = new ArrayList<User>();
		
		for (User user: users)
			if ( hasRole(user, "manager") )
				managers.add(user);
		
		log.info("prepareManagers");
		log.info("size {0}", managers.size() );
	}
	
	private boolean hasRole(User user, String rolename)
	{
		for (Role userRole: user.getRoles())
			if ( userRole.getName().equals(rolename) )
				return true;
		
		return false;
	}
	
	
	public List<User> retrieveDevelopers(boolean belongs) {
		List<User> users = em.createQuery("FROM User").getResultList();
		List<User> list = new ArrayList<User>();
		
		for (User user: users)
			if ( hasRole(user, "developer") )
				if ( !( belongs ^ doesDeveloperBelongsToProject(user.getId()) ) )
					list.add(user);
		
		return list;
	}
	
	
	public void addDeveloper()
	{
		project.getDevelopers().add( em.find(User.class, projectManagementBean.getDeveloperId()) );
		em.merge(project);
	}
	
	public void removeDeveloper(long userId)
	{
		log.info(" remove dev {0} ", userId);
		
		project = em.find(Project.class, project.getId());
		project.getDevelopers().remove( em.find(User.class, userId) );
		em.persist(project);
	}
	
	public void create()
	{
		log.info("create");
		project.setStatus( (ProjectStatus)em.createQuery("FROM ProjectStatus WHERE name = :name").setParameter("name", "seed").getSingleResult() );
		project.setManager( em.find(User.class, projectManagementBean.getManagerId()) );
		
		String url = configurationBean.getFileServerHost() + "/project/icon/default.png";
		project.setIcon(url);
		
		log.info("status {0}, manager {1}, icon {2}", project.getStatus(), project.getManager().getRealname(), project.getIcon());
		em.persist(project);
	}
	
	public void edit()
	{
		String name = project.getName();
		String description = project.getDescription();
		
		project = em.find(Project.class, project.getId());
		
		project.setName(name);
		project.setDescription(description);
		project.setStatus( em.find(ProjectStatus.class, projectManagementBean.getStatusId()) );
		
		if (identity.hasRole("admin"))
			project.setManager( em.find(User.class, projectManagementBean.getManagerId()) );

		if (fileService.getData() != null)
		{
			String url = configurationBean.getFileServerHost() + "/project/icon/" + fileService.saveProjectIcon();
			project.setIcon(url);
		}
		
//		em.persist(project);
		em.merge(project);
		
		operationService.redirectToSystemPage("/project/view.seam?projectId=" + project.getId());
	}
	
	public void delete()
	{
		project = em.find(Project.class, project.getId());
		em.remove(project);
		
		operationService.redirectToSystemPage("/project/home.seam");
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Project> getProjects() {
		if (projects != null)
			return projects;
		else
		{
			projects = (List<Project>) em.createQuery("select p " +
													  "from Project p ")
													  .getResultList();
						
			return projects;
		}
	}
}