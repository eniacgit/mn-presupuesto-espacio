package org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects;

import java.util.ArrayList;

public class VOEmail {
	private String remitente;
	private String destinatario;
	private String asunto;
	private String cuerpo;
	private ArrayList<VOArchivoAdjunto> lstArchivosAdjuntos;
	
	public VOEmail() {
		super();
		// TODO Auto-generated constructor stub
	}

	public VOEmail(String remitente, String destinatario, String asunto, String cuerpo, ArrayList<VOArchivoAdjunto> lstArchivosAdjuntos) {
		super();
		this.remitente = remitente;
		this.destinatario = destinatario;
		this.asunto = asunto;
		this.cuerpo = cuerpo;
		this.lstArchivosAdjuntos = lstArchivosAdjuntos;
	}
	
	public String getRemitente() {
		return remitente;
	}

	public void setRemitente(String remitente) {
		this.remitente = remitente;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getCuerpo() {
		return cuerpo;
	}

	public void setCuerpo(String cuerpo) {
		this.cuerpo = cuerpo;
	}

	public ArrayList<VOArchivoAdjunto> getLstArchivosAdjuntos() {
		return lstArchivosAdjuntos;
	}

	public void setLstArchivosAdjuntos(ArrayList<VOArchivoAdjunto> lstArchivosAdjuntos) {
		this.lstArchivosAdjuntos = lstArchivosAdjuntos;
	}
		
}
