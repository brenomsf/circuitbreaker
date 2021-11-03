package br.com.bmsf.circuitbreaker;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cidades")
public class CidadesController {
	
	private final CidadeService service;
	
	public CidadesController(CidadeService service) {
		this.service = service;
	}
	
	@GetMapping
	public ResponseEntity<List<String>> findByEstado(@RequestParam(required = true) String uf){
		try {
//			return ResponseEntity.ok(service.findByEstado(uf));
			return ResponseEntity.ok(service.findByEstadoHystrix(uf));
		}catch (Exception ex) {
			return new ResponseEntity("Erro inesperado. Tente novamente mais tarde.", HttpStatus.BAD_GATEWAY);
		}
	}

}
