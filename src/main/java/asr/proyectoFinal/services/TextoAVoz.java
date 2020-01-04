package asr.proyectoFinal.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.*;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.SynthesizeOptions;
import com.ibm.watson.developer_cloud.text_to_speech.v1.util.WaveUtils;
import com.ibm.watson.developer_cloud.text_to_speech.v1.websocket.BaseSynthesizeCallback;

import asr.proyectoFinal.dao.VCAPHelper;

public class TextoAVoz {
	public static void hablar(String texto,HttpServletResponse response) {
		String apiKey;
		
		apiKey = VCAPHelper.getLocalProperties("microservicios.properties").getProperty("textoVoz_apiKey");
		
		IamOptions options = new IamOptions.Builder().apiKey(apiKey).build();

		TextToSpeech textToSpeech = new TextToSpeech(options);
		textToSpeech.setEndPoint("https://gateway-lon.watsonplatform.net/text-to-speech/api");

		try {
			SynthesizeOptions synthesizeOptions = new SynthesizeOptions
					.Builder()
					.text(texto)
					.accept("audio/webm")
					.voice("es-ES_LauraVoice")
					.build();

			InputStream inputStream = textToSpeech.synthesize(synthesizeOptions).execute();//.getResult();
			//InputStream in = WaveUtils.reWriteWaveHeader(inputStream);
			response.reset();
			OutputStream out = response.getOutputStream();
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			out.close();
			//in.close();
			inputStream.close();
						
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ;
	}
}
