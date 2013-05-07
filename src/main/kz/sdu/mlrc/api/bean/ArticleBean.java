package kz.sdu.mlrc.api.bean;

public class ArticleBean
{
	private Long id;
	private String short_text;
	private String full_text;
	private String icon_host;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getShort_text() {
		return short_text;
	}

	public void setShort_text(String short_text) {
		this.short_text = short_text;
	}
	
	public String getFull_text() {
		return full_text;
	}
	
	public void setFull_text(String full_text) {
		this.full_text = full_text;
	}

	public String getIcon_host() {
		return icon_host;
	}

	public void setIcon_host(String icon_host) {
		this.icon_host = icon_host;
	}
}