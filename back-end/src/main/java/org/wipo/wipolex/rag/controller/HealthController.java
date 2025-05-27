package org.wipo.wipolex.rag.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

	@GetMapping("/health")
	String health() {
		return "UP";
	}
	
	@GetMapping("/api/health")
	String healthApi() {
		return "API UP";
	}
}
