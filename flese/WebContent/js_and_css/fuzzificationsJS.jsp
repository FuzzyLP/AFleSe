/*! * auxiliarJS Library v1 * auxiliar javascript code * Author: Victor
Pablos Ceruelo */ /*
----------------------------------------------------------------------------------------------------------------
*/ /*
----------------------------------------------------------------------------------------------------------------
*/ /*
----------------------------------------------------------------------------------------------------------------
*/

<%@page import="constants.KConstants.Fuzzifications"%>
<%@page import="constants.KUrls"%>
<%@page import="constants.KConstants"%>
<%@page import="auxiliar.JspsUtils"%>

<% if (JspsUtils.getStringWithValueS().equals("N")) { %>
<script type="text/javascript">
<% } %>

/* ----------------------------------------------------------------------------------------------------------------------------*/
/* ----------------------------------------------------------------------------------------------------------------------------*/
/* ----------------------------------------------------------------------------------------------------------------------------*/

var fuzzificationFunction = null;

function fuzzificationPoints (predOwner, predOwnerHumanized, functionPoints) {
	this.predOwner = predOwner;
	this.name = predOwnerHumanized;
	this.data = functionPoints;
	this.modified = false;
}

function fuzzificationFunctionConstructor(predDefined, predNecessary, modifiable, msgTop, msgBottom, fuzzificationPoints) {
	this.predDefined = predDefined;
	this.predNecessary = predNecessary;
	this.modifiable = modifiable;
	this.msgTop = msgTop;
	this.msgBottom = msgBottom;
	this.fuzzificationPoints = fuzzificationPoints; // ownersPersonalizations
}

function setFuzzificationFunction (predDefined, predNecessary, modifiable, msgTop, msgBottom, fuzzificationPoints) {
	fuzzificationFunction = new fuzzificationFunctionConstructor(predDefined, predNecessary, modifiable, msgTop, msgBottom, fuzzificationPoints);
}

function indexOfMyPersonalizedFunction() {
	var i = fuzzificationFunction.modifiable;
	
	if (i < fuzzificationFunction.fuzzificationPoints.length) {
		return i;
	}
	
	debug.info("IndexOfMyPersonalizedFunction: i < 0");
	return -1;
	
}

function changeFuzzificationPointValue(fpx, valueFloat) {
	var i = indexOfMyPersonalizedFunction();

	if (i >= 0) {
		var j=0; 
		var found = false;
		
		while ((j < fuzzificationFunction.fuzzificationPoints[i].data.length) && (! found)) {
			if (fuzzificationFunction.fuzzificationPoints[i].data[j][0] == fpx) {
				fuzzificationFunction.fuzzificationPoints[i].data[j][1] = valueFloat;
				fuzzificationFunction.fuzzificationPoints[i].modified = true;
				found = true;
			}
			else {
				j++;
			}
		}
		if (! found) {
			debug.info("changeFuzzificationPointValue: not found");
		}
	}
	else {
		debug.info("changeFuzzificationPointValue: not found");
	}
}

/* ----------------------------------------------------------------------------------------------------------------------------*/
/* ----------------------------------------------------------------------------------------------------------------------------*/
/* ----------------------------------------------------------------------------------------------------------------------------*/

function barValueChanged(barObject, fuzzificationBarDivId, fpx, fuzzificationGraphicDivId) {
	
	debug.info("barValueChanged");
	
	if ((barObject == null) || (barObject == undefined)) {
		debug.info("barObject is null or undefined in barValueChanged.");
		return;
	}
	
	var valueOriginal=barObject.value;
	if  ((valueOriginal == null) || (valueOriginal == undefined) || (isNaN(valueOriginal))) {
		alert("Erroneous value. I'll reset to 0.");
		barObject.value = 0;
		valueOriginal = 0;
		return;
	}
	
	var valueFloat = parseFloat(valueOriginal);
	var valueToShow = 0;
	
	if ((valueFloat != null) && (valueFloat != undefined) && (! isNaN(valueFloat))) {
		valueToShow = valueFloat.toFixed(2);
	}
	else {
		valueFloat = 0;
		valueToShow = 0;
	}
		
	// Modify the stored value 
	changeFuzzificationPointValue(fpx, valueFloat);

	// Show the value in the div.
	var fuzzificationBarDiv = getContainer(fuzzificationBarDivId);
	fuzzificationBarDiv.innerHTML = valueToShow;

	// Display in the graphic the result.
	insertFuzzificationGraphicRepresentation(fuzzificationGraphicDivId);
}


/* ----------------------------------------------------------------------------------------------------------------------------*/
/* ----------------------------------------------------------------------------------------------------------------------------*/
/* ----------------------------------------------------------------------------------------------------------------------------*/

function insertFuzzificationGraphicRepresentation(fuzzificationGraphicDivId) {
	
	var div = document.getElementById(fuzzificationGraphicDivId);
	if ((div != null) && (div != undefined)) {
		div.innerHTML = ""; // Re-initialize
	
		var container = document.createElement('div');
		container.id = fuzzificationGraphicDivId + "Container";
		container.style.width= "50em"; 
		container.style.height= "15em";
		container.style.margin= "2em";
		container.style['text-align'] = "center";
		container.style['align'] = "center";
		div.appendChild(container);
	
		// alert("insertFuzzificationGraphicRepresentation not implemented yet !!!");
		drawChart(container.id);
	}
}

/* ----------------------------------------------------------------------------------------------------------------------------*/
/* ----------------------------------------------------------------------------------------------------------------------------*/
/* ----------------------------------------------------------------------------------------------------------------------------*/

// var charts = new Array(); // globally available
var chart = null;

function drawChart(divIdentifier) {
	
	if ((fuzzificationFunction != null) && (fuzzificationFunction.fuzzificationPoints != null)) {

		var minValue = null;
		var maxValue = null;
		var currentValue = 0;
		for (var i=0; i<fuzzificationFunction.fuzzificationPoints.length; i++) {
			for (var j=0; j<fuzzificationFunction.fuzzificationPoints[i].data.length; j++) {
				currentValue = fuzzificationFunction.fuzzificationPoints[i].data[j][0];
				if ((minValue == null) || (minValue > currentValue)) {
					minValue = currentValue;
				}
				if ((maxValue == null) || (maxValue < currentValue)) {
					maxValue = currentValue;
				}
			}
		}
		
		var extension = (maxValue - minValue) * (10 / 100);
		if (! (minValue == 0)) {
			minValue = minValue - extension;
		}
		maxValue = maxValue + extension;
		
//		$(document).ready(function() {
			  // charts[i] = 
		      chart = new Highcharts.Chart({
		         chart: {
	    	        renderTo: divIdentifier,
	        	    type: 'line' //,
/*					style: { margin: '0 auto' } */
		         },
		         title: { text: fuzzificationFunction.msgTop },
		         xAxis: {
					title: { text: fuzzificationFunction.msgBottom },
					min: minValue,
					max: maxValue
		            // categories: ['Apples', 'Bananas', 'Oranges']
		         },
		         yAxis: {
					title: { text: 'Truth value' },
					min: 0,
					max: 1
		         	// categories: [0, 0.25, 0.5, 0.75, 1]
	    	     },
/*	    	     navigator: { height: 30, width: 40 }, center: [60, 45], size: 50, */
	        	 series: fuzzificationFunction.fuzzificationPoints
		         		/*	[{ name: 'Jane', data: [1, 0, 4] }, { name: 'John', data: [5, 7, 3] }] */
		      });
//		   });
	}
}

/* ----------------------------------------------------------------------------------------------------------------------------*/
/* ----------------------------------------------------------------------------------------------------------------------------*/
/* ----------------------------------------------------------------------------------------------------------------------------*/

/* ----------------------------------------------------------------------------------------------------------------------------*/
/* ----------------------------------------------------------------------------------------------------------------------------*/
/* ----------------------------------------------------------------------------------------------------------------------------*/

/* ----------------------------------------------------------------------------------------------------------------------------*/
/* ----------------------------------------------------------------------------------------------------------------------------*/
/* ----------------------------------------------------------------------------------------------------------------------------*/

function personalizeProgramFile(url, params, fileName) {
	var containerId = '<%=KConstants.JspsDivsIds.auxAndInvisibleSection %>';
	
	loadAjaxInDialog(containerId, url + params, 'Personalize program file ' + fileName);
	
	//prevent the browser to follow the link
	return false;
}

/* ----------------------------------------------------------------------------------------------------------------------------*/
/* ----------------------------------------------------------------------------------------------------------------------------*/
/* ----------------------------------------------------------------------------------------------------------------------------*/

function fuzzificationFunctionNameInColloquial(currentName, grade) {
	var result = null;
	if ((currentName != null) && (currentName != "") && (currentName != "----")) {
	
		var i = currentName.indexOf("(");
		var j = currentName.indexOf(")");
		
		if ((i != -1) && (j != -1)){
			result = "";
			if ((grade == 'all') || (grade == 'subject')) {
				result += currentName.substring(i+"(".length, j);
			}
			if (grade == 'all') {
				result += " is ";
			}
			if ((grade == 'all') || (grade == 'adjective')) {
				result += currentName.substring(0, i);
			}
		}
	}
	
	if ((result == null) && (currentName != null)) return currentName;
	else return prologNameInColloquialLanguage(result);
}

/* ----------------------------------------------------------------------------------------------------------------------------*/
/* ----------------------------------------------------------------------------------------------------------------------------*/
/* ----------------------------------------------------------------------------------------------------------------------------*/

function personalizationFunctionChanged(comboBox, PersonalizationFunctionUnderModificationDivId, url) {
	
	var params = getComboBoxValue(comboBox);
	if (params != "") {
		loadAjaxIn(PersonalizationFunctionUnderModificationDivId, url + params);
	}
	else {
		var container = getContainer(PersonalizationFunctionUnderModificationDivId);
		container.innerHTML = "Please choose a valid fuzzification function.";
	}
	
}

/* ----------------------------------------------------------------------------------------------------------------------------*/
/* ----------------------------------------------------------------------------------------------------------------------------*/
/* ----------------------------------------------------------------------------------------------------------------------------*/

function saveFuzzification(fuzzificationSaveStatusDivId, saveUrl) {
	var fuzzificationSaveStatusDiv = getContainer(fuzzificationSaveStatusDivId);
	fuzzificationSaveStatusDiv.innerHTML = loadingImageHtml(false);
	
	var i = indexOfMyPersonalizedFunction();

	if (i >= 0) {
		if (fuzzificationFunction.fuzzificationPoints[i].modified) {
			for (var j=0; j < fuzzificationFunction.fuzzificationPoints[i].data.length; j++) {
				var fpx = fuzzificationFunction.fuzzificationPoints[i].data[j][0];
				var fpy = fuzzificationFunction.fuzzificationPoints[i].data[j][1];
		
				saveUrl += "&fpx["+j+"]=" + fpx;
				saveUrl += "&fpy["+j+"]=" + fpy;
			}
	
			fuzzificationFunction.fuzzificationPoints[i].modified = false;
			loadAjaxIn(fuzzificationSaveStatusDivId, saveUrl);
		}
		else {
			fuzzificationSaveStatusDiv.innerHTML = "No changes to save.";
		}
	}
	else {
		fuzzificationSaveStatusDiv.innerHTML = "Impossible to save fuzzification modifications.";
	}
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
