package org.camunda.bpm.menini_nicola.mn_proceso_espacio;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.fachada.Fachada;

public class AutogenerarPresupuestoDelegate implements JavaDelegate{

	private final static Logger LOGGER = Logger.getLogger("AUTOGENERAR-COTIZACION");
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		Fachada f = new Fachada();
		String nroCotizacion = f.generarNroCotizaciónFechaActual();
		execution.setVariable("cotizacion",nroCotizacion);		

		String validez = (String) execution.getVariable("validez");
		execution.setVariable("condiciones", "Esta cotización tiene una validez es de " + validez + " días corridos a partir de la fecha que figura en el cabezal\n" +
		 "El precio NO incluye IVA. Forma de pago: 100% al aceptar la cotización");
	}

}

