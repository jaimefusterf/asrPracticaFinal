package asr.proyectoFinal.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;

import asr.proyectoFinal.dao.VCAPHelper;

import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;

public class ReconocimientoImagenes{
	
	public static String reconocer(String image){
		String apiKey;
		
		apiKey = VCAPHelper.getLocalProperties("microservicios.properties").getProperty("reconocimientoImg_apiKey");
		IamOptions options = new IamOptions.Builder().apiKey(apiKey).build();
		VisualRecognition visualRecognition = new VisualRecognition("2019-04-11", options);
	
		ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
				.url(image)
				.build();
		ClassifiedImages result = visualRecognition.classify(classifyOptions).execute();
		System.out.println(result);
		

		String imagenJSON = result.toString();
		JsonParser parser = new JsonParser();
		JsonObject rootObj = parser.parse(imagenJSON).getAsJsonObject(); //Cogemos todo el JSON
		JsonArray imagenes = rootObj.getAsJsonArray("images");
		
		JsonObject obj2 = (JsonObject)imagenes.get(0);
		JsonArray clasificaciones = obj2.getAsJsonArray("classifiers");
		
		JsonObject obj3 = (JsonObject)clasificaciones.get(0);
		JsonArray clases = obj3.getAsJsonArray("classes");
		
		double score=0;
		String clase=image;
		for(int i=0; i<clases.size();i++)
		{
			double scoreResult=clases.get(i).getAsJsonObject().get("score").getAsDouble();
			if(scoreResult>score)
			{
				score=scoreResult;
				clase=clases.get(i).getAsJsonObject().get("class").getAsString();
			}
			
		}
			
		System.out.println(clase);
		
		
//		if(imagenes.size()>0)
//			imagenPrimera = imagenes.get(0).getAsJsonObject().get("classifiers").getAsString();
//		
//		System.out.println(imagenPrimera);
		return clase;
	}

}
