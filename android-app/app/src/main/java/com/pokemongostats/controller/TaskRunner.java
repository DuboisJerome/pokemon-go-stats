package com.pokemongostats.controller;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskRunner {
	private static final ExecutorService executor =
			new ThreadPoolExecutor(5, 32, 1,
					TimeUnit.SECONDS, new LinkedBlockingQueue<>());
	private final Handler handler = new Handler(Looper.getMainLooper());

	public interface Callback<R> {
		void onComplete(R result);

		void onError(Exception e);
	}

	public <R> Future<?> executeAsync(Callable<R> callable, Callback<R> callback) {
		Runnable task = () -> {
			try {
				R result = callable.call();
				this.handler.post(() ->
						callback.onComplete(result)
				);
			} catch (Exception e) {
				this.handler.post(() ->
						callback.onError(e)
				);
			}
		};
		return executor.submit(task);
	}

	public static <R> Future<?> executeNewRunnerAsync(Callable<R> callable, Callback<R> callback) {
		TaskRunner runner = new TaskRunner();
		return runner.executeAsync(callable, callback);
	}
}