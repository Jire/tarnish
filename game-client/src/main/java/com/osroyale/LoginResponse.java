package com.osroyale;

public enum LoginResponse {

	FAIL(-1),
	EXCHANGE_DATA(0),
	DELAY(1),
	NORMAL(2),
	INVALID_CREDENTIALS(3, "", "Invalid username or password."),
	ACCOUNT_DISABLED(4, "Your account has been disabled.", "Please check your message-center for details."),
	ACCOUNT_ONLINE(5, "Your account is already logged in.", "Try again in 60 secs..."),
	GAME_UPDATED(6, Configuration.NAME + " has been updated!", "Please reload this page."),
	WORLD_FULL(7, "This world is full.", "Please use a different world."),
	LOGIN_SERVER_OFFLINE(8, "Unable to connect.", "Login server offline."),
	LOGIN_LIMIT_EXCEEDED(9, "Login limit exceeded.", "Too many connections from your address."),
	BAD_SESSION_ID(10, "Unable to connect.", "Bad session id."),
	LOGIN_SERVER_REJECTED_SESSION(11, "Login server rejected session.", "Please try again."),
	MEMBERS_ACCOUNT_REQUIRED(12, "You need a members account to login to this world.", "Please subscribe, or use a different world."),
	COULD_NOT_COMPLETE_LOGIN(13, "Could not complete login.", "Please try using a different world."),
	SERVER_BEING_UPDATED(14, "The server is being updated.", "Please wait 1 minute and try again."),
	RECONNECTING(15),
	LOGIN_ATTEMPTS_EXCEEDED(16, "Login attempts exceeded.", "Please wait 1 minute and try again."),
	MEMBERS_ONLY_AREA(17, "You are standing in a members-only area.", "To play on this world move to a free area first"),
	INVALID_LOGIN_SERVER(20, "Invalid loginserver requested", "Please try using a different world."),
	TRANSFERING_PROFILE(21, "You have only just left another world", "Your profile will be transferred in: 10 seconds"),
	BAD_USERNAME(22, "This username is not permitted!", "Please try a different username."),
	SHORT_USERNAME(23, "Username must consist of 3 or more characters.", "Please try a different username."),
	INSUFFICIENT_PERMSSION(24, "You do not have sufficient permission to access this.", "Please try a different world."),
	UNAUTHORIZED_PRIVILEGE(25, "Your account has un-authorized privileges and is", "now locked. Please open a ticket on forums to fix."),
	FORUM_REGISTRATION(26, "You have not registered on the forums yet.", "Please register on the forums before trying to login."),
	INVALID_EMAIL(27, "The email you entered is invalid.", "Please try a different email."),
	BAD_PASSWORD(28, "This password is not permitted!", "Please try a different password.");

	private final int code;
	private final String line1;
	private final String line2;

	LoginResponse(int code) {
		this(code, "");
	}

	LoginResponse(int code, String line1) {
		this(code, line1, "");
	}

	LoginResponse(int code, String line1, String line2) {
		this.code = code;
		this.line1 = line1;
		this.line2 = line2;
	}

	public static LoginResponse lookup(int code) {
		assert (code >= 0 && code < LoginResponse.values().length);
		return LoginResponse.values()[code];
	}

	public final int getCode() {
		return code;
	}

	public String getLine1() {
		return line1;
	}

	public String getLine2() {
		return line2;
	}

}
