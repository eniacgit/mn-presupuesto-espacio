package org.camunda.bpm.menini_nicola.mn_proceso_espacio;

import java.awt.List;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.fachada.Fachada;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOCliente;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOClientePresupuesto;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOEspacio;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOPresupuesto;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EnviarPresupuestoDelegate implements JavaDelegate {

	private final static Logger LOGGER = Logger.getLogger("ENVIAR-PRESUPUESTO");

	//remitente por defecto: camunda.forever@gmail.com
	private static String remitente ="camunda.forever"; 
	
	private static void enviarConGmail(String destinatario, String asunto, String cuerpo) {

		Properties props = System.getProperties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.user", "remitente");
		props.put("mail.smtp.clave", "bolimar2018");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.port", "587");

		Session session = Session.getDefaultInstance(props);
		MimeMessage message = new MimeMessage(session);

		try {			
			message.setFrom(new InternetAddress(remitente));
			message.addRecipients(Message.RecipientType.TO, destinatario);
			message.setSubject(asunto);
			message.setText(cuerpo);
			Transport transport = session.getTransport("smtp");
			transport.connect("smtp.gmail.com", remitente, "bolimar2018");
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();

		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
	 // TODO Auto-generated method stub
	 LOGGER.info("Solicitud de procesamiento por '" + execution.getVariable("customerId") + "'...");		
			
	 String destinatarioIn= (String)execution.getVariable("email");
	 String destinatario = destinatarioIn;
	 String asunto ="Correo de prueba enviado desde proceso en camunda mediante Java";
	 String cuerpo = "Esta es una prueba de correo, y si lo estas viendo que es que quedó resuelto como mandar mails desde camunda...";
	 enviarConGmail(destinatario, asunto, cuerpo);
	 
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
	 String moneda = (String) execution.getVariable("moneda"); // hay que probar
	 String costo = (String) execution.getVariable("costo");
	 byte estado = 1; // Estados: 0 (no aprobado, 1 aprobado)
	 String cronograma = (String) execution.getVariable("cronograma");	 
	 String condicionesVenta = "bla bla bla";
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
	
	// Creación de presupuesto espacio
	HashMap parametros = new HashMap<String, Object>();
	parametros.put("cotizacion", cotizacion);
	parametros.put("cliente", cliente);
	parametros.put("email", email);
	parametros.put("descripcion", descripcion);
	parametros.put("moneda", moneda);
	parametros.put("costo", costo);
	parametros.put("condiciones", condicionesVenta);
	
	
	String path = System.getProperty("user.dir");
	LOGGER.info("Ruta donde estoy parado '" + path);
	
	
	
	FileInputStream fis;
	try {
		fis = new FileInputStream("reportes//jasper//presupuestoEspacio.jasper");
		BufferedInputStream bufferedInputStream = new BufferedInputStream(fis);
		
		//Load bufferedInputStream file.jasper 
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(bufferedInputStream); 
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parametros,new JREmptyDataSource());
		JasperExportManager.exportReportToPdfFile(jasperPrint,path + "presupuestoEspacio.pdf");
		JasperViewer.viewReport(jasperPrint, false);
	} catch (FileNotFoundException | JRException e) {
		e.printStackTrace();
	}
	
	}

}
