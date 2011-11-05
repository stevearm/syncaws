package com.horsefire.syncaws.tasks;

public interface Task {

	void validate();

	void run() throws Exception;
}
