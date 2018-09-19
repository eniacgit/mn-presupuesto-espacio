package org.camunda.bpm.menini_nicola.mn_proceso_espacio.accesoBD;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOPresupuesto;
import org.h2.command.Prepared;

public class AccesoBD {
	private String driver;
	private String username;
	private String pass;
	private String url;
	private String base;
	
	public Connection conectarBD() {
		// Carga los datos desde el archivo de configuracion
		// y se conecta a la base del servidor
		Connection con = null;		
		
		try {
			Properties p = new Properties();
			p.load(new FileInputStream("config/parametros.txt"));
			driver = p.getProperty("driver");
			Class.forName(driver);
			//System.out.println("driver: " + driver);
			
			username = p.getProperty("usuario");
			pass = p.getProperty("password");
			url = p.getProperty("url");
			base = p.getProperty("bdatos");
			
			con = DriverManager.getConnection(url + base, username, pass);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return con;
	}
	
	
	public void desconectarBD(Connection con) {
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void insertarCliente(String nombre, String email, String telefono, String celular) throws  SQLException, IOException {
		// Ingresa un nuevo cliente al sistema
		Connection con = this.conectarBD();
		Consultas consultas = new Consultas();
		String insert = consultas.insertarCliente();
		
		PreparedStatement pstmt = con.prepareStatement(insert);		
		pstmt.setString(1, nombre);
		pstmt.setString(2, email);
		pstmt.setString(3, telefono);
		pstmt.setString(4, celular);
		pstmt.executeUpdate();
		pstmt.close();
		this.desconectarBD(con);
	}
	
	public boolean existeCliente(String nombre) {
	// Retorna true si el nombre del cliente ya fue dado de alta
		boolean existeCliente = false;
		Connection con = con = this.conectarBD();
		Consultas consultas = new Consultas();
		
		String select = consultas.existeCliente();
		try {
			PreparedStatement pstmt = con.prepareStatement(select);
			pstmt.setString(1, nombre);
			
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				existeCliente = true;
			rs.close();
			pstmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		this.desconectarBD(con);
		return existeCliente;
	}
	
	public int obtenerIdCliente(String nombre) {
	// Retorna el id del cliente
	// Precondicion: el cliente existe en la base de datos
		int idCliente = 0;
		Connection con = con = this.conectarBD();
		Consultas consultas = new Consultas();
		String select = consultas.obtenerIdCliente();
		try {
			PreparedStatement pstmt = con.prepareStatement(select);
			pstmt.setString(1, nombre);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			idCliente = rs.getInt(1);
			rs.close();
			pstmt.close();			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.desconectarBD(con);
		return idCliente;
	}
	
	
	public void insertarPresupuesto(String cotizacion, String fecha, String moneda, float costo, String condicionesVenta, String descripcion) throws  SQLException, IOException {
		// Ingresa un nuevo cliente al sistema
		Connection con = this.conectarBD();
		Consultas consultas = new Consultas();
		String insert = consultas.insertarPresupuesto();
		
		PreparedStatement pstmt = con.prepareStatement(insert);		
		pstmt.setString(1, cotizacion);
		pstmt.setString(2, fecha);
		pstmt.setString(3, moneda);
		pstmt.setFloat(4, costo);
		pstmt.setString(5, condicionesVenta);
		pstmt.setString(6, descripcion);
		pstmt.executeUpdate();
		pstmt.close();
		this.desconectarBD(con);
	}
	
	
}