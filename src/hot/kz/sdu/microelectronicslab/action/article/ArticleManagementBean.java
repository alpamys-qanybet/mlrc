package kz.sdu.microelectronicslab.action.article;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("articleManagementBean")
@Scope(ScopeType.EVENT)
public class ArticleManagementBean
{
	private long authorId;

	public long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(long authorId) {
		this.authorId = authorId;
	}
}