package com.pokemongostats.controller;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskRunner {
	private static final Executor executor =
			new ThreadPoolExecutor(5, 32, 1,
					TimeUnit.SECONDS, new LinkedBlockingQueue<>());
	private final Handler handler = new Handler(Looper.getMainLooper());

	public interface Callback<R> {
		void onComplete(R result);

		void onError(Exception e);
	}

	public <R> void executeAsync(Callable<R> callable, Callback<R> callback) {
		executor.execute(() -> {
			try {
				final R result = callable.call();
				handler.post(() ->
					callback.onComplete(result)
				);
			} catch (Exception e) {
				handler.post(() ->
					callback.onError(e)
				);
			}
		});
	}

	public static <R> void executeNewRunnerAsync(Callable<R> callable, Callback<R> callback) {
		TaskRunner runner = new TaskRunner();
		runner.executeAsync(callable,callback);
	}
}