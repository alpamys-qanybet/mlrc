package kz.sdu.microelectronicslab.action.user;

public class RoleBean
{
	private long id;
	private String name;
	private boolean enabled;
	
	public RoleBean() {}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
}