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
	
	public String obtenerIdPresupuesto() {
		String consulta = "SELECT idPresupuesto FROM mn_presupuesto WHERE cotizacion = ?;";
		return consulta;
	}
	
	public String insertarClientePresupuesto() {
		String consulta ="INSERT INTO mn_cliente_presupuesto (estado, idCliente, idPresupuesto) VALUES (?,?,?);";
		return consulta;	
	}
	
	public String insertarEspacio() {
		String consulta ="INSERT INTO mn_espacio (cronograma, validez, idPresupuesto) VALUES (?,?,?);";
		return consulta;	
	}
	
	public String existeNroCotización() {
		String consulta ="select cotizacion from mn_presupuesto where cotizacion=?";
		return consulta;
	}
	
	public String obtenerRemitente() {
		String consulta ="select email from mn_email where id=?;";
		return consulta;
	}
	
	public String obtenerPasswordRemitente() {
		String consulta ="select password from mn_email where id=?;";
		return consulta;
	}
	
}
