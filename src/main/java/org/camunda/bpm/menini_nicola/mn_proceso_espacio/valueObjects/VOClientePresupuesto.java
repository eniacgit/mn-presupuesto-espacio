package org.camunda.bpm.menini_nicola.mn_proceso_espacio.valueObjects;

public class VOClientePresupuesto {
	private int idClientePresupuesto;
	private byte estado;
	private int idCliente;
	private int idPresupuesto;
	
	public VOClientePresupuesto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public VOClientePresupuesto(int idClientePresupuesto, byte estado, int idCliente, int idPresupuesto) {
		super();
		this.idClientePresupuesto = idClientePresupuesto;
		this.estado = estado;
		this.idCliente = idCliente;
		this.idPresupuesto = idPresupuesto;
	}

	public int getIdClientePresupuesto() {
		return idClientePresupuesto;
	}

	public void setIdClientePresupuesto(int idClientePresupuesto) {
		this.idClientePresupuesto = idClientePresupuesto;
	}

	public byte getEstado() {
		return estado;
	}

	public void setEstado(byte estado) {
		this.estado = estado;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public int getIdPresupuesto() {
		return idPresupuesto;
	}

	public void setIdPresupuesto(int idPresupuesto) {
		this.idPresupuesto = idPresupuesto;
	}
			
}
