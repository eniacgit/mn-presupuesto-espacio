package org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects;

public class VOArchivoAdjunto {
	private String rutaArchivoAdjunto;
	private String nombreArchivoAdjunto;
	
	public VOArchivoAdjunto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public VOArchivoAdjunto(String rutaArchivoAdjunto, String nombreArchivoAdjunto) {
		super();
		this.rutaArchivoAdjunto = rutaArchivoAdjunto;
		this.nombreArchivoAdjunto = nombreArchivoAdjunto;
	}

	public String getRutaArchivoAdjunto() {
		return rutaArchivoAdjunto;
	}

	public void setRutaArchivoAdjunto(String rutaArchivoAdjunto) {
		this.rutaArchivoAdjunto = rutaArchivoAdjunto;
	}

	public String getNombreArchivoAdjunto() {
		return nombreArchivoAdjunto;
	}

	public void setNombreArchivoAdjunto(String nombreArchivoAdjunto) {
		this.nombreArchivoAdjunto = nombreArchivoAdjunto;
	}
	
	
	
	
}
