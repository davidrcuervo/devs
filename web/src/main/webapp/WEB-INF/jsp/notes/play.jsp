<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!doctype html>
<html lang="en">

	<head>
		<meta charset="utf-8">

		<title>${title == null ? notes.file.name : title}</title>

		<meta name="description" content="A framework for easily creating beautiful presentations using HTML">
		<meta name="author" content="Hakim El Hattab">

		<meta name="apple-mobile-web-app-capable" content="yes" />
		<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />

		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>


		<link rel="stylesheet" href="${page.rootUrl}/assets/reveal.js/css/reveal.css">
		<link rel="stylesheet" href="${page.rootUrl}/assets/reveal.js/css/theme/default.css" id="theme">
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
	

		<!-- For syntax highlighting -->
		<link rel="stylesheet" href="${page.rootUrl}/assets/reveal.js/lib/css/zenburn.css">
		
		<c:choose>
			<c:when test="${notes.format == 'pdf'}">
				<link rel="stylesheet" href="${page.rootUrl}/assets/reveal.js/css/print/pdf.css" type="text/css" media="print" />
			</c:when>
			<c:otherwise>
				<link rel="stylesheet" href="${page.rootUrl}/assets/reveal.js/css/print/paper.css" type="text/css" media="print" />
			</c:otherwise>
		</c:choose>

<%-- 
		<!-- If the query includes 'print-pdf', use the PDF print sheet -->
		<script>
			document.write( '<link rel="stylesheet" href="${page.rootUrl}/assets/reveal.js/css/print/' + ( window.location.search.match( /print-pdf/gi ) ? 'pdf' : 'paper' ) + '.css" type="text/css" media="print">' );
		</script>
--%>

		<!--[if lt IE 9]>
		<script src="${page.rootUrl}/assets/reveal.js/lib/js/html5shiv.js"></script>
		<![endif]-->
	</head>

	<body>

		<div class="reveal">

			<!-- Any section element inside of this container is displayed as a slide -->
			<div class="slides">${notes.print}</div>

		</div>

		<script src="${page.rootUrl}/assets/reveal.js/lib/js/head.min.js"></script>
		<script src="${page.rootUrl}/assets/reveal.js/js/reveal.min.js"></script>

		<script>

			// Full list of configuration options available here:
			// https://github.com/hakimel/reveal.js#configuration
			Reveal.initialize({
				controls: true,
				progress: true,
				history: true,

				theme: Reveal.getQueryHash().theme, // available themes are in /css/theme
				transition: Reveal.getQueryHash().transition || 'default', // default/cube/page/concave/zoom/linear/none

				// Optional libraries used to extend on reveal.js
				dependencies: [
					{ src: '${page.rootUrl}/assets/reveal.js/lib/js/classList.js', condition: function() { return !document.body.classList; } },
					{ src: '${page.rootUrl}/assets/reveal.js/plugin/markdown/showdown.js', condition: function() { return !!document.querySelector( '[data-markdown]' ); } },
					{ src: '${page.rootUrl}/assets/reveal.js/plugin/markdown/markdown.js', condition: function() { return !!document.querySelector( '[data-markdown]' ); } },
					{ src: '${page.rootUrl}/assets/reveal.js/plugin/highlight/highlight.js', async: true, callback: function() { hljs.initHighlightingOnLoad(); } },
					{ src: '${page.rootUrl}/assets/reveal.js/plugin/zoom-js/zoom.js', async: true, condition: function() { return !!document.body.classList; } },
					{ src: '${page.rootUrl}/assets/reveal.js/plugin/notes/notes.js', async: true, condition: function() { return !!document.body.classList; } }
				]
			});

		</script>

	</body>
</html>