<%@page import="authProviders.AuthProviderInterface"%>
<%@page import="authProviders.OpenIdAuthProvider"%>
<%@page import="storeHouse.RequestStoreHouse"%>
<%@page import="constants.KUrls"%>
<%@page import="constants.KConstants"%>
<%@page import="storeHouse.SessionStoreHouse"%>
<%@page import="auxiliar.JspsUtils"%>
<%@page import="org.brickred.socialauth.Profile"%>
<%@page import="org.brickred.socialauth.Contact"%>
<%@page import="org.brickred.socialauth.AuthProvider"%>


<div id='userInformationDiv' class='userInformationTable'>
	<div class='userInformationTableRow'>
		<div class='userInformationTableCell'>Field Name</div>
		<div class='userInformationTableCell'>Value</div>
	</div>
	<%
	RequestStoreHouse requestStoreHouse = JspsUtils.getRequestStoreHouse(request);
	SessionStoreHouse sessionStoreHouse = JspsUtils.getSessionStoreHouse(requestStoreHouse);
	AuthProviderInterface provider = sessionStoreHouse.getAuthProvider();
	Profile profile = null;
	if ((provider != null) && (provider instanceof OpenIdAuthProvider)) {
		profile = ((OpenIdAuthProvider) provider).getUserProfile();
	}
	
	if (profile == null) { %>
	<div class='userInformationTableRow'>
		<div class='userInformationTableCell'>DisplayName</div>
		<div class='userInformationTableCell'><%= sessionStoreHouse.getLocalUserInfo().getLocalUserName() %></div>
	</div>

	<%	} else { %>
	<div class='userInformationTableRow'>
		<div class='userInformationTableCell'>DisplayName</div>
		<div class='userInformationTableCell'><%= profile.getDisplayName() %></div>
	</div>
	<div class='userInformationTableRow'>
		<div class='userInformationTableCell'>Email</div>
		<div class='userInformationTableCell'><%= profile.getEmail() %></div>
	</div>
	<div class='userInformationTableRow'>
		<div class='userInformationTableCell'>FirstName</div>
		<div class='userInformationTableCell'><%= profile.getFirstName() %></div>
	</div>
	<div class='userInformationTableRow'>
		<div class='userInformationTableCell'>LastName</div>
		<div class='userInformationTableCell'><%= profile.getLastName() %></div>
	</div>
	<div class='userInformationTableRow'>
		<div class='userInformationTableCell'>FullName</div>
		<div class='userInformationTableCell'><%= profile.getFullName() %></div>
	</div>
	<div class='userInformationTableRow'>
		<div class='userInformationTableCell'>Language</div>
		<div class='userInformationTableCell'><%= profile.getLanguage() %></div>
	</div>
	<div class='userInformationTableRow'>
		<div class='userInformationTableCell'>Country</div>
		<div class='userInformationTableCell'><%= profile.getCountry() %></div>
	</div>
	<div class='userInformationTableRow'>
		<div class='userInformationTableCell'>Location</div>
		<div class='userInformationTableCell'><%= profile.getLocation() %></div>
	</div>
	<div class='userInformationTableRow'>
		<div class='userInformationTableCell'>Gender</div>
		<div class='userInformationTableCell'><%= profile.getGender() %></div>
	</div>
	<div class='userInformationTableRow'>
		<div class='userInformationTableCell'>ProfileImageUrl</div>
		<div class='userInformationTableCell'><%= profile.getProfileImageURL() %></div>
	</div>
	<div class='userInformationTableRow'>
		<div class='userInformationTableCell'>ProviderId</div>
		<div class='userInformationTableCell'><%= profile.getProviderId() %></div>
	</div>
	<div class='userInformationTableRow'>
		<div class='userInformationTableCell'>ValidatedId</div>
		<div class='userInformationTableCell'><%= profile.getValidatedId() %></div>
	</div>
	<div class='userInformationTableRow'>
		<div class='userInformationTableCell'>Date Of BirthDay</div>
		<div class='userInformationTableCell'><%= profile.getDob() %></div>
	</div>
	<% } %>
</div>

<div id='<%=KConstants.JspsDivsIds.filesListDiv %>'
	class='filesListTable'></div>

<div id='<%=KConstants.JspsDivsIds.fileUploadDiv %>'
	class='filesListTable'></div>

<div id='<%=KConstants.JspsDivsIds.ontologyStartDivId %>'
	class='filesListTable'></div>

<script type="text/javascript">
	loadAjaxIn('<%=KConstants.JspsDivsIds.filesListDiv %>', '<%=KUrls.Files.ListMyFiles.getUrl(true) %>');
	loadAjaxIn('<%=KConstants.JspsDivsIds.fileUploadDiv %>', '<%=KUrls.Files.UploadDiv.getUrl(true) %>');
	loadAjaxIn('<%=KConstants.JspsDivsIds.ontologyStartDivId %>', '<%=KUrls.Ontologies.Start.getUrl(true) %>');
</script>



<!-- END -->
