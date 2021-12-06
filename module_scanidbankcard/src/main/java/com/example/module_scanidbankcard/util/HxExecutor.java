package com.example.module_scanidbankcard.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class HxExecutor {
	private static ExecutorService executor = Executors.newFixedThreadPool(5);

	public static void execute(Runnable worker) {
		executor.execute(worker);
	}
}
