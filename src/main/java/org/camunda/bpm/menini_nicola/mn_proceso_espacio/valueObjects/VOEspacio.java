package org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects;

public class VOEspacio {
	private int idEspacio;
	private String cronograma;
	private int validez;
	private int idPresupuesto;
	
	public VOEspacio() {
		super();
		// TODO Auto-generated constructor stub
	}

	public VOEspacio(int idEspacio, String cronograma, int validez, int idPresupuesto) {
		super();
		this.idEspacio = idEspacio;
		this.cronograma = cronograma;
		this.validez = validez;
		this.idPresupuesto = idPresupuesto;
	}

	public int getIdEspacio() {
		return idEspacio;
	}

	public void setIdEspacio(int idEspacio) {
		this.idEspacio = idEspacio;
	}

	public String getCronograma() {
		return cronograma;
	}

	public void setCronograma(String cronograma) {
		this.cronograma = cronograma;
	}

	public int getValidez() {
		return validez;
	}

	public void setValidez(int validez) {
		this.validez = validez;
	}

	public int getIdPresupuesto() {
		return idPresupuesto;
	}

	public void setIdPresupuesto(int idPresupuesto) {
		this.idPresupuesto = idPresupuesto;
	}
	
	
	
	
	
}
