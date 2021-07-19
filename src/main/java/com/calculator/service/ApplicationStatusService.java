package com.calculator.service;

import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.util.Date;

import com.calculator.utils.DateUtils;

public class ApplicationStatusService {

	private static final ApplicationStatusService INSTANCE = new ApplicationStatusService();

	private ApplicationStatusService() {

	}

	public static ApplicationStatusService getInstance() {

		return INSTANCE;
	}

	public Date getUptime() {

		long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
		return DateUtils.convertToDate(LocalDateTime.now().minusSeconds(uptime / 1000));
	}

}
