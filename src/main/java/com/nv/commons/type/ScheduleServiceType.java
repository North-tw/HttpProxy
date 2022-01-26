package com.nv.commons.type;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.nv.manager.WebSocketSessionManager;
import com.nv.util.LogUtils;

public enum ScheduleServiceType {

	BreakUnusedWebSocketSessionTask("delay", 0, 20, TimeUnit.SECONDS, () -> {
		try {
			WebSocketSessionManager.getInstance().breakUnusedSession();
		} catch (Exception e) {
			LogUtils.err.error(e.getMessage(), e);
		}
	});

	public final static List<ScheduleServiceType> VALUES = Arrays.stream(ScheduleServiceType.values())
		.collect(Collectors.toUnmodifiableList());

	private Runnable task;

	private String rateOrDelay;

	private int initDelay;

	private int interval;

	private TimeUnit timeUnit;

	private ScheduledExecutorService exec;

	private ScheduleServiceType(String rateOrDelay, int initDelay, int interval, TimeUnit timeUnit,
		Runnable task) {
		this.rateOrDelay = rateOrDelay;
		this.initDelay = initDelay;
		this.interval = interval;
		this.timeUnit = timeUnit;
		this.task = task;
	}

	public Optional<String> schedule() {
		if (exec == null) {
			exec = Executors.newSingleThreadScheduledExecutor();
		}
		switch (rateOrDelay) {
			case "rate" :
				exec.scheduleAtFixedRate(task, initDelay, interval, timeUnit);
				break;
			case "delay" :
				exec.scheduleWithFixedDelay(task, initDelay, interval, timeUnit);
				break;
			default :
				throw new RuntimeException("Abnormal rateOrDelay!!! rateOrDelay:" + rateOrDelay);
		}
		return Optional.of(rateOrDelay);
	}

	public void shutdown() {
		if (exec != null) {
			try {
				exec.shutdown();
			} catch (Exception e) {
				// nothing need to be done
			}
		}
	}
}
