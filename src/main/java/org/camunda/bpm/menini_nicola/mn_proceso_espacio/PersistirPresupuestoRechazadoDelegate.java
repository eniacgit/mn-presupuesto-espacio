package org.camunda.bpm.menini_nicola.mn_proceso_espacio;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.fachada.Fachada;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOCliente;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOClientePresupuesto;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOEspacio;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOPresupuesto;

public class PersistirPresupuestoRechazadoDelegate implements JavaDelegate{
	
	private final static Logger LOGGER = Logger.getLogger("PERSISTIR-PRESUPUESTO-RECHAZADO");
	
	private static void moverArchivo(String origen, String destino) {
		Path origenPath = FileSystems.getDefault().getPath(origen);
		Path destinoPath = FileSystems.getDefault().getPath(destino);
		
		try {
			Files.move(origenPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		
		String destinatarioIn= (String)execution.getVariable("email");	 	 
		 // Persistencia de datos del cliente
		 String cliente = (String) execution.getVariable("cliente");
		 String email = destinatarioIn;
		 String tel = (String) execution.getVariable("tel");
		 String celular = (String) execution.getVariable("celular");
		 
		 VOCliente voCliente = new VOCliente();
		 voCliente.setNombre(cliente);
		 voCliente.setEmail(email);
		 voCliente.setTelefono(tel);
		 voCliente.setCelular(celular);
		 
		 Fachada fachada = new Fachada();
		 if (!fachada.existeCliente(cliente)) {
			 fachada.insertarCliente(voCliente);
		 }
		 
		 String cotizacion = (String) execution.getVariable("cotizacion");	 
		 String moneda = (String) execution.getVariable("moneda");
		 
		 if (moneda.equals("dolares"))
			 moneda ="USD";
		 else
			 moneda ="$U";
		 
		 String costo = (String) execution.getVariable("costo");
		 byte estado = 0; // Estados: 0 (no aprobado, 1 aprobado)
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
		
		// muevo reportePDF y cronograma a carpeta RECHAZADOS		
		String rutaArchivos = (String)execution.getVariable("rutaReportePDF");
		String nombreReportePDF = (String)execution.getVariable("nombreReportePDF");
		String nombreCronogramaPDF = (String)execution.getVariable("nombreCronogramaPDF");
		
		moverArchivo(rutaArchivos + nombreReportePDF, rutaArchivos+"/RECHAZADOS/" + nombreReportePDF);
		moverArchivo(rutaArchivos + nombreCronogramaPDF, rutaArchivos+"/RECHAZADOS/" + nombreCronogramaPDF);
		
			
	}

}
