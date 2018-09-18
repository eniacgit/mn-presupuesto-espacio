package org.camunda.bpm.menini_nicola.mn_proceso_espacio;

import java.io.IOException;
import java.sql.SQLException;

import org.camunda.bpm.menini_nicola.mn_proceso_espacio.fachada.Fachada;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOCliente;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOPresupuesto;

public class TestAccesoBD {

	public static void main(String[] args) throws SQLException, IOException {
		Fachada f = new Fachada();
		VOCliente cliente = new VOCliente();
		cliente.setNombre("Juan Perez");
		cliente.setEmail("jperez@gmail.com");
		cliente.setTelefono("26918787");
		cliente.setCelular("094654123");
		
		if (!f.existeCliente(cliente.getNombre()))	{	
			f.insertarCliente(cliente);
			System.out.println("Cliente insertado con exito!");
		}else 
			System.out.println("El cliente ya existe en la base de datos!");
	
		// obtengo idCliente
		System.out.println("idCliente: " + f.obtenerIdCliente(cliente.getNombre()));	
	
		VOPresupuesto presupuesto = new VOPresupuesto();
		presupuesto.setCotizacion("180918-01");
		presupuesto.setFecha("18-09-18");
		presupuesto.setMoneda("$");
		presupuesto.setCondicionesVenta("bla bla bla");
		presupuesto.setDescripcion("aaa aaaa aaaa aaa");
		
		f.insertarPresupuesto(presupuesto);
		
	
	}
	
	
	


}
