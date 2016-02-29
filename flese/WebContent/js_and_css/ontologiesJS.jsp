/*! * auxiliarJS Library v1 * auxiliar javascript code * Author: Victor
Pablos Ceruelo */ /*
----------------------------------------------------------------------------------------------------------------
*/ /*
----------------------------------------------------------------------------------------------------------------
*/ /*
----------------------------------------------------------------------------------------------------------------
*/

<%@page import="constants.KUrls"%>
<%@page import="constants.KConstants"%>
<%@page import="auxiliar.JspsUtils"%>

<% if (JspsUtils.getStringWithValueS().equals("N")) { %>
<script type="text/javascript">
<% } %>

/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */

function sendOntologyMainQuery (fieldId, url, divId) {
	var fieldValue = getFieldValue(fieldId);
	if (fieldValue != "") {
		var params = "&<%=KConstants.Request.serviceEndPoint %>=" + fieldValue;
		loadAjaxIn(divId, url + params);
	}
	else {
		alert("Please introduce a valid ontology url");
	}
	return false;
}

function ontologyQuery(url, serviceEndPoint, rdfNodeUri, divId) {
	var fullUrl = url;
	
	if ((rdfNodeUri != null) && (rdfNodeUri != "")) {
		fullUrl += "&<%=KConstants.Request.serviceEndPoint %>=" + serviceEndPoint;
		fullUrl += "&<%=KConstants.Request.url %>=" + rdfNodeUri;
		fullUrl += "&<%=KConstants.Request.divIdPrefix %>=" + divId;
		
		return loadAjaxIn(divId, fullUrl);
	}
	else {
		var container = getContainer(divId);
		container.innerHTML="Operation not available";
	}
	return false;
}

/* ----------------------------------------------------------------------------------------------------------------------------*/
/* ----------------------------------------------------------------------------------------------------------------------------*/
/* ----------------------------------------------------------------------------------------------------------------------------*/


<% if (JspsUtils.getStringWithValueS().equals("N")) { %>
</script>
<% } %>