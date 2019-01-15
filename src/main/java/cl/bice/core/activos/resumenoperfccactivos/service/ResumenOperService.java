package cl.bice.core.activos.resumenoperfccactivos.service;

import org.springframework.stereotype.Service;

import cl.bice.core.activos.resumenoperfccactivos.dao.ResumenOperDAO;
import cl.bice.core.activos.resumenoperfccactivos.to.RespuestaTO;

@Service
public class ResumenOperService {
	
	private final ResumenOperDAO resumenDao;
	
	public ResumenOperService(final ResumenOperDAO resumenDao) {
		this.resumenDao = resumenDao;
	}

	public RespuestaTO getResumenOperFccActivos(String tipoProducto, String rut, int desde, int hasta) {
		return this.resumenDao.getResumenOperFccActivos(tipoProducto, rut, desde, hasta);
	}
	
}
