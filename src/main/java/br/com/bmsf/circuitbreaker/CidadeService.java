package br.com.bmsf.circuitbreaker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.stereotype.Service;

@Service
public class CidadeService {
	
	@Autowired
    private Resilience4JCircuitBreakerFactory circuitBreakerFactory;

	private static int countOK = 0;
	private static int countError = 0;
	
	public List<String> findByEstado(String uf){
		return simulaServicoInstavel(uf);
	}
	
//	@HystrixCommand(fallbackMethod = "findByEstadoHystrixFallback")
//	@CircuitBreaker(name = "shared",fallbackMethod = "findByEstadoHystrixFallback")
	public List<String> findByEstadoHystrix(String uf){
		return circuitBreakerFactory.create("findByEstadoHystrix")
				.run(
						() -> simulaServicoInstavel(uf),
						Throwable -> findByEstadoHystrixFallback(uf)
				);
	}
	
	public List<String> findByEstadoHystrixFallback(String uf){
		return Arrays.asList("Fallback metodo");
	}
	
	private List<String> simulaServicoInstavel(String uf){
		if(countOK <= 10) {			
			if("sp".equalsIgnoreCase(uf)) {
				countOK++;
				return Arrays.asList("Sao Jose dos Campos","Jacarei","Santo Andre", "Maua");
			}
			
			if(countOK == 10) {
				countError = 0;
			}
			
			countOK++;
			
			return new ArrayList<>();
		}else if(countError <= 5) {
			countError++;
			
			for(int i=0;i<200;i++) {
				System.out.println();
			}
			
			throw new NullPointerException();
		}
		
		countOK = 0;
		countError = 0;
		
		return Arrays.asList("Sao Jose dos Campos","Jacarei","Santo Andre", "Maua");
	}
}
