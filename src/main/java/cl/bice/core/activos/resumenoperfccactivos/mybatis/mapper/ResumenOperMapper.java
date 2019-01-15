package cl.bice.core.activos.resumenoperfccactivos.mybatis.mapper;

import java.util.HashMap;
import org.springframework.stereotype.Component;



@Component
public interface ResumenOperMapper {

	
	public HashMap getResumen(HashMap<String, Object> map);
	
}
