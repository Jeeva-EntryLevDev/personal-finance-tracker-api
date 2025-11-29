package com.jeeva.financetracker.expensetrackerapi;

import org.springframework.boot.SpringApplication;

public class TestPersonalFinanceTrackerApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(PersonalFinanceTrackerApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
