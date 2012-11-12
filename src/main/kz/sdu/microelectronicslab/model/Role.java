package kz.sdu.microelectronicslab.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.jboss.seam.annotations.security.management.RoleConditional;
import org.jboss.seam.annotations.security.management.RoleName;

@Entity
@Table(name="ROLE", uniqueConstraints=@UniqueConstraint(columnNames="name"))
public class Role implements Serializable
{
	private long id;
	private String name;
	private boolean conditional;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	@RoleName
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	@RoleConditional
	public boolean isConditional()
	{
		return conditional;
	}

	public void setConditional(boolean conditional)
	{
		this.conditional = conditional;
	}
}