package com.horsefire.syncaws;

import java.util.UUID;

public class UuidGenerator {

	public String getId() {
		return UUID.randomUUID().toString();
	}
}
