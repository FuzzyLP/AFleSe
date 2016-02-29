<%@page import="constants.KConstants"%>
<%@page import="constants.KUrls"%>
<%
	String urlUpload = KUrls.Files.Upload.getUrl(true);
%>


Upload Program files
<br />
<FORM ID='<%=KConstants.JspsDivsIds.uploadFormId %>'
	ENCTYPE='multipart/form-data' method='POST' accept-charset='UTF-8'
	target='<%=KConstants.JspsDivsIds.uploadFormTargetiFrameId %>'
	action='<%=urlUpload %>'>
	<INPUT TYPE='file' NAME='programFileToUpload' size='50'
		onchange='fileUploadAutomaticSendActionOnChange("<%=KConstants.JspsDivsIds.uploadFormId %>", "<%=KConstants.JspsDivsIds.uploadStatusDivId%>");'>
</FORM>

<div id='<%=KConstants.JspsDivsIds.uploadStatusDivId%>'></div>
<iframe id='<%=KConstants.JspsDivsIds.uploadFormTargetiFrameId %>'
	name='<%=KConstants.JspsDivsIds.uploadFormTargetiFrameId%>'
	style='display: none;'></iframe>
<!-- style='display:none;' -->

<script type="text/javascript">
	insertiFrameWindowEvaluationOfJS('<%=KConstants.JspsDivsIds.uploadFormTargetiFrameId %>');
</script>




<!-- END -->