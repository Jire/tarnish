package com.osroyale.util.parser.old;

import com.google.gson.JsonObject;
import com.osroyale.game.world.items.SkillRequirement;
import com.osroyale.util.parser.GsonParser;

import java.util.HashMap;
import java.util.Map;

public class EquipmentParser extends GsonParser {
	
	public static Map<Integer, Loader> LOADED = new HashMap<>();

	public EquipmentParser() {
		super("equip");
	}

	@Override
	protected void parse(JsonObject data) {
		final int id = data.get("id").getAsInt();
		SkillRequirement[] requirement = new SkillRequirement[] {};

		if (data.has("requirements")) {
			requirement = builder.fromJson(data.get("requirements"), SkillRequirement[].class);
		}

		LOADED.put(id, new Loader(id, requirement));
	}

	static class Loader {
		private final int id;
		private final SkillRequirement[] reqs;
		
		public Loader(int id, SkillRequirement[] req) {
			this.id = id;
			this.reqs = req;
		}
		
		public int getId() {
			return id;
		}
		
		public SkillRequirement[] getReqs() {
			return reqs;
		}
	}
}
