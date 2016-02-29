<%@page import="urls.ServerAndAppUrls"%>
<%@page import="constants.KUrls"%>
<%@page import="constants.KConstants"%>
<jsp:include page="WEB-INF/commonHtmlHead.jsp" />

<body>
	<div class="indexPageTitle">FleSe</div>
	<br />
	<br />
	<div class="indexPageTitleExplanation">
		<%= KConstants.Titles.FirstLine %>
		<%= KConstants.Titles.SecondLine %>
	</div>
	<br />
	<br />
	<div class='indexPageImportantLink'>
		<a class='indexPageImportantLink'
			href="<%=KUrls.Auth.SignOut.getUrl(false) %>">Go to the
			application</a>
	</div>
	<br />
	<br />
	<div class='indexPageSubTitle'>What is FleSe</div>
	<div class='indexPageText'>Flese is a framework allowing the
		final user to perform fuzzy queries (as "fast red car") in databases
		that might have no fuzzy information at all.</div>
	<br />
	<br />
	<div class='indexPageSubTitle'>FleSe in detail</div>
	<div class='indexPageText'>
		Flese is a framework composed by two sub-frameworks: the engine that
		processes fuzzy queries and the web interface that presents results in
		a human-readable way.
		<ul>
			<li>The engine is an improved version of the framework Rfuzzy
				presented in <a
				href="http://www.sciencedirect.com/science/article/pii/S0020025510003610">&ldquo;RFuzzy:
					Syntax, semantics and implementation details of a simple and
					expressive fuzzy tool over Prolog&rdquo;</a>. In this new framework we
				have included the management of quantifiers (even negation),
				similarity, overload of attribute's names and others. Its main
				advantage over some other engines is that its syntax is trivial, it
				allows to reuse existing databases and Prolog code and it has every
				facility we need to represent real-world applications. Rfuzzy is a
				package of the <a href="http://ciaohome.org/">Ciao Prolog</a> logic
				programming environment.
			</li>
		</ul>
		<ul>
			<li>The web interface is a Java application that interprets the
				answers provided by the fuzzy framework. It runs on a Tomcat server
				behind an Apache proxy.</li>
		</ul>
	</div>

	<br />
	<br />
	<div class='indexPageSubTitle'>Technologies used</div>
	<div class='indexPageText'>
		FleSe core is developed as a <a href="http://ciao-lang.org/">Ciao
			Prolog</a> package, while the web interface is managed by a <a
			href="https://www.java.com/">Java</a> application running on an <a
			href="http://tomcat.apache.org/">Apache Tomcat</a> <a
			href="http://www.debian.org/">Debian</a> (Linux) server. The client
		part of the web interface is developed in <a
			href="http://en.wikipedia.org/wiki/HTML">HTML</a> and <a
			href="http://en.wikipedia.org/wiki/JavaScript">JavaScript</a> and
		uses <a href="http://en.wikipedia.org/wiki/Ajax_%28programming%29">Ajax</a>
		to improve the usability. Besides, we use some libraries developed by
		others. Mainly: <a href="http://code.google.com/p/socialauth/">SocialAuth</a>,
		<a href="https://code.google.com/p/openid4java/">OpenId4Java</a>, <a
			href="http://jquery.com/">jQuery</a>, <a href="https://jqueryui.com/">jQuery
			UI</a> and <a href="http://www.highcharts.com/">Highcharts JS</a>.
	</div>

	<br />
	<br />
	<div class='indexPageSubTitle'>Developers</div>
	<div class='indexPageText'>
		This application is being developed by <a
			href="http://babel.ls.fi.upm.es/~vpablos/">Ph.D. student Victor
			Pablos Ceruelo</a> under the supervision of <a
			href="http://babel.ls.fi.upm.es/~susana/">Dr. Susana Muñoz
			Hernandez</a>, as part of her research in the <a
			href="http://babel.ls.fi.upm.es/">Babel Research Group</a>.
	</div>

	<br />
	<br />
	<div class='indexPageSubTitle'>Support</div>
	<div class='indexPageText'>This work is partially supported by
		research projects DESAFIOS10 (TIN2009-14599-C03-00) funded by
		Ministerio Ciencia e Innovación of Spain, PROMETIDOS (P2009/TIC-1465)
		funded by Comunidad Autónoma de Madrid, MCYT project TIC2003-01036 and
		Research Staff Training Program (BES-2008-008320) funded by the
		Spanish Ministry of Science and Innovation. It is partially supported
		too by the Universidad Politécnica de Madrid entities Departamento de
		Lenguajes y Sistemas Informáticos e Ingeniería de Software and
		Facultad de Informática.</div>
	<br />
	<br />
	<br />
	<div class=indexPageImages>
		<a href="http://babel.ls.fi.upm.es/"> <img height="150"
			src="images/logo-babel.jpg" alt="logo babel research group"></img>
		</a> <a href="http://upm.es/"> <img height="150"
			src="images/logo-upm.jpg"
			alt="logo Universidad Politécnica de Madrid"></img>
		</a> <a href="http://www.fi.upm.es/"> <img height="150"
			src="images/logo-fi.jpg" alt="logo Facultad de Informática"></img>
		</a> <a href="http://www.dlsiis.fi.upm.es/"> <img height="150"
			src="images/logo-lsiis.jpeg"
			alt="logo Departamento de Lenguajes y Sistemas Informáticos e Ingeniería del Software"></img>
		</a> <a href="http://www.micinn.es/"> <img height="150"
			src="images/logo-micinn.png"
			alt="logo Ministerio de Ciencia e Innovación"></img>
		</a> <a href="http://www.madrid.org/"> <img height="150"
			src="images/logo-cm.gif" alt="logo Comunidad de Madrid"></img>
		</a> <a href="http://mcyt.es/"> <img height="150"
			src="images/logo-mcyt.gif"
			alt="logo Ministerio de Ciencia y Tecnología"></img>
		</a>

	</div>
</body>
</html>
