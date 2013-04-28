package kz.sdu.microelectronicslab.action.group;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("groupManagementBean")
@Scope(ScopeType.EVENT)
public class GroupManagementBean
{
	private long participantId;
	private String webSiteUrl;

	public long getParticipantId()
	{
		return participantId;
	}

	public void setParticipantId(long participantId)
	{
		this.participantId = participantId;
	}

	public String getWebSiteUrl()
	{
		return webSiteUrl;
	}

	public void setWebSiteUrl(String webSiteUrl)
	{
		this.webSiteUrl = webSiteUrl;
	}
}