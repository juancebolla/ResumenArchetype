package cl.bice.core.activos.resumenoperfccactivos.to;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ResumenTO implements Serializable {
    
	private static final long serialVersionUID = 1L;

	private BigDecimal numRegistros;
	
	private BigDecimal desde;
	
	private BigDecimal hasta;

	private List<ProductoTO> registros;

	public BigDecimal getNumRegistros() {
		return numRegistros;
	}

	public void setNumRegistros(BigDecimal numRegistros) {
		this.numRegistros = numRegistros;
	}

	public BigDecimal getDesde() {
		return desde;
	}

	public void setDesde(BigDecimal desde) {
		this.desde = desde;
	}

	public BigDecimal getHasta() {
		return hasta;
	}

	public void setHasta(BigDecimal hasta) {
		this.hasta = hasta;
	}

	public List<ProductoTO> getRegistros() {
		return registros;
	}

	public void setRegistros(List<ProductoTO> registros) {
		this.registros = registros;
	}


	
}
