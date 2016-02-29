package auxiliar;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import servlets.Servlet;
import urls.UrlMap;
import constants.KConstants;
import constants.KUrls;

public class NextStep {

	final Log LOG = LogFactory.getLog(Servlet.class);

	private int action = 0;
	private UrlMap urlMap = null;
	private String appended = "";

	public NextStep(int action, UrlMap urlMap, String append) {
		if ((action <= KConstants.NextStep.none) || (action >= KConstants.NextStep.invalidAction))
			action = KConstants.NextStep.forward_to;
		if ((urlMap == null) && ((append == null) || ("".equals(append)))) {
			// If we do not know where to go, exception page.
			urlMap = KUrls.Pages.Exception;
		}

		this.urlMap = urlMap;
		this.action = action;
		if (append != null)
			this.appended = append;
	}

	public void addToAppend(String msg) {
		if ((msg != null) && (!"".equals(msg))) {
			StringBuilder appendedTmp = new StringBuilder();
			appendedTmp.append(this.appended);
			appendedTmp.append(msg);
			this.appended = appendedTmp.toString();
		}
	}

	public void takeAction(HttpServletRequest request, HttpServletResponse response) throws NextStepException, IOException,
			ServletException {

		String isAjaxParam = request.getParameter(KConstants.Request.isAjaxParam);
		boolean isAjax = ((isAjaxParam != null) && (KConstants.Values.True.equals(isAjaxParam)));

		String url = null;

		switch (this.action) {
		case KConstants.NextStep.forward_to:
			url = getUrl(false, false, isAjax, request);
			LOG.info("forward_to: " + url);
			RequestDispatcher dispatcher = request.getRequestDispatcher(url);
			dispatcher.forward(request, response);
			break;
		case KConstants.NextStep.redirect_to:
			url = getUrl(true, true, isAjax, request);
			LOG.info("redirect_to: " + url);
			response.sendRedirect(url);
			break;
		case KConstants.NextStep.redirect_to_with_session:
			url = getUrl(true, true, isAjax, request);
			LOG.info("redirect_to_with_session: " + url);
			response.encodeRedirectURL(url);
			break;
		case KConstants.NextStep.none:
		default:
			LOG.info("No action to take. ");
		}
	}

	public String getUrl(boolean withServerPath, boolean withAppPath, boolean isAjax, HttpServletRequest request) {
		if (urlMap == null) {
			return appended;
		}

		return urlMap.getUrl(withServerPath, withAppPath, isAjax, request) + appended;
	}
	
	public String getLoggingInformation(boolean isAjax) {
		return getUrl(false, false, isAjax, null);
	}
}
