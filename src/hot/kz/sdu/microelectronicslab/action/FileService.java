package kz.sdu.microelectronicslab.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import kz.sdu.microelectronicslab.action.user.ProfileManager;

import org.apache.commons.io.FilenameUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("fileService")
@Scope(ScopeType.CONVERSATION)
public class FileService implements Serializable
{
	@In(create=true)
	protected ProfileManager profileManager;
	
	@In(create=true)
	protected ConfigurationBean configurationBean; 
	
	private byte[] data;
	private String filename;
	
	public byte[] getData()
	{
		return data;
	}
	
	public void setData(byte[] data)
	{
		this.data = data;
	}
	
	public String getFilename()
	{
		return filename;
	}
	
	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public void save() throws IOException
	{
//      Prepare filename prefix and suffix for an unique filename in upload folder.
        String prefix = FilenameUtils.getBaseName(filename);
        String suffix = FilenameUtils.getExtension(filename);
        
        File file = File.createTempFile(prefix + "_", "." + suffix, new File( configurationBean.getFileStorePath() + "/user/avatar" ));
		// new
		FileOutputStream out = new FileOutputStream( file );
		out.write( data, 0, data.length );
		out.close();
		
		// old
		String oldAvatarName = profileManager.changeAvatar( file.getName() );
		
		if ( !oldAvatarName.equals("unknown.png") ) // check whether it is not default
		{
			file = new File(configurationBean.getFileStorePath() + "/user/avatar/" + oldAvatarName);
			file.delete();
		}
	}
}