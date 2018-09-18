package org.camunda.bpm.menini_nicola.mn_proceso_espacio.accesoBD;

public class Consultas {
	public String insertarCliente() {
		String consulta ="INSERT INTO mn_cliente (nombre, email, telefono, celular) VALUES (?,?,?,?);";
		return consulta;	
	}
	
	public String existeCliente() {
		String consulta = "SELECT nombre FROM mn_cliente WHERE nombre = ?;";
		return consulta;
	}
	
	public String obtenerIdCliente() {
		String consulta = "SELECT idCliente FROM mn_cliente WHERE nombre = ?;";
		return consulta;
	}
	
	public String insertarPresupuesto(){
		String consulta = "INSERT INTO mn_presupuesto (cotizacion,fecha,moneda,costo,condicionesVenta,descripcion) VALUES(?,?,?,?,?,?);";
		return consulta;
	}
}
