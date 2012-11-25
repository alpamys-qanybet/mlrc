package kz.sdu.microelectronicslab.action.user;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("roleManagementBean")
@Scope(ScopeType.EVENT)
public class RoleManagementBean
{
	private long userId;
	private long roleId;
	
	public long getUserId()
	{
		return userId;
	}

	public void setUserId(long userId)
	{
		this.userId = userId;
	}

	public long getRoleId()
	{
		return roleId;
	}

	public void setRoleId(long roleId)
	{
		this.roleId = roleId;
	}
}