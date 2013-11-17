package com.brotherlogic.proxycache.callbacklistener;

import java.util.Map;

public interface ListenerCallback {
	public void processResponse(Map<String, String> props);
}
