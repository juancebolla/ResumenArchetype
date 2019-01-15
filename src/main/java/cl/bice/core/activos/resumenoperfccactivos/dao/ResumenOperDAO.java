package cl.bice.core.activos.resumenoperfccactivos.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import cl.bice.core.activos.resumenoperfccactivos.mybatis.mapper.ResumenOperMapper;
import cl.bice.core.activos.resumenoperfccactivos.to.ProductoTO;
import cl.bice.core.activos.resumenoperfccactivos.to.RespuestaTO;
import cl.bice.core.activos.resumenoperfccactivos.to.ResumenTO;

@Repository
@EnableAutoConfiguration
@PropertySource(value = "classpath:application.properties", encoding = "UTF-8")
public class ResumenOperDAO {
	
	private final ResumenOperMapper resumenMapper;

	public ResumenOperDAO(final ResumenOperMapper resumenOperMapper) {
		super();
		this.resumenMapper = resumenOperMapper;
	}
	
	public RespuestaTO getResumenOperFccActivos(String tipoProducto, String rut, int desde, int hasta) {
		RespuestaTO resp;
		try {
			resp = new RespuestaTO();
			HashMap<String, Object> rs_map = new HashMap<String, Object>();
			rs_map.put("P_TIPO_PRODUCTO",tipoProducto);
			rs_map.put("P_RUT", rut);
			rs_map.put("P_NUM_REGISTROS", null);			
			rs_map.put("P_REGISTROS",null);
			rs_map.put("P_DESDE", desde);
			rs_map.put("P_HASTA", hasta);
			rs_map.put("P_ERROR_CODE",null);
			rs_map.put("P_ERROR_DESC", null);
			rs_map.put("P_NUM_OPERACION", "");
			System.out.println("P_TIPO_PRODUCTO:"+tipoProducto+",rut:"+rut+",desde:"+desde+",hasta:"+hasta);
			this.resumenMapper.getResumen(rs_map);
			if (rs_map != null) {
				BigDecimal numRegistros = (BigDecimal) rs_map.get("P_NUM_REGISTROS");
				System.out.println("numRegistros:"+numRegistros);
				List<ProductoTO> registros = (List<ProductoTO>) rs_map.get("P_REGISTROS");
				BigDecimal desdeRs = (BigDecimal) rs_map.get("P_DESDE");
				BigDecimal hastaRs = (BigDecimal) rs_map.get("P_HASTA");
				String error = (String) rs_map.get("P_ERROR_CODE");
				String desc = (String) rs_map.get("P_ERROR_DESC");
				if (numRegistros != null && numRegistros.compareTo(BigDecimal.ZERO) > 0) {
					resp.setEstado("SUCCESS");
					resp.setDescripcion("Respuesta OK.");
					ResumenTO resumen = new ResumenTO();
					resumen.setDesde(desdeRs);
					resumen.setHasta(hastaRs);
					resumen.setNumRegistros(numRegistros);
					resumen.setRegistros(registros);
					resp.setResumen(resumen);
				}
				else {
					resp.setEstado("FAILURE");
					resp.setDescripcion((error != null ? error:"error null.")+(desc != null ? desc:"descripcion null."));
				}
			}
			else {
				resp.setEstado("FAILURE");
				resp.setDescripcion("Respuesta SP es null.");
			}
		}
		catch (Exception e) {
			resp = new RespuestaTO();
			resp.setEstado("FAILURE");
			resp.setDescripcion(getStackTrace(e));
		}
		return resp;
	}
	
	
	public static String getStackTrace(final Throwable throwable) {
		 final java.io.StringWriter sw = new java.io.StringWriter();
		 final java.io.PrintWriter pw = new java.io.PrintWriter(sw, true);
		 throwable.printStackTrace(pw);
		 return sw.getBuffer().toString();
	}
	
}
