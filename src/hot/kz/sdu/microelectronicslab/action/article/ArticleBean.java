package kz.sdu.microelectronicslab.action.article;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("articleBean")
@Scope(ScopeType.EVENT)
public class ArticleBean
{
	private long articleId;
	private boolean isRequestFromModal;

	public long getArticleId() {
		return articleId;
	}

	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}

	public boolean isRequestFromModal() {
		return isRequestFromModal;
	}

	public void setRequestFromModal(boolean isRequestFromModal) {
		this.isRequestFromModal = isRequestFromModal;
	}
}