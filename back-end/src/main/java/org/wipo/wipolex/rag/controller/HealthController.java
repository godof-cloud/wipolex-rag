package org.wipo.wipolex.rag.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

	@GetMapping("/api/health")
	String healthApi() {
		return "SERVICE UP";
	}
}
