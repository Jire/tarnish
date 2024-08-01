/*
package com.osroyale;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

import static com.osroyale.Configuration.CHAR_PATH;

*/
/**
 * Handles managing the account profiles for the login screen.
 *
 * @author Daniel
 * @author Zion
 *//*

public class AccountManager {

	private static final String FILE_NAME = "accounts.dat";
	public static final List<AccountData> ACCOUNTS = new LinkedList<>();

	public static AccountData get(int index) {
		if (ACCOUNTS.isEmpty() || index >= ACCOUNTS.size()) {
			return null;
		}
		return ACCOUNTS.get(index);
	}

	static void add(String username, String password, int rank) {
		Utility.formatName(username);
		AccountData account = null;
		int accountIndex = 0;

		for (int index = 0; index < ACCOUNTS.size(); index++) {
			if (ACCOUNTS.get(index).username.equalsIgnoreCase(username)) {
				account = ACCOUNTS.get(index);
				accountIndex = index;
				break;
			}
		}

		if (account == null) {
			if (ACCOUNTS.size() >= 3) {
				return;
			}
			ACCOUNTS.add(new AccountData(username, password));
			saveAccount();
			return;
		}

		account.uses++;
		ACCOUNTS.remove(accountIndex);
		ACCOUNTS.add(accountIndex, new AccountData(username, password, account.created, account.avatar, rank, account.uses));
		saveAccount();
	}

	public static void setAvatar(String username, int avatar) {
		AccountData lastAccount = Client.instance.lastAccount;
		Utility.formatName(username);
		AccountData account = null;
		int accountIndex = 0;

		for (int index = 0; index < ACCOUNTS.size(); index++) {
			if (ACCOUNTS.get(index).username.equalsIgnoreCase(username)) {
				account = ACCOUNTS.get(index);
				accountIndex = index;
				break;
			}
		}

		if (account == null) {
			return;
		}

		ACCOUNTS.remove(accountIndex);
		ACCOUNTS.add(accountIndex, new AccountData(username, lastAccount.password, lastAccount.created, avatar, lastAccount.rank, lastAccount.uses));
		saveAccount();
	}

	public static void clearAccountList() {
		ACCOUNTS.clear();
		saveAccount();
	}

	*/
/**
	 * Removes a desired account from the <accounts> map
	 *//*

	public static void removeAccount(AccountData account) {
		if (!ACCOUNTS.contains(account)) {
			return;
		}
		ACCOUNTS.remove(account);
		saveAccount();
	}

	*/
/**
	 * Saves the account
	 *//*

	public static void saveAccount() {
		if (ACCOUNTS.isEmpty())
			return;
		try {
			File file = FileUtility.getOrCreate(CHAR_PATH, FILE_NAME);
			DataOutputStream output = new DataOutputStream(new FileOutputStream(file));
			output.writeByte(ACCOUNTS.size());
			for (AccountData account : ACCOUNTS) {
				output.writeUTF(account.username);
				output.writeUTF(account.password);
				output.writeUTF(account.created);
				output.writeInt(account.avatar);
				output.writeInt(account.rank);
				output.writeInt(account.uses);
			}
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	*/
/**
	 * Loads the account
	 *//*

	public static void loadAccount() {
		try {
			File file = FileUtility.getOrCreate(CHAR_PATH, FILE_NAME);
			DataInputStream input = new DataInputStream(new FileInputStream(file));
			int fileSize = input.read();
			if (fileSize < 0) {
				input.close();
				return;
			}
			for (int index = 0; index < fileSize; index++) {
				String username = input.readUTF();
				String password = input.readUTF();
				String created = input.readUTF();
				int avatar = input.readInt();
				int rank = input.readInt();
				int uses = input.readInt();
				AccountData account = new AccountData(username, password, created, avatar, rank, uses);
				ACCOUNTS.add(account);
			}
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		initialize();
	}

	public static void initialize() {
		if (ACCOUNTS.isEmpty()) {
			return;
		}
		Client.instance.lastAccount = ACCOUNTS.get(0);
	}
}*/
