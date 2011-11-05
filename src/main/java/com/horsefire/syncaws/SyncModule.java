package com.horsefire.syncaws;

import com.google.inject.AbstractModule;

public class SyncModule extends AbstractModule {

	private final CommandLineArgs m_args;

	public SyncModule(CommandLineArgs args) {
		m_args = args;
	}

	@Override
	protected void configure() {
		bind(CommandLineArgs.class).toInstance(m_args);
	}
}
