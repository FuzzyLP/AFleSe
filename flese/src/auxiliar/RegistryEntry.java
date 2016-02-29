package auxiliar;

import java.util.Date;

import storeHouse.RequestStoreHouse;

public class RegistryEntry {

	private Date dateIn;
	private Date dateOut;
	private String manager;
	private String op;
	private String msg;
	private String requestParams;
	private NextStep nextStep;
	private boolean isAjax;

	public RegistryEntry(String manager, String op, String msg, RequestStoreHouse requestStoreHouse) {
		if (manager == null) {
			manager = "";
		}
		if (op == null) {
			op = "";
		}
		if (msg == null) {
			msg = "";
		}

		String requestParamsAux = "";
		if (requestStoreHouse != null) {
			requestParamsAux = requestStoreHouse.getRequestParametersString();
		}
		if (requestParamsAux == null) {
			requestParamsAux = "";
		}

		this.dateIn = Dates.getCurrentDate();
		this.dateOut = Dates.getCurrentDate();
		this.manager = manager;
		this.op = op;
		this.msg = msg;
		this.requestParams = requestParamsAux;
		this.isAjax = false;
		this.nextStep = null;
	}

	public RegistryEntry(RegistryEntry registryEntry, NextStep nextStep, boolean isAjax) {
		this.dateIn = (registryEntry == null) ? Dates.getCurrentDate() : registryEntry.getDateIn();
		this.dateOut = Dates.getCurrentDate();
		this.manager = (registryEntry == null) ? "" : registryEntry.getManager();
		this.op = (registryEntry == null) ? "" : registryEntry.getOp();
		this.msg = (registryEntry == null) ? "" : registryEntry.getMsg();
		this.nextStep = null;
		this.isAjax = isAjax;
		this.requestParams = registryEntry.getRequestParams();

		if (nextStep != null) {
			this.nextStep = nextStep;
		}
	}

	public void setMsg(String msg) {
		if ((this.msg != null) && (!"".equals(msg))) {
			this.msg = msg;
		}
	}

	public Date getDateIn() {
		return this.dateIn;
	}

	public String getStringOfDateIn() {
		return Dates.getStringOfDate(this.dateIn, Dates.longFormat);
	}

	public Date getDateOut() {
		return this.dateOut;
	}

	public String getStringOfDateOut() {
		return Dates.getStringOfDate(this.dateOut, Dates.longFormat);
	}

	public String getManager() {
		return this.manager;
	}

	public String getOp() {
		return this.op;
	}

	public String getMsg() {
		return this.msg;
	}

	public String getRequestParams() {
		return this.requestParams;
	}

	public NextStep getNextStep() {
		return this.nextStep;
	}

	public boolean getIsAjax() {
		return this.isAjax;
	}

	public String getHtmlTableHead() {
		StringBuilder htmlTableHeadSB = new StringBuilder();
		htmlTableHeadSB.append("<tr>");
		htmlTableHeadSB.append("<th>");
		htmlTableHeadSB.append("Time In");
		htmlTableHeadSB.append("</th>");
		htmlTableHeadSB.append("<th>");
		htmlTableHeadSB.append("Time Out");
		htmlTableHeadSB.append("</th>");
		htmlTableHeadSB.append("<th>");
		htmlTableHeadSB.append("Manager");
		htmlTableHeadSB.append("</th>");
		htmlTableHeadSB.append("<th>");
		htmlTableHeadSB.append("Operation");
		htmlTableHeadSB.append("</th>");
		htmlTableHeadSB.append("<th>");
		htmlTableHeadSB.append("Info");
		htmlTableHeadSB.append("</th>");
		htmlTableHeadSB.append("<th>");
		htmlTableHeadSB.append("Request Params");
		htmlTableHeadSB.append("</th>");
		htmlTableHeadSB.append("<th>");
		htmlTableHeadSB.append("Next Step");
		htmlTableHeadSB.append("</th>");
		htmlTableHeadSB.append("</tr>");
		return htmlTableHeadSB.toString();
	}

	public String getHtmlTableRow() {
		StringBuilder htmlTableRowSB = new StringBuilder();
		htmlTableRowSB.append("<tr>");
		htmlTableRowSB.append("<td>");
		htmlTableRowSB.append(getHtmlFormatOfDate(getDateIn()));
		htmlTableRowSB.append("</td>");
		htmlTableRowSB.append("<td>");
		htmlTableRowSB.append(getHtmlFormatOfDate(getDateOut()));
		htmlTableRowSB.append("</td>");
		htmlTableRowSB.append("<td>");
		htmlTableRowSB.append(getManagerInHtmlFormat());
		htmlTableRowSB.append("</td>");
		htmlTableRowSB.append("<td>");
		htmlTableRowSB.append(getOp());
		htmlTableRowSB.append("</td>");
		htmlTableRowSB.append("<td>");
		htmlTableRowSB.append(getMsg());
		htmlTableRowSB.append("</td>");
		htmlTableRowSB.append("<td>");
		htmlTableRowSB.append(getRequestParamsInHtmlFormat());
		htmlTableRowSB.append("</td>");
		htmlTableRowSB.append("<td>");
		htmlTableRowSB.append(getNextStepInHtmlFormat());
		htmlTableRowSB.append("</td>");
		htmlTableRowSB.append("</tr>");
		return htmlTableRowSB.toString();
	}

	private String getHtmlFormatOfDate(Date dateIn) {
		String longFormat = Dates.getStringOfDate(dateIn, Dates.longFormat);
		String shortFormat = Dates.getStringOfDate(dateIn, Dates.timeFormat);
		return "<a href=\'\' onclick=\'return false;\' title=\'" + longFormat + "\'>" + shortFormat + "</a>";
	}

	private String getHtmlFormatOfUrl(String url) {
		if (url == null) {
			return "";
		}
		String longFormat = url;
		String shortFormat = longFormat;

		if (shortFormat.lastIndexOf("/") > 0) {
			if ((shortFormat.lastIndexOf("/") + 1 < shortFormat.length())) {
				shortFormat = shortFormat.substring(shortFormat.lastIndexOf("/") + 1);
				if (shortFormat.lastIndexOf("?") > 0) {
					if ((shortFormat.lastIndexOf("?") < shortFormat.length())) {
						shortFormat = shortFormat.substring(0, shortFormat.lastIndexOf("?"));
					}
				}
			}
		}
		return "<a href=\'\' onclick=\'return false;\' title=\'" + longFormat + "\'>" + shortFormat + "</a>";
	}

	private String getManagerInHtmlFormat() {
		String longFormat = getManager();
		String shortFormat = getManager();

		if (shortFormat.lastIndexOf(".") > 0) {
			if ((shortFormat.lastIndexOf(".") + 1 < shortFormat.length())) {
				shortFormat = shortFormat.substring(shortFormat.lastIndexOf(".") + 1);
			}
		}
		return "<a href=\'\' onclick=\'return false;\' title=\'" + longFormat + "\'>" + shortFormat + "</a>";
	}

	private String getNextStepInHtmlFormat() {
		String longFormat = getNextStep() == null ? "" : getNextStep().getLoggingInformation(getIsAjax());
		return getHtmlFormatOfUrl(longFormat);
	}

	private String getRequestParamsInHtmlFormat() {
		String longFormat = getRequestParams() == null ? "" : getRequestParams();
		String shortFormat = "params";
		return "<a href=\'\' onclick=\'return false;\' title=\'" + longFormat + "\'>" + shortFormat + "</a>";
	}
}
