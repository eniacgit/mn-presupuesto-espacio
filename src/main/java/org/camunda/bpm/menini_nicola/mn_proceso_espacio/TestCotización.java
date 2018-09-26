package org.camunda.bpm.menini_nicola.mn_proceso_espacio;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.camunda.bpm.menini_nicola.mn_proceso_espacio.fachada.Fachada;

public class TestCotización {

	public static void main(String[] args) {
		/*DateFormat dateFormat = new SimpleDateFormat("yyMMdd");
		Calendar cal = Calendar.getInstance();
		System.out.println(dateFormat.format(cal.getTime()));
		*/
	/*	
		int mes=202;
		String valor = String.format("%02d",mes); //regresa la cadena ’01’
		
		System.out.println(valor);
	 */
		
		Fachada f = new Fachada();
		System.out.println(f.generarNroCotizaciónFechaActual());
		
		
		
	}

}
