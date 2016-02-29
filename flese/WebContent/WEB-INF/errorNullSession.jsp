
<%@page import="auxiliar.JspsUtils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="constants.KConstants"%>

<%
ArrayList<String> msgs = new ArrayList<String>();
msgs.add(KConstants.AppMsgs.errorSessionNull1);
String msgsArray = JspsUtils.getMessagesInJS(msgs);
//tring ss = KConstants.Request.modeAdvanced;
String mes1 = KConstants.AppMsgs.errorSessionNull1;
%>

<script type="text/javascript">
popupExpire();
function popupExpire() {
    alert("<%=mes1%>");
    history.go(-1);
}
if (typeof(clearMainSection) == "function") {
	clearMainSection();
}
if (typeof(window.parent.clearMainSection) == "function") {
	window.parent.clearMainSection();
}
if (typeof(clearMsgsSection) == "function") {
	clearMsgsSection();
}
if (typeof(window.parent.clearMsgsSection) == "function") {
	window.parent.clearMsgsSection();
}
if (typeof(showMsgsArray) == "function") {
	showMsgsArray(<%= msgsArray %>);
}
if (typeof(window.parent.showMsgsArray) == "function") {
	window.parent.showMsgsArray(<%= msgsArray %>);
}
</script>