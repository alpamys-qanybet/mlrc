package kz.sdu.microelectronicslab.action.article;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("articleService")
@Scope(ScopeType.CONVERSATION)
public class ArticleService
{
	private String galleryRight;
	private String galleryLeft;
	
	public String getGalleryRight() {
		return galleryRight;
	}
	public void setGalleryRight(String galleryRight) {
		this.galleryRight = galleryRight;
	}
	public String getGalleryLeft() {
		return galleryLeft;
	}
	public void setGalleryLeft(String galleryLeft) {
		this.galleryLeft = galleryLeft;
	}
}