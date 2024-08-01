package com.osroyale;

import java.util.HashMap;
import java.util.Map;

public class ParticleAttachment {

	private static final Map<Integer, int[][]> attachments = new HashMap<>();

	static {
		// max cape
		attachments.put(29616, new int[][]{{272, 0}, {269, 0}, {49, 0}, {45, 0}, {37, 0}, {16, 0}, {17, 0}, {5, 0}, {41, 0}, {283, 0}, {310, 0}, {315, 0}});
		attachments.put(29624, new int[][]{{49, 0}, {45, 0}, {37, 0}, {16, 0}, {17, 0}, {5, 0}, {41, 0}, {283, 0}, {310, 0}, {315, 0}});

		attachments.put(187, new int[][]{{33, 1}, {24, 1},  {6, 1},  {13, 1},  {16, 1},  {22, 1},  {23, 1}
				,  {22, 1},  {10, 1}});



	}

	public static int[][] getAttachments(int modelId) {
		return attachments.get(modelId);
	}
}