package kz.sdu.mlrc.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.annotations.In;

import com.google.gson.Gson;

import kz.sdu.mlrc.api.bean.ArticleBean;
import kz.sdu.mlrc.api.bean.NewsBean;
import kz.sdu.mlrc.api.bean.WebSiteBean;
import kz.sdu.mlrc.api.logic.ArticleLogic;

public class CmsaasServlet extends HttpServlet
{	
	public CmsaasServlet()
    {
    	super();
    }

	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException
	{
		res.setHeader("Access-Control-Allow-Origin", "*");
		res.setContentType("application/json");
		res.setCharacterEncoding("UTF-8");
		
		String content = req.getParameter("content");
		
		if (content.equals("news"))
		{
			Long id = Long.parseLong( req.getParameter("id") );

			WebSiteBean webSiteBean = null;

			try
			{
				ArticleLogic articleLogic = new ArticleLogic();
			
				List<ArticleBean>  articleBeanList = articleLogic.loadArticlesByWebSiteId( id );
				
				webSiteBean = articleLogic.getWebSiteById( id );
			
				NewsBean newsBean = new NewsBean();
				newsBean.setArticles( articleBeanList );
				newsBean.setWeb_site( webSiteBean );
				
				Gson gson = new Gson();
				String json = gson.toJson( newsBean );
				
				PrintWriter writer = new PrintWriter( res.getWriter() );
				writer.write(json);
				System.out.println(json);
				writer.close();
				return;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (content.equals("article"))
		{
			try {
				Long id = Long.parseLong( req.getParameter("id") );
				
				ArticleLogic articleLogic = new ArticleLogic();
				ArticleBean articleBean = articleLogic.loadArticleById( id );
		
				Gson gson = new Gson();
				String json = gson.toJson( articleBean );
				
				PrintWriter writer = new PrintWriter( res.getWriter() );
				writer.write(json);
				writer.close();
				return;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}