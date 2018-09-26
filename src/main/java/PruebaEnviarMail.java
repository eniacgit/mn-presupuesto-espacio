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

import org.camunda.bpm.engine.impl.core.model.Properties;

public class PruebaEnviarMail {
	
	private static String remitente ="camunda.forever"; 

	private static void enviarConGmail(String destinatario, String asunto, String cuerpo, String filename) {
		// Se obtiene el objeto Session. La configuración es para una cuenta de gmail
		java.util.Properties props = System.getProperties();
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
			//message.setText(cuerpo);
			
			
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(cuerpo);
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			messageBodyPart = new MimeBodyPart();
			DataSource source =new FileDataSource(filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(filename);
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			
			

			Transport transport = session.getTransport("smtp");
			transport.connect("smtp.gmail.com", remitente, "bolimar2018");
			transport.sendMessage(message, message.getAllRecipients());
			System.out.println("Mail enviado con exito!");			
			transport.close();

		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		String filename = "/home/danielo/Escritorio/prueba.pdf";
		
		String destinatarioIn= "deleon.danielo@gmail.com";
		 String destinatario = destinatarioIn;
		 String asunto ="Correo de prueba enviado desde proceso en camunda mediante Java";
		 String cuerpo = "Esta es una prueba de correo, y si lo estas viendo que es que quedó resuelto como mandar mails desde camunda...";
		 enviarConGmail(destinatario, asunto, cuerpo, filename);
		

	}

}
