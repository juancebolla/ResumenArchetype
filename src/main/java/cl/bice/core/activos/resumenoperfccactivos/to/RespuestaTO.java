package cl.bice.core.activos.resumenoperfccactivos.to;

import java.io.Serializable;

public class RespuestaTO implements Serializable {
    
	private static final long serialVersionUID = 1L;
	
	private String estado;
	
	private String descripcion;
	
	private ResumenTO resumen;

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public ResumenTO getResumen() {
		return resumen;
	}

	public void setResumen(ResumenTO resumen) {
		this.resumen = resumen;
	}
	
	

}
