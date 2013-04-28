package kz.sdu.microelectronicslab.action.group;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("groupManagementBean")
@Scope(ScopeType.EVENT)
public class GroupManagementBean
{
	private long participantId;
	private long webSiteId;
	private long themeId;

	public long getParticipantId()
	{
		return participantId;
	}

	public void setParticipantId(long participantId)
	{
		this.participantId = participantId;
	}

	public long getWebSiteId()
	{
		return webSiteId;
	}

	public void setWebSiteId(long webSiteId)
	{
		this.webSiteId = webSiteId;
	}

	public long getThemeId()
	{
		return themeId;
	}

	public void setThemeId(long themeId)
	{
		this.themeId = themeId;
	}
}