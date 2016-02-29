package auxiliar;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class HtmlPrinterClass {

	PrintWriter out = null;
	
	public HtmlPrinterClass (HttpServletResponse response, Boolean setContentTypeToTextAndPlain) throws IOException {
		out = response.getWriter();
		if (setContentTypeToTextAndPlain) {
			response.setContentType("text/plain");
		}
	}
	
	public void write(String Text) {
		out.println(Text);
	}
    
	public void end(){
		out.close();
	}
	
}
