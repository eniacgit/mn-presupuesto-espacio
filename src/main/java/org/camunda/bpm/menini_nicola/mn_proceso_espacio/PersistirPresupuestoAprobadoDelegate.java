package org.camunda.bpm.menini_nicola.mn_proceso_espacio;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.fachada.Fachada;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.modelo.Cliente;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOCliente;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOClientePresupuesto;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOEspacio;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOPresupuesto;

public class PersistirPresupuestoAprobadoDelegate implements JavaDelegate{
	
	private final static Logger LOGGER = Logger.getLogger("PERSISTIR-PRESUPUESTO");

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		
		String destinatarioIn= (String)execution.getVariable("EMAIL");	 	 
		//traer datos del cliente		
		Cliente cliente= new Cliente();
		cliente = (Cliente)execution.getVariable("cliente");
		
		// Persistencia de datos del cliente
		
		
//		 String cliente = (String) execution.getVariable("CLIENTE");
//		 String email = destinatarioIn;
//		 String tel = (String) execution.getVariable("TEL");
//		 String celular = (String) execution.getVariable("CELULAR");
		 
		 VOCliente voCliente = new VOCliente();
		 voCliente.setNombre(cliente.getNombre());
		 voCliente.setEmail(cliente.getEmail());
		 voCliente.setTelefono(cliente.getTelefono());
		 voCliente.setCelular(cliente.getCelular());
		 voCliente.setTipo(cliente.getTipo());
		 voCliente.setRut(cliente.getRut());
		 voCliente.setRazonSocial(cliente.getRazonSocial());
		 voCliente.setDireccion(cliente.getDireccion());	
		 
//		 voCliente.setNombre(cliente);
//		 voCliente.setEmail(email);
//		 voCliente.setTelefono(tel);
//		 voCliente.setCelular(celular);
		 
		 Fachada fachada = new Fachada();
		 if (!fachada.existeCliente(voCliente.getNombre())) {
			 fachada.insertarCliente(voCliente);
		 }
		 
		 String cotizacion = (String) execution.getVariable("COTIZACION");	 
		 String moneda = (String) execution.getVariable("moneda");
		 
		 if (moneda.equals("dolares"))
			 moneda ="USD";
		 else
			 moneda ="$U";
		 
		 String costo = (String) execution.getVariable("COSTO");
		 byte estado = 1; // Estados: 0 (no aprobado, 1 aprobado)
		 String cronograma = (String) execution.getVariable("cronograma");	 
		 String condicionesVenta = (String) execution.getVariable("condiciones");
		 
		 String descripcion = (String) execution.getVariable("descripcion");	 
		 //to do: generar texto para las condiciones y agregarle la validez en dias obtenido
		  //en el start-form.html
		 
		 // Persistencia de datos del presupuesto
		 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		 Date date = new Date();
		 
		 VOPresupuesto presupuesto = new VOPresupuesto();
		 presupuesto.setCotizacion(cotizacion); 
		 
		 String fecha =dateFormat.format(date);	 
		 presupuesto.setFecha(fecha);
		 presupuesto.setMoneda(moneda);
		 presupuesto.setCosto(Float.parseFloat(costo));
		 presupuesto.setCondicionesVenta(condicionesVenta);
		 presupuesto.setDescripcion(descripcion);
		 fachada.insertarPresupuesto(presupuesto);
		 
		 int idPresupuesto = fachada.obtenerIdPresupuesto(presupuesto.getCotizacion());
		 int idCliente = fachada.obtenerIdCliente(voCliente.getNombre());
		 
		 VOClientePresupuesto clientePresupuesto = new VOClientePresupuesto();
		 clientePresupuesto.setEstado(estado);
		 clientePresupuesto.setIdCliente(idCliente);
		 clientePresupuesto.setIdPresupuesto(idPresupuesto);
		 
		 fachada.insertarClientePresupuesto(clientePresupuesto);
		 
		VOEspacio espacio = new VOEspacio();
		espacio.setCronograma(cronograma);
		espacio.setValidez(30); // ver mejor despues
		espacio.setIdPresupuesto(idPresupuesto);
		
		fachada.insertarEspacio(espacio);
		
		
	}

}
