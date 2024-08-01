package com.osroyale;

public enum PrivacyChatMode {
	ON(0),
	FRIENDS_ONLY(1),
	OFF(2);

	private final int code;

	PrivacyChatMode(int code) {
		this.code = code;
	}

	public static boolean isValid(int code) {
		return code >= 0 && code < PrivacyChatMode.values().length;
	}

	public static PrivacyChatMode get(int code) {
		if (!isValid(code)) {
			return null;
		}
		return PrivacyChatMode.values()[code];
	}

	public int getCode() {
		return code;
	}

}
