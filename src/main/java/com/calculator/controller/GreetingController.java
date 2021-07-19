package com.calculator.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.calculator.dto.GreetingDto;
import com.calculator.dto.StatusDto;
import com.calculator.service.ApplicationStatusService;

@RestController
public class GreetingController {

	private AtomicLong counter = new AtomicLong();

	@GetMapping("/greeting")
	public ResponseEntity<EntityModel<GreetingDto>> greet(@RequestParam(value = "name", defaultValue = "World") String name) {

		GreetingDto greetingDto = new GreetingDto();
		greetingDto.setId(counter.incrementAndGet());
		greetingDto.setContent(String.format("Hello %s!", name));

		return ResponseEntity.ok(EntityModel.of(greetingDto, linkTo(methodOn(GreetingController.class).greet(null)).withSelfRel()));
	}

	@GetMapping("/status")
	public ResponseEntity<EntityModel<StatusDto>> getApplicationStatus() {

		StatusDto statusDto = new StatusDto();
		statusDto.setStatus("UP");
		statusDto.setUptime(ApplicationStatusService.getInstance().getUptime());

		return ResponseEntity.ok(EntityModel.of(statusDto, linkTo(methodOn(GreetingController.class).getApplicationStatus()).withSelfRel()));
	}

}
