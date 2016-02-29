/*! * auxiliarJS Library v1 * auxiliar javascript code * Author: Victor
Pablos Ceruelo */ /*
----------------------------------------------------------------------------------------------------------------
*/ /*
----------------------------------------------------------------------------------------------------------------
*/ /*
----------------------------------------------------------------------------------------------------------------
*/

<%@page import="constants.KConstants"%>
<%@page import="constants.KUrls"%>
<%@page import="auxiliar.JspsUtils"%>

<%if (JspsUtils.getStringWithValueS().equals("N")) { %>
<script type="text/javascript">
<% } %>

function updateFuzzification(fuzzificationSaveStatusDivId, saveUrl) {
	var fuzzificationSaveStatusDiv = getContainer(fuzzificationSaveStatusDivId);
	loadAjaxIn(fuzzificationSaveStatusDivId, saveUrl);
}

function loadingImageHtml(withNewLine) {
	var result = "<img src=\"images/loading.gif\" width=\"200\" alt=\"loading\" title=\"loading\" />";
	
	if (withNewLine) return "<br />" + result;
	else return result;
}

function getContainer(containerId) {
	var container = null;
	if (containerId === null) {
		debug.info("getContainer: containerId is null");
	}
	else {
		if (!isString(containerId)) {
			debug.info("getContainer: containerId is not a string.");
		}
		else {
			container = document.getElementById(containerId);
			if (container === null) {
				debug.info("getContainer: container is null");
				debug.info("getContainer: containerId is " + containerId);
			}
			if (container == undefined) {
				debug.info("getContainer: container is undefined");
				debug.info("getContainer: containerId is " + containerId);				
			}
		}
	}
	return container;
}

function writeHtmlInContainer(containerId, html, reset) {
	var container = getContainer(containerId);
	if (reset) {
		container.innerHTML = "";
	}
	container.innerHTML += html;
}

function debugInfoIfVarIsNull(varValue, varName, preMsg) {
	if (varValue === null) {
		debug.info(preMsg + ": " + varName + " is null.");
	}
}

function executeAjaxLoadedPageJS(loadedContent) {
	// From http://stackoverflow.com/questions/10888326/executing-javascript-script-after-ajax-loaded-a-page-doesnt-work
	var content = loadedContent;
	// xmlhttp.responseText;
	
	var scriptStart = '<%= KConstants.JavaScriptScripts.jsStart %>';
	var scriptEnd = '<%= KConstants.JavaScriptScripts.jsEnd %>';

    var script = content.match("<%=KConstants.JavaScriptScripts.jsRegex %>");
    if (script != null) {
    	var scriptString = script.toString();
    	scriptString = scriptString.replace(scriptStart, '');
    	scriptString = scriptString.replace(scriptEnd, '');
    	// scriptString = scriptString.replace('\n', '');
    	var notExecutable = scriptHasProblematicParts(scriptString);
    	if (notExecutable) {
    		debug.warn(scriptString);
    	}
    	else {
			eval(scriptString);
    	}
	}
}

function scriptHasProblematicParts(scriptString) {
	var problematic = false;
	if ((scriptString != null) && (scriptString != null) && (typeof(scriptString) == "string")) {
		problematic = (scriptString.indexOf("script type=") != -1);
		// Not working in google chrome.
		// problematic = (scriptString.contains(""));
	}
	
	return problematic; 
}

function loadAjaxIn(containerId, ajaxPageUrl) {
	// debug.info("loadAjaxIn("+containerId + ", " + ajaxPageUrl + ")");
	var container = getContainer(containerId);
	if (container === null) {
		debug.info("aborted loadAjaxIn");
		debug.info("containerId: " + containerId);
		debug.info("ajaxPageUrl: " + ajaxPageUrl);
		debug.info("aborted loadAjaxIn (end)");
		return;
	}

	// Clear the msgs section. WHY ?????? 
	// clearMsgsSection();
	
	container.innerHTML=loadingImageHtml(true);
    $.ajax({
        method: 'get',
        url: ajaxPageUrl,
        data: 'page=' + $(this).attr('rel'),
        beforeSend: function() {
            container.innerHTML=loadingImageHtml(true);
        },
        complete: function() {
        	debug.info("load of html is complete.");
        	// container.innerHTML=loadingImageHtml(true);
        },
        success: function(html) {
        	console.log("loading html: " + html);
        	container.innerHTML=html;
        	executeAjaxLoadedPageJS(html);
        	//alert("success" + html);
		},
		fail: function() { 
			alert("error: Impossible to load page " + ajaxPageUrl); 
		}
	});
    
    // Do not let the navigator to follow links !!!
	return false;
}

function loadAjaxInDialog(containerId, ajaxPageUrl, title, width, height) {
	var container = getContainer(containerId);
	if (container === null) {
		debug.info("aborted loadAjaxInDialog");
		debug.info("containerId: " + containerId);
		debug.info("ajaxPageUrl: " + ajaxPageUrl);
		debug.info("aborted loadAjaxInDialog (end)");
		return;
	}
	
    $.ajax({
        method: 'get',
        url: ajaxPageUrl,
        data: 'page=' + $(this).attr('rel'),
        beforeSend: function() {
            container.innerHTML=loadingImageHtml(true);
        },
        complete: function() {
        	debug.info("load of html is complete.");
        	// container.innerHTML=loadingImageHtml(true);
        },
        success: function(html) {
        	console.log("loading html: " + html);
        	container.innerHTML=html;
        	openDialogWindow(containerId, title, width, height);
        	executeAjaxLoadedPageJS(html);
		},
		fail: function() { 
			alert("error: Impossible to load page " + ajaxPageUrl); 
		}
	});
}

function openDialogWindow(containerId, title, width, height) {
	var container = getContainer(containerId);
	if (container === null) {
		debug.info("aborted openDialogWindow");
		debug.info("containerId: " + containerId);
		debug.info("title: " + title);
		debug.info("aborted openDialogWindow (end)");
		return;
	}
	
	if (nullOrUndefined(title))
		title = "undefined";
	if (nullOrUndefined(width))
		width = "auto";
	if (nullOrUndefined(height))
		height = "auto";
	
	$(container).dialog({
        // add a close listener to prevent adding multiple divs to the document
        close: function(event, ui) {
            // remove div with all data and events
            // dialog.remove();
            container.innerHTML = "";
        },
        modal: true,
        resizable: true, 
        height: height, // 800,
        width: width, // 800,
        title: title
    });
    // $( "#" + fileViewContentsDivId ).dialog();
}

/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */

function getIframeWindow(iframe_object) {
	  var doc = null;

	  if (iframe_object.contentWindow) return iframe_object.contentWindow;
	  if (iframe_object.window) return iframe_object.window;

	  if ((doc == null) && iframe_object.contentDocument) doc = iframe_object.contentDocument;
	  if ((doc == null) && iframe_object.document) doc = iframe_object.document;

	  if ((doc != null) && doc.defaultView) return doc.defaultView;
	  if ((doc != null) && doc.parentWindow) return doc.parentWindow;

	  debug.info("getIframeWindow returns null");
	  return null;
	}

function notNullNorUndefined(value) {
	return ((value != null) && (value != undefined));
}

function nullOrUndefined(value) {
	return ((value == null) || (value == undefined));
}


/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */

function clearMainSection() {
	var mainSection = getContainer('<%=KConstants.JspsDivsIds.mainSecDivId %>');
	mainSection.innerHTML = "";
}

function clearMsgsSection() {
	var mainSection = getContainer('<%=KConstants.JspsDivsIds.msgsSecDivId %>');
	mainSection.innerHTML = "";
}

function showMsgsArray(msgs) {
	var divId = '<%=KConstants.JspsDivsIds.msgsSecDivId %>';
	showMsgsArrayInDiv(divId, msgs);
}

function showMsgsArrayInDiv(divId, msgs) {
	var msgsContainer = getContainer(divId);

	if (msgsContainer != null) {
		msgsContainer.innerHTML = "";
		if (Array.isArray(msgs)) {
			if (msgs.length > 0) {
				for (var i=0; i<msgs.length; i++) {
					var subDiv = document.createElement('div');
					// row.className = "";
					// row.id = destinyAddLine;
					// document.getElementById(msgsContainerId)
					subDiv.innerHTML = msgs[i];
					msgsContainer.appendChild(subDiv);
				}
			}
			else {
				// alert("showMsgs: msgs length is 0.");
				debug.info("showMsgs: msgs length is 0.");
			}
		}
		else {
			// alert("showMsgs: msgs is not an array.");
			debug.info("showMsgs: msgs is not an array.");
		}
	}
	else {
		// alert("showMsgs: msgs container is null.");
		debug.info("showMsgs: msgs container is null.");
	}
}


function isString(o) {
	var result = false;
	if ((o != null) && (o != undefined)) {
		result = (typeof o == "string") || (o instanceof String) || (typeof o == "object" && o.constructor === String);
	}
	// alert("isString returns " + result);
	return result;
}

/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */

function getComboBoxValue(comboBox) {
	if (comboBox == null) {
		debug.info("getComboBoxValue: comboBox is null");
		return "";
	}
	if (comboBox == undefined) {
		debug.info("getComboBoxValue: comboBox is undefined");
		return "";
	}
	
	var comboBoxSelectedIndex = comboBox.selectedIndex;
	if (comboBoxSelectedIndex == null) {
		debug.info("getComboBoxValue: comboBoxSelectedIndex is null");
		return "";
	}
	if (comboBoxSelectedIndex == undefined) {
		debug.info("getComboBoxValue: comboBoxSelectedIndex is undefined");
		return "";
	}
	
	var comboBoxSelectedField = comboBox.options[comboBoxSelectedIndex];
	if (comboBoxSelectedField == null) {
		debug.info("getComboBoxValue: comboBoxSelectedField is null");
		return "";
	}
	if (comboBoxSelectedField == undefined) {
		debug.info("getComboBoxValue: comboBoxSelectedField is undefined");
		return "";
	}

	var comboBoxValue = comboBoxSelectedField.value;
	var comboBoxText = comboBoxSelectedField.text;
	var comboBoxName = comboBoxSelectedField.name;
	var comboBoxTitle = comboBoxSelectedField.title;
	debug.info("getComboBoxValue: value: " + comboBoxValue);
	debug.info("getComboBoxValue: text: " + comboBoxText);
	debug.info("getComboBoxValue: name: " + comboBoxName);
	debug.info("getComboBoxValue: title: " + comboBoxTitle);

	if (comboBoxValue === null) {
		debug.info("getComboBoxValue: comboBoxValue is null (===).");
		return "";
	}
	
	// alert("comboBoxValue: " + comboBoxValue);
	if (comboBoxValue == null) {
		debug.info("getComboBoxValue: comboBoxValue is null (==).");
		return "";
	}
	
	if (comboBoxValue == "") {
		debug.info("getComboBoxValue: comboBoxValue is empty string.");
		return "";
	} 

	if (comboBoxValue == "----") {
		debug.info("getComboBoxValue: comboBoxValue is default value (----).");
		return "";
	}
	
	return comboBoxValue;
}

function getFieldValue(fieldName) {
	if (! isString(fieldName)) return null;
	var field = document.getElementById(fieldName);
	if (field == null) {
		debug.info("null field for fieldName "+fieldName);
		return "";
	}
	if (field.value == null) {
		debug.info("null field value for fieldName "+fieldName);
		if (field.selectedIndex != null) {
			return getComboBoxValue(field);
		}
		else {
			return "";
		}
	}
	if (field.value == undefined) {
		debug.info("undefined field value for "+fieldName);
		return "";
	}
	
	if (field.value == '----') {
		return "";
	}
	
	return (field.value);
}

function splitStringResult(head, tail) {
	this.head = head;
	this.tail = tail;
}

function splitString(inputString, separation) {
	var splitStringResult = inputString;
	var i = comboBoxValue.indexOf(separation);
	if (i != -1) {
		splitStringResult = new splitStringResult('', '');
		splitStringResult.head = comboBoxValue.substring(0, i);
		splitStringResult.tail = comboBoxValue.substring(i+separation.length);
	}
	return splitStringResult;
}

/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */

function loadUserOptions() {
	showMsgsArray(<%= JspsUtils.getEmptyArrayMessagesInJs() %>);
	loadAjaxIn('<%= KConstants.JspsDivsIds.mainSecDivId %>', '<%= KUrls.User.Options.getUrl(true) %>');
	return false;
}

function loadNewQuery() {
	showMsgsArray(<%= JspsUtils.getEmptyArrayMessagesInJs() %>);
	loadAjaxIn('<%= KConstants.JspsDivsIds.mainSecDivId %>', '<%= KUrls.Queries.SelectProgramFile.getUrl(true) %>');
	return false;
}

/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */

function selectedProgramDatabaseChanged(comboBox, urlSelectQueryStartType, urlProgramFileActions ,fuzzificationUpdateStatusDivId, updateURl) {
	showMsgsArray(<%= JspsUtils.getEmptyArrayMessagesInJs() %>);
	var selectQueryDivId = '<%= KConstants.JspsDivsIds.selectQueryDivId %>'; 
	var runQueryDivId = '<%= KConstants.JspsDivsIds.runQueryDivId %>';
	var programFileActionsContainerId = "<%=KConstants.JspsDivsIds.programFileActionsContainerId %>";
	
	var selectQueryDiv = document.getElementById(selectQueryDivId);
	debugInfoIfVarIsNull(selectQueryDiv, "selectQueryDiv", "selectedProgramDatabaseChanged");
	selectQueryDiv.innerHTML = loadingImageHtml(true);
	
	var runQueryDiv = document.getElementById(runQueryDivId);
	debugInfoIfVarIsNull(runQueryDiv, "runQueryDiv", "selectedProgramDatabaseChanged");
	runQueryDiv.innerHTML = "";
	
	var programFileActionsContainer = getContainer(programFileActionsContainerId);
	
	var selectedProgramFileUrlParams = getComboBoxValue(comboBox);
	
	if (selectedProgramFileUrlParams == "") {
		programFileActionsContainer.style.visibility = 'hidden'; 
		selectQueryDiv.innerHTML="Please choose a valid database to continue.";
	}
	else {
		loadAjaxIn(selectQueryDivId, urlSelectQueryStartType + selectedProgramFileUrlParams);
		programFileActionsContainer.style.visibility = 'visible';
		loadAjaxIn(programFileActionsContainerId, urlProgramFileActions + selectedProgramFileUrlParams);
	}
	
}

/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */

function selectedQueryStartTypeChanged(comboBox, url1, url2) {
	var queryLinesContainerId = "<%=KConstants.JspsDivsIds.queryLinesContainerId %>";
	var searchOrPersonalizeTableId = "<%=KConstants.JspsDivsIds.searchOrPersonalizeTableId %>";
	
	var startupType = getComboBoxValue(comboBox);
	debug.info("startupType changed to " + startupType);
	
	resetQueryLinesCounterFieldValue();
	var queryLinesContainerDiv = getContainer(queryLinesContainerId); 
	queryLinesContainerDiv.innerHTML="";
	
	var searchOrPersonalizeTable = getContainer(searchOrPersonalizeTableId);
	
	if ((startupType == "") || (startupType == '')) {
		queryLinesContainerDiv.innerHTML="Please select what are you looking for.";
		queryLinesContainerDiv.style.display='none';
		searchOrPersonalizeTable.style.visibility = 'hidden'; 
	}
	else {
		searchOrPersonalizeTable.style.visibility = 'visible';
		queryLinesContainerDiv.style.display='block';
		loadAjaxIn(queryLinesContainerId, url1 + startupType);
	}
}

/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */

function resetQueryLinesCounterFieldValue() {
	var queryLinesCounterFieldId = "<%=KConstants.Request.linesCounterParam %>";
	document.getElementById(queryLinesCounterFieldId).value = 0;
}

function incrementQueryLinesCounterFieldValue() {
	var queryLinesCounterFieldId = "<%=KConstants.Request.linesCounterParam %>";
	document.getElementById(queryLinesCounterFieldId).value ++;
	return getQueryLinesCounterFieldValue();
}

function getQueryLinesCounterFieldValue() {
	var queryLinesCounterFieldId = "<%=KConstants.Request.linesCounterParam %>";
	if ((document.getElementById(queryLinesCounterFieldId) != null) &&
		(document.getElementById(queryLinesCounterFieldId) != undefined) &&
		(document.getElementById(queryLinesCounterFieldId).value != null) &&
		(document.getElementById(queryLinesCounterFieldId).value != undefined)) {
		return document.getElementById(queryLinesCounterFieldId).value;
	}
	else return 0;
}

/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */

function selectQueryAddLine(urlQueryAddLine, urlQueryAddAggregator) {
	debug.info("selectQueryAddLine");
	debug.info(urlQueryAddLine);
	debug.info(urlQueryAddAggregator);
	var queryLinesTableId = "<%=KConstants.JspsDivsIds.queryLinesTableId %>";
	var queryLinesAggregatorTableId = "<%=KConstants.JspsDivsIds.queryLinesAggregatorTableId %>";
	
	var queryLinesCounter = getQueryLinesCounterFieldValue();
	var queryLineId = "queryLine[" + queryLinesCounter + "]";
	var destinyAddLine = queryLineId + ".row";

	var row = document.createElement('div');
	row.className = queryLinesTableId + "Row";
	row.id = destinyAddLine;
	document.getElementById(queryLinesTableId).appendChild(row);
	
	var lineInfo = "&" + "<%= KConstants.Request.lineNumberParam %>" + "=" + queryLinesCounter;
	var lineId = "&" + "<%= KConstants.Request.lineIdParam %>" + "=" + queryLineId;
	var linesCounter = "&" + "<%= KConstants.Request.linesCounterParam %>" + "=" + queryLinesCounter;
	
	loadAjaxIn(destinyAddLine, urlQueryAddLine + lineInfo + lineId);
	loadAjaxIn(queryLinesAggregatorTableId, urlQueryAddAggregator + lineInfo + lineId + linesCounter);
	
	queryLinesCounter = incrementQueryLinesCounterFieldValue();
	// Do not allow navigator to call url.
	return false;
}

/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */

function selectPredicateChanged(comboBox, lineId, negUrl, quantUrl, opUrl, valueUrl) {
	// lineId + ".predicate"
	var startupType = getComboBoxValue(comboBox);
	
	loadAjaxIn(lineId + ".negationDiv", negUrl + startupType);
	loadAjaxIn(lineId + ".quantifierDiv", quantUrl + startupType);
	loadAjaxIn(lineId + ".operatorDiv", opUrl + startupType);
	loadAjaxIn(lineId + ".valueDiv", valueUrl + startupType);
	
	/*
	if (foundPredInfo.predType[typeIndex][foundPredInfo.predType[typeIndex].length -1] == 'rfuzzy_truth_value_type') {
		insertChooseQuantifier(queryLineId, rowId, 1, queryLinesTableId);
		insertChooseQuantifier(queryLineId, rowId, 0, queryLinesTableId);
	}
	else {
		insertRfuzzyComputeOperator(queryLineId, rowId, index, typeIndex, queryLinesTableId);
		insertRfuzzyComputeArgument(queryLineId, rowId, index, typeIndex, queryLinesTableId);
	}
	*/
}

/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */

function aggregatorDetailsShow(toShowMsgId, toHideMsgId, chooseAgregatorInfoCellId, chooseAgregatorCellId) {
	document.getElementById(toShowMsgId).style.display='none';
	
	document.getElementById(toHideMsgId).style.display='inline';
	document.getElementById(toHideMsgId).className = "queryLineAggregatorTable";
	
	document.getElementById(chooseAgregatorInfoCellId).style.display='table-cell';
	document.getElementById(chooseAgregatorInfoCellId).className = "queryLineAggregatorTable";
	
	document.getElementById(chooseAgregatorCellId).style.display='table-cell';
	document.getElementById(chooseAgregatorCellId).className = "queryLineAggregatorTable";
	return false;
}

function aggregatorDetailsHide(toShowMsgId, toHideMsgId, chooseAgregatorInfoCellId, chooseAgregatorCellId) {
	document.getElementById(toHideMsgId).style.display='none';
	document.getElementById(chooseAgregatorInfoCellId).style.display='none';
	document.getElementById(chooseAgregatorCellId).style.display='none';
	
	document.getElementById(toShowMsgId).style.display='inline';
	document.getElementById(toShowMsgId).className = "queryLineAggregatorTable";
	return false;		
}

/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */

/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */

function evaluateQuery(url) {

	var runQueryDivId = "<%= KConstants.JspsDivsIds.runQueryDivId %>";
	debug.info("runQueryAfterSoftTests");
	
	var database = getFieldValue("<%=KConstants.Request.databaseParam %>");
	url += "&<%=KConstants.Request.databaseParam %>=" + database; 
	
	var aggregator = getFieldValue("<%=KConstants.Request.aggregatorParam %>");
	url += "&<%=KConstants.Request.aggregatorParam %>=" + aggregator; 
	
	var linesCounter = getQueryLinesCounterFieldValue();
	url += "&<%=KConstants.Request.linesCounterParam %>=" + linesCounter;
		
	var fields = ["<%=KConstants.Request.predicateParam %>", 
	              "<%=KConstants.Request.negationParam %>", "<%=KConstants.Request.quantifierParam %>",
	              "<%=KConstants.Request.operatorParam %>", "<%=KConstants.Request.valueParam %>"];
		
	var tmp = null;
		
	for (var i=0; i < linesCounter; i++) {
		for (var j=0; j < fields.length; j++) {

			tmp = getFieldValue("queryLine["+i+"]." + fields[j]);
			if ((tmp != null) && (tmp != "")) {
				url += "&queryLine["+i+"]." + fields[j] + "=" + tmp;
			}
		}
	}
	
	loadAjaxIn(runQueryDivId, url);

	// Tell the navigator not to follow the link !!!
	return false;
}

/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */
/* ---------------------------------------------------------------------------------------------------------------- */

function openUrlInNewTab(url)
{
  var win=window.open(url, '_blank');
  win.focus();
  return false;
}

function launchCallsRegistry() {
	var url = '<%= KUrls.Auth.CallsRegistry.getUrl(false) %>';
	debug.info("Opening url: " + url);
	return openUrlInNewTab(url);
}

/* ----------------------------------------------------------------------------------------------------------------------------*/
/* ----------------------------------------------------------------------------------------------------------------------------*/
/* ----------------------------------------------------------------------------------------------------------------------------*/

// EOF

/* ----------------------------------------------------------------------------------------------------------------------------*/
/* ----------------------------------------------------------------------------------------------------------------------------*/
/* ----------------------------------------------------------------------------------------------------------------------------*/

<% if (JspsUtils.getStringWithValueS().equals("N")) { %>
</script>
<% } %>


