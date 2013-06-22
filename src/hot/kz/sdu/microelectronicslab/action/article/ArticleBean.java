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
	private String searchText;
	private String			title;
	private String			author;
	private String			keywords;
	private String dateFrom, dateTo;
	

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

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}
}