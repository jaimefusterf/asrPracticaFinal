package asr.proyectoFinal.servlets;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.nio.file.Files;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;

import asr.proyectoFinal.dao.CloudantPalabraStore;
import asr.proyectoFinal.dominio.Palabra;
import asr.proyectoFinal.services.ReconocimientoImagenes;
import asr.proyectoFinal.services.TextoAVoz;
import asr.proyectoFinal.services.Traductor;

/**
 * Servlet implementation class Controller
 */
@WebServlet(urlPatterns = {"/listar", "/insertar", "/hablar", "/images"})
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String result="";
	Palabra palabra = new Palabra();
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		PrintWriter out = response.getWriter();
		out.println("<html><head><meta charset=\"UTF-8\"></head><body>");
		
		CloudantPalabraStore store = new CloudantPalabraStore();
		System.out.println(request.getServletPath());
		switch(request.getServletPath())
		{
			case "/listar":
				if(store.getDB() == null)
					out.println("No hay DB");
				else
					//out.println("Palabras en la BD Cloudant:<br />" + store.getAll());
					request.setAttribute("almacenadas", store.getAll());
					request.getRequestDispatcher("jsp/WordsStoraged.jsp").forward(request, response);
				break;
				
			case "/hablar":
				TextoAVoz.hablar(palabra.getName(),response);
				
				break;
				
			case "/insertar":
				
				//String parametro = request.getParameter("palabra");
				String parametro=result;
				if(parametro==null)
				{
					out.println("usage: /insertar?palabra=palabra_a_traducir");
				}
				else
				{
					if(store.getDB() == null) 
					{
						out.println(String.format("Palabra: %s", palabra));
					}
					else
					{
						parametro = Traductor.translate(parametro, "en", "es", false);
						palabra.setName(parametro);
						store.persist(palabra);
						request.setAttribute("traduccion", palabra.getName());
						request.getRequestDispatcher("jsp/WordTranslated.jsp").forward(request, response);
					    //out.println(String.format("Almacenada la palabra: %s", palabra.getName()));			    	  
					}
				}
				break;
		}
		out.println("</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//doGet(request, response);
		
		//PrintWriter out = response.getWriter();
		//out.println("<html><head><meta charset=\"UTF-8\"></head><body>");
		
		String image=request.getParameter("image");
		result=ReconocimientoImagenes.reconocer(image);
		System.out.println(result);
		request.setAttribute("resultado", result);
		request.getRequestDispatcher("jsp/ImageRecognized.jsp").forward(request, response);
		//out.println(String.format("La imagen es de: %s", result));	
		//out.println("</html>");
	}

}
