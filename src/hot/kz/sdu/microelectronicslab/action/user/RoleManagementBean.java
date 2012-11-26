package kz.sdu.microelectronicslab.action.user;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("roleManagementBean")
@Scope(ScopeType.EVENT)
public class RoleManagementBean
{
	private long userId;
	private boolean isRequestFromModal;
	
	public long getUserId()
	{
		return userId;
	}

	public void setUserId(long userId)
	{
		this.userId = userId;
	}

	public boolean isRequestFromModal() {
		return isRequestFromModal;
	}

	public void setRequestFromModal(boolean isRequestFromModal) {
		this.isRequestFromModal = isRequestFromModal;
	}
}