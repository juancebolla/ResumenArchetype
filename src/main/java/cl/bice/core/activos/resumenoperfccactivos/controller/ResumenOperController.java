package cl.bice.core.activos.resumenoperfccactivos.controller;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import cl.bice.core.activos.resumenoperfccactivos.Greeting;

import cl.bice.core.activos.resumenoperfccactivos.service.ResumenOperService;
import cl.bice.core.activos.resumenoperfccactivos.to.RespuestaTO;

@RestController
public class ResumenOperController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    
    private final ResumenOperService resumenService;
    
    public ResumenOperController(final ResumenOperService resumenService) {
    	this.resumenService = resumenService;
    }

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
    
    @RequestMapping("/resumen")
    public RespuestaTO resumen(
    		@RequestParam(value="tipoProducto") String tipoProducto,
    		@RequestParam(value="rut") String rut,
    		@RequestParam(value="desde") int desde,
    		@RequestParam(value="hasta") int hasta
    		) {
        return resumenService.getResumenOperFccActivos(tipoProducto, rut, desde, hasta);
    }   
    
}