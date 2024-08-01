package com.osroyale.content.skill.guides;

public class MenuOption {

    public Option[] option;

    public MenuOption(Option... option) {
        this.option = option;
    }

    public static class Option {

        public int itemId;
        public int level;
        public String name;

        public Option(int itemId, int level, String name) {
            this.itemId = itemId;
            this.level = level;
            this.name = name;
        }

    }

}
