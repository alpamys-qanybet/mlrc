package kz.sdu.microelectronicslab.action;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("configurationBean")
@Scope(ScopeType.APPLICATION)
public class ConfigurationBean
{
	private final String company = "sdu/mlrc";
	private final String fileServerHost = "http://localhost/" + company;
	private final String fileStorePath = "/var/www/" + company;
	
	public String getFileServerHost()
	{
		return fileServerHost;
	}

	public String getFileStorePath()
	{
		return fileStorePath;
	}
}