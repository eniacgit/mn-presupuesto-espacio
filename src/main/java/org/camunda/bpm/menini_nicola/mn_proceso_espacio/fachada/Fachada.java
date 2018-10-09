package org.camunda.bpm.menini_nicola.mn_proceso_espacio.fachada;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.camunda.bpm.menini_nicola.mn_proceso_espacio.accesoBD.AccesoBD;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOArchivoAdjunto;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOCliente;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOClientePresupuesto;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOEmail;
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
	
	public String generarNroCotizaciónFechaActual() {
		// A partir de la fecha actual genera un nro de cotizacion
		// Si ya hay una cotizcion para el dia actual incremente el digito del indice
		// Ejemplo: Si ya existe la cotizacion 180924-01,la siguiente sera 180924-02 
		
		DateFormat dateFormat = new SimpleDateFormat("yyMMdd");
		Calendar cal = Calendar.getInstance();
		
		// Obtengo nro de cotizacion del dia actual
		String fecha = dateFormat.format(cal.getTime());
		String nroCotizacion = fecha  + "-01";
		int cont=1;
		AccesoBD accesoBD = new AccesoBD();
		while (accesoBD.existeNroCotización(nroCotizacion)) {
			cont++;
			nroCotizacion = fecha + "-" + String.format("%02d",cont);
		}		
		return nroCotizacion;
	}
	
	private static void addAtachment(Multipart multipart, String rutaArcvhivo, String nombreArchivo) throws MessagingException {
		DataSource source = new FileDataSource(rutaArcvhivo+nombreArchivo);
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setDataHandler(new DataHandler(source));
		messageBodyPart.setFileName(nombreArchivo);
		multipart.addBodyPart(messageBodyPart);
	}
	
	public void enviarConGmail (VOEmail voEmail) throws MessagingException {
	// Envia un correo electronico con archivos adjuntos (ArrayList) utilizando el email y contrasenia
	// almacenados en la tabla mn_email
		//String remitente = voEmail.getRemitente(); // este campo viene vacio
		String destinatario = voEmail.getDestinatario();
		String asunto = voEmail.getAsunto();
		String cuerpo = voEmail.getCuerpo();
		ArrayList<VOArchivoAdjunto> lstArchivosAdjuntos = voEmail.getLstArchivosAdjuntos();
		
		AccesoBD accesoBD = new AccesoBD();
		String email = accesoBD.obtenerRemitente();
		String [] arrayEmail = email.split("@");
		String remitente = arrayEmail[0];
		String clave = accesoBD.obtenerPasswordRemitente();
		
		
		// Se obtiene el objeto Session. La configuración es para una cuenta de gmail
		Properties props = new Properties();		
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.user", "remitente");
		props.put("mail.smtp.clave", clave);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.port", "587");
		
		Session session = Session.getDefaultInstance(props);
		MimeMessage message = new MimeMessage(session);
		message.addRecipients(Message.RecipientType.TO, destinatario);
		message.setSubject(asunto);
		
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setText(cuerpo);
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
		messageBodyPart = new MimeBodyPart();
		
		// obtengo archivos adjuntos de la lista
		for (int i=0; i<lstArchivosAdjuntos.size();i++) {
			String rutaArchivo = lstArchivosAdjuntos.get(i).getRutaArchivoAdjunto();
			String nombreArchivo = lstArchivosAdjuntos.get(i).getNombreArchivoAdjunto();
			addAtachment(multipart, rutaArchivo, nombreArchivo);
		}
		message.setContent(multipart);
		
		Transport transport = session.getTransport("smtp");
		transport.connect("smtp.gmail.com", remitente, clave);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();		
	}
	
	
	// no sirve :(
	public void enviarConGmailProgramado (VOEmail voEmail, long delay) {
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				try {
					enviarConGmail(voEmail);
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}			
		};
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		long period = delay;
		executor.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS);
		try {
			Thread.sleep(period);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		executor.shutdown();
	}
}
