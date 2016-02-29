<%@page import="storeHouse.RequestStoreHouse"%>
<%@page import="java.util.ArrayList"%>
<%@page import="auxiliar.JspsUtils"%>
<%@page import="constants.KUrls"%>

<%
	RequestStoreHouse requestStoreHouse = JspsUtils.getRequestStoreHouse(request);
%>

<%	if (! JspsUtils.isAjax(requestStoreHouse)) { %>
<jsp:include page='../commonHtmlBody.jsp' />
<% } %>

<h3>
	To access to FleSe you need an account in a Social Authentication
	Provider. <br /> Please choose one of the following or provide an
	OpenId url.
</h3>

<table class="providersIcons">
	<tr class="providersIcons">
		<td class="providersIcons"><a id="facebookLink"
			href="<%= KUrls.Auth.SignIn.getUrl(false) + "&id=facebook" %>"> <img
				src="images/facebook_icon.png" alt="Facebook" title="Facebook"
				border="0" width="50"></img>
		</a></td>
		<td class="providersIcons"><a id="twitterLink"
			href="<%= KUrls.Auth.SignIn.getUrl(false) + "&id=twitter" %>"> <img
				src="images/twitter_icon.png" alt="Twitter" title="Twitter"
				border="0" width="50"></img></a></td>
		<td class="providersIcons"><a id="gmailLink"
			href="<%= KUrls.Auth.SignIn.getUrl(false)  + "&id=google" %>"> <img
				src="images/gmail_icon.png" alt="Gmail" title="Gmail" border="0"
				width="50"></img></a></td>
	</tr>
	<tr class="providersIcons">
		<td class="providersIcons"><a id="yahooLink"
			href="<%= KUrls.Auth.SignIn.getUrl(false) + "&id=yahoo" %>"> <img
				src="images/yahoomail_icon.png" alt="YahooMail" title="YahooMail"
				border="0" width="50"></img></a></td>
		<td class="providersIcons"><a id="hotmailLink"
			href="<%= KUrls.Auth.SignIn.getUrl(false) + "&id=hotmail" %>"> <img
				src="images/hotmail_icon.jpeg" alt="HotMail" title="HotMail"
				border="0" width="50"></img></a></td>
		<td class="providersIcons"><a id="linkedinLink"
			href="<%= KUrls.Auth.SignIn.getUrl(false) + "&id=linkedin" %>"> <img
				src="images/linkedin_icon.gif" alt="Linked In" title="Linked In"
				border="0" width="50"></img></a></td>
	</tr>
	<tr class="providersIcons">
		<td class="providersIcons"><a id="foursquareLink"
			href="<%= KUrls.Auth.SignIn.getUrl(false) + "&id=foursquare" %>">
				<img src="images/foursquare_icon.jpeg" alt="FourSquare"
				title="FourSquare" border="0" width="50"></img>
		</a></td>
		<td class="providersIcons"><a id="myspaceLink"
			href="<%= KUrls.Auth.SignIn.getUrl(false) + "&id=myspace" %>"> <img
				src="images/myspace_icon.jpeg" alt="MySpace" title="MySpace"
				border="0" width="50"></img></a></td>
		<td class="providersIcons"><a id="mendeleyLink"
			href="<%= KUrls.Auth.SignIn.getUrl(false) + "&id=mendeley" %>"> <img
				src="images/mendeley_icon.jpg" alt="Mendeley" title="Mendeley"
				border="0" width="50"></img>
		</a></td>
	</tr>
	<!--  Yammer is a Private Social Network for Your Company					
	<a href="SocialAuthServlet?id=yammer&op=signin"><img
		src="images/yammer.jpg" alt="Yammer" title="Yammer" border="0"></img></a>
	<br /> <br /> 
	 -->
</table>

<br />
<h3>or enter OpenID url:</h3>
<form action="SocialAuthServlet" onsubmit="return validate(this);">
	<input type="text" value="" name="id" /> <input type="submit"
		value="Submit" />
</form>
<br />
<br />
<br />
<br />
<br />

