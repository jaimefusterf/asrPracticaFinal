<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Proyecto ASR new...</title>
</head>
<body>
	<h1>Ejemplo de Proyecto de ASR con Cloudant ahorita con DevOps</h1>
	<hr />
	<p>Opciones sobre la base de datos de Cloudant de Jaime Fúster</p>
	<ul>
		<li><a href="listar">Listar</a></li>
		<li><form method="POST" action="insertar" name="insertarPalabra">
				<label>Traducir palabra:</label><input type="text" name="palabra">
				<input type="submit" value="Insertar" name="insertar">
			</form></li>
	</ul>
</body>
</html>