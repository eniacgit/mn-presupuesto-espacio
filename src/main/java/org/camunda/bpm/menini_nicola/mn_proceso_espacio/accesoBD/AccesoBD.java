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
	
	public void insertarCliente(String nombre, String email, String telefono, String celular,String rut,String razonSocial,String tipo, String direccion) throws  SQLException, IOException {
		// Ingresa un nuevo cliente al sistema
		Connection con = this.conectarBD();
		Consultas consultas = new Consultas();
		String insert = consultas.insertarCliente();
		
		PreparedStatement pstmt = con.prepareStatement(insert);		
		pstmt.setString(1, nombre);
		pstmt.setString(2, email);
		pstmt.setString(3, telefono);
		pstmt.setString(4, celular);
		pstmt.setString(5, rut);
		pstmt.setString(6, razonSocial);
		pstmt.setString(7, tipo);
		pstmt.setString(8, direccion);
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
	
	public int obtenerIdPresupuesto(String cotizacion) {
		// Retorna el id del presupuesto
		// Precondicion: el presupuesto existe en la base de datos
			int idCliente = 0;
			Connection con = this.conectarBD();
			Consultas consultas = new Consultas();
			String select = consultas.obtenerIdPresupuesto();
			try {
				PreparedStatement pstmt = con.prepareStatement(select);
				pstmt.setString(1, cotizacion);
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
	
	public void insertarClientePresupuesto(byte estado, int idCliente, int idPresupuesto) throws  SQLException, IOException {
		// Ingresa un nuevo cliente-presupuesto al sistema
		Connection con = this.conectarBD();
		Consultas consultas = new Consultas();
		String insert = consultas.insertarClientePresupuesto();
		
		PreparedStatement pstmt = con.prepareStatement(insert);		
		pstmt.setByte(1, estado);
		pstmt.setInt(2, idCliente);
		pstmt.setInt(3, idPresupuesto);		
		pstmt.executeUpdate();
		pstmt.close();
		this.desconectarBD(con);
	}
	
	public void insertarEspacio(String cronograma, int validez, int idPresupuesto) throws  SQLException, IOException {
		// Ingresa un nuevo cliente-presupuesto al sistema
		Connection con = this.conectarBD();
		Consultas consultas = new Consultas();
		String insert = consultas.insertarEspacio();
		
		PreparedStatement pstmt = con.prepareStatement(insert);		
		pstmt.setString(1, cronograma);
		pstmt.setInt(2, validez);
		pstmt.setInt(3, idPresupuesto);		
		pstmt.executeUpdate();
		pstmt.close();
		this.desconectarBD(con);
	}
	
		
	public boolean existeNroCotización(String nroCotizacion) {
		// Retorna true si el nro de la cotizacion ya existe en la bd
			boolean existeCliente = false;
			Connection con = con = this.conectarBD();
			Consultas consultas = new Consultas();
			
			String select = consultas.existeNroCotización();
			try {
				PreparedStatement pstmt = con.prepareStatement(select);
				pstmt.setString(1, nroCotizacion);
				
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
		
	public String obtenerRemitente() {
		// Retorna el mail con el que se enviarán los presupuestos 
		// Precondicion: en la tabla mn_email solo existe una tupla con id=1
			int id = 1;
			String remitente="";
			Connection con = this.conectarBD();
			Consultas consultas = new Consultas();
			String select = consultas.obtenerRemitente();
			try {
				PreparedStatement pstmt = con.prepareStatement(select);
				pstmt.setInt(1, id);
				ResultSet rs = pstmt.executeQuery();
				rs.next();
				remitente = rs.getString(1);
				rs.close();
				pstmt.close();			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.desconectarBD(con);
			return remitente;
		}
	
	public String obtenerPasswordRemitente() {
		// Retorna el password del remitente con el que se enviarán los presupuestos 
		// Precondicion: en la tabla mn_email solo existe una tupla con id=1
		int id = 1;
		String password="";
		Connection con = this.conectarBD();
		Consultas consultas = new Consultas();
		String select = consultas.obtenerPasswordRemitente();
		try {
			PreparedStatement pstmt = con.prepareStatement(select);
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			password = rs.getString(1);
			rs.close();
			pstmt.close();			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.desconectarBD(con);
		return password;
	}
}
