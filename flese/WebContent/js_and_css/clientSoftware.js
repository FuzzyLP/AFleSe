/*!
 * tabsSelection Library v1
 * Author: Victor Pablos Ceruelo
 */

var clientSoftware = "";
var clientSoftwareVersion = "";

function detectClientSoftware() {
	if (/Firefox[\/\s](\d+\.\d+)/.test(navigator.userAgent)){ //test for Firefox/x.x or Firefox x.x (ignoring remaining digits);
		clientSoftware="firefox";
		clientSoftwareVersion=new Number(RegExp.$1); // capture x.x portion and store as a number
	}
	else {
		if (/MSIE (\d+\.\d+);/.test(navigator.userAgent)){ //test for MSIE x.x;
			clientSoftware="msie";
			clientSoftwareVersion=new Number(RegExp.$1); // capture x.x portion and store as a number
		}
		else {
			//Note: userAgent in Opera9.24 WinXP returns: Opera/9.24 (Windows NT 5.1; U; en)
			//      userAgent in Opera 8.5 (identified as IE) returns: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1) Opera 8.50 [en]
			//      userAgent in Opera 8.5 (identified as Opera) returns: Opera/8.50 (Windows NT 5.1; U) [en]

			if (/Opera[\/\s](\d+\.\d+)/.test(navigator.userAgent)){ //test for Opera/x.x or Opera x.x (ignoring remaining decimal places);
				clientSoftware="opera";
				clientSoftwareVersion=new Number(RegExp.$1); // capture x.x portion and store as a number
			}
		}
	}
}
