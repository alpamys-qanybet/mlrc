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

import javax.persistence.EntityManager;

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
	
	@Out(scope=ScopeType.PAGE,required=false)
	private List<User> developers;
	
	public void preparePage(String content)
	{
		if (content.equals("projectList"))
			projectStatuses = em.createQuery("FROM ProjectStatus").getResultList();
		
		else if (content.equals("createProject"))
		{
			project = new Project();
			
			if (identity.hasRole("admin"))
				prepareManagers();
		}
		else if (content.equals("editProject"))
		{
			project = em.find(Project.class, projectManagementBean.getProjectId());
			projectManagementBean.setManagerId( project.getManager().getId() );
			projectManagementBean.setStatusId( project.getStatus().getId() );
		
			if (identity.hasRole("admin"))
				prepareManagers();
			
			if (identity.hasRole("manager"))
				prepareDevelopers();
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
	}
	
	private void prepareDevelopers()
	{
		List<User> users = em.createQuery("FROM User").getResultList();
		developers = new ArrayList<User>();
		
		for (User user: users)
			if ( hasRole(user, "developer") )
				developers.add(user);
	}
	
	private boolean hasRole(User user, String rolename)
	{
		for (Role userRole: user.getRoles())
			if ( userRole.getName().equals(rolename) )
				return true;
		
		return false;
	}
	
	public void addDeveloper(long userId)
	{
		project.getDevelopers().add( em.find(User.class, userId) );
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
		project.setStatus( (ProjectStatus)em.createQuery("FROM ProjectStatus WHERE name = :name").setParameter("name", "seed").getSingleResult() );
		project.setManager( em.find(User.class, projectManagementBean.getManagerId()) );
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
		
//		em.persist(project);
		em.merge(project);
	}
	
	public void delete()
	{
		project = em.find(Project.class, project.getId());
		em.remove(project);
	}
}