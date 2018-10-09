package org.camunda.bpm.menini_nicola.mn_proceso_espacio;

import java.awt.List;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.fachada.Fachada;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOArchivoAdjunto;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOCliente;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOClientePresupuesto;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOEmail;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOEspacio;
import org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects.VOPresupuesto;

import com.lowagie.text.pdf.PdfWriter;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.view.JasperViewer;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EnviarPresupuestoDelegate implements JavaDelegate {

	private final static Logger LOGGER = Logger.getLogger("ENVIAR-PRESUPUESTO");

	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
	 LOGGER.info("Solicitud de procesamiento por '" + execution.getVariable("customerId") + "'...");		
	
	 
	 String cotizacion = (String) execution.getVariable("cotizacion");
	 String cliente = (String) execution.getVariable("cliente");
	 String destinatarioIn= (String)execution.getVariable("email");
	 String email = destinatarioIn;
	 String descripcion = (String) execution.getVariable("descripcion");
	 String moneda = (String) execution.getVariable("moneda");
	 
	 if (moneda.equals("dolares"))
		 moneda ="USD";
	 else
		 moneda ="$U";
	 String costo = (String) execution.getVariable("costo");
	 String condicionesVenta = (String) execution.getVariable("condiciones");
	 
	 
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
	LOGGER.info("RUTA DONDE ESTOY PARADO: " + path);	
	
	FileInputStream fis;
	try {
		fis = new FileInputStream("reportes//jasper//presupuestoEspacio.jasper");
		BufferedInputStream bufferedInputStream = new BufferedInputStream(fis);
		
		//Load bufferedInputStream file.jasper 
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(bufferedInputStream); 
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parametros,new JREmptyDataSource());
		
		Properties p = new Properties();
		p.load(new FileInputStream("config/parametros.txt"));
		String rutaArchivoAdjunto = p.getProperty("carpeta_reportes");
				
		// Se crae el archivo pd con el nombre:
		// Ejemplo: Cotizacion_ESPACIO_180926-01_Fernando_Pelaez.pdf
		String nombreArchivoAdjunto="Cotizacion_ESPACIO_" + cotizacion + "_" + cliente.replace(' ' , '_') +".pdf" ;

		//String rutaArchivoAdjunto = "//home//danielo//";
		//JasperExportManager.exportReportToPdfFile(jasperPrint,"//home//danielo//presupuestoEspacio.pdf");
		JasperExportManager.exportReportToPdfFile(jasperPrint,rutaArchivoAdjunto + nombreArchivoAdjunto);
		//JasperViewer.viewReport(jasperPrint, false);
		
		String destinatario = destinatarioIn;		
		String nombreCronograma = "Cronograma_" + cliente.replace(' ' , '_') + ".pdf";
		
		/* En Camunda los archivos se almacenan como una variable de instancia de proceso del tipo Bytes.
		 * Para almacenar ese archivo, primero hay que tranformar dicha instancia con el siguiente código:		 
		 */		
		/////////////////////////////////////////////////////////////////////////////////////////////////
		InputStream cronogramaAdjunto = (ByteArrayInputStream)execution.getVariable("INVOICE_DOCUMENT");
		File newFile = new File(rutaArchivoAdjunto + nombreCronograma);
		FileOutputStream fos = new FileOutputStream(newFile);
		int data;
		while ((data=cronogramaAdjunto.read())!=-1){
			char ch = (char)data;
			fos.write(ch);			
		}
		fos.flush();
		fos.close();		
		/////////////////////////////////////////////////////////////////////////////////////////////////
		
		// REPORTE PDF
		VOArchivoAdjunto arch1 = new VOArchivoAdjunto();
		arch1.setRutaArchivoAdjunto(rutaArchivoAdjunto);
		arch1.setNombreArchivoAdjunto(nombreArchivoAdjunto);
		
		// CRONOGRAMA PDF
		VOArchivoAdjunto arch2 = new VOArchivoAdjunto();
		arch2.setRutaArchivoAdjunto(rutaArchivoAdjunto);
		arch2.setNombreArchivoAdjunto(nombreCronograma);
		
		// ArrayList de archivos adjuntos (reporte pdf, cronograma pdf)
		ArrayList<VOArchivoAdjunto> lstArchivosAdjuntos = new ArrayList<VOArchivoAdjunto>();
		lstArchivosAdjuntos.add(arch1);
		lstArchivosAdjuntos.add(arch2);
		
		VOEmail voEmail = new VOEmail();
		voEmail.setDestinatario(destinatario);
		voEmail.setAsunto("Correo de prueba enviado desde proceso en camunda mediante Java");
		voEmail.setCuerpo("Esta es una prueba de correo, y si lo estas viendo que es que quedó resuelto como mandar mails desde camunda...");
		voEmail.setLstArchivosAdjuntos(lstArchivosAdjuntos);
		
		Fachada f = new Fachada();
		f.enviarConGmail(voEmail);
		//enviarConGmail(destinatario, asunto, cuerpo,rutaArchivoAdjunto,nombreArchivoAdjunto, "//home//danielo//","libro.pdf");
		
		
		// se envia mail recordatorio a los 3 dias -- NO FUNCIONÓ :(
	/*	VOEmail voEmail2 = new VOEmail();
		voEmail2.setAsunto("Email recordatorio");
		voEmail2.setDestinatario("deleon.danielo@gmail.com");
		voEmail2.setRemitente("deleon.danielo@gmail.com");
		voEmail2.setCuerpo("Este email se envía como aviso de que hace 3 dias que el cliente no ha dado respuesta");
		long delay = 10000L;
		f.enviarConGmailProgramado(voEmail2, delay);
		*/
	} catch (FileNotFoundException | JRException e) {
		e.printStackTrace();
	}
	
	}

}
