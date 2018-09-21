package org.camunda.bpm.menini_nicola.mn_proceso_espacio.fachada;

import java.io.IOException;
import java.sql.SQLException;

import org.camunda.bpm.menini_nicola.mn_proceso_espacio.accesoBD.AccesoBD;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOCliente;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOClientePresupuesto;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOEspacio;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOPresupuesto;

public class Fachada {
	
	public void insertarCliente(VOCliente cliente) throws SQLException, IOException {
		AccesoBD accesoBD = new AccesoBD();
		String nombre = cliente.getNombre();
		String email = cliente.getEmail();
		String telefono = cliente.getTelefono();
		String celular = cliente.getCelular();
		accesoBD.insertarCliente(nombre, email, telefono, celular);
	}
	
	public boolean existeCliente(String nombre) {
		AccesoBD accesoBD = new AccesoBD();
		return accesoBD.existeCliente(nombre);
	}
	
	public int obtenerIdCliente(String nombre) {
		AccesoBD accesoBD = new AccesoBD();
		return accesoBD.obtenerIdCliente(nombre);
	}
	
	public void insertarPresupuesto(VOPresupuesto presupuesto) throws SQLException, IOException {
		AccesoBD accesoBD = new AccesoBD();
		String cotizacion = presupuesto.getCotizacion();
		String fecha = presupuesto.getFecha();
		String moneda = presupuesto.getMoneda();
		float costo = presupuesto.getCosto();
		String condicionesVenta = presupuesto.getCondicionesVenta();
		String descripcion = presupuesto.getDescripcion();
		accesoBD.insertarPresupuesto(cotizacion, fecha, moneda, costo,condicionesVenta, descripcion);
	}
	
	public int obtenerIdPresupuesto(String cotizacion) {
		AccesoBD accesoBD = new AccesoBD();
		return accesoBD.obtenerIdPresupuesto(cotizacion);
	}
	
	public void insertarClientePresupuesto(VOClientePresupuesto clientePresupuesto) throws  SQLException, IOException {
		int idClientePresupuesto = clientePresupuesto.getIdClientePresupuesto();
		byte estado = clientePresupuesto.getEstado();
		int idCliente = clientePresupuesto.getIdCliente();
		int idPresupuesto = clientePresupuesto.getIdPresupuesto();
		
		AccesoBD accesoBD = new AccesoBD();
		accesoBD.insertarClientePresupuesto(estado, idCliente, idPresupuesto);
	}
	
	public void insertarEspacio(VOEspacio espacio) throws  SQLException, IOException {
		String cronograma = espacio.getCronograma();
		int validez = espacio.getValidez();
		int idPresupuesto = espacio.getIdPresupuesto();
		
		AccesoBD accesoBD = new AccesoBD();
		accesoBD.insertarEspacio(cronograma, validez, idPresupuesto);		
	}
	
}
