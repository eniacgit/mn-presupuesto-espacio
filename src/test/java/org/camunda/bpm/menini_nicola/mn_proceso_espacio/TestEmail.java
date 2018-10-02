package org.camunda.bpm.menini_nicola.mn_proceso_espacio;

import java.util.ArrayList;

import javax.mail.MessagingException;

import org.camunda.bpm.menini_nicola.mn_proceso_espacio.accesoBD.AccesoBD;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.fachada.Fachada;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOArchivoAdjunto;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOEmail;

public class TestEmail {

	public static void main(String[] args) {
		AccesoBD a = new AccesoBD();
		System.out.println("email: " + a.obtenerRemitente());
		System.out.println("password: " + a.obtenerPasswordRemitente());
		
		VOArchivoAdjunto arch1 = new VOArchivoAdjunto();
		arch1.setRutaArchivoAdjunto("//home//danielo//");
		arch1.setNombreArchivoAdjunto("libro.pdf");
		
		VOArchivoAdjunto arch2 = new VOArchivoAdjunto();
		arch2.setRutaArchivoAdjunto("//home//danielo//");
		arch2.setNombreArchivoAdjunto("Test.java");
		
		ArrayList<VOArchivoAdjunto> lstArchivosAdjuntos = new ArrayList<VOArchivoAdjunto>();
		
		lstArchivosAdjuntos.add(arch1);
		lstArchivosAdjuntos.add(arch2);
		
		
		VOEmail voEmail = new VOEmail();
		voEmail.setDestinatario("deleon.danielo@gmail.com");
		voEmail.setAsunto("Probando mandador de mail");
		voEmail.setCuerpo("Hola,\n Este es un mail de prueba para corroborar que llegan los archivos adjuntos.\nSalu2!");
		voEmail.setLstArchivosAdjuntos(lstArchivosAdjuntos);
		
		try {
			Fachada f = new Fachada();			
			f.enviarConGmail(voEmail);
			System.out.println("Correo enviado con éxito!!");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
