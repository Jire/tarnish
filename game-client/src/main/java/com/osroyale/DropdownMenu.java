package com.osroyale;


public class DropdownMenu {
    private final int width;
    private final String[] options;
    private final Dropdown dropdown;
    private boolean open;
    private String selected;
    private final int defaultOption;
    public int scroll;

    private static final int SELECT_HEIGHT = 13;

    DropdownMenu(int width, int defaultOption, Dropdown dropwdown, String... options) {
        this.width = width;
        this.options = options;
        this.selected = defaultOption == -1 ? "Select an option" : options[defaultOption];
        this.defaultOption = defaultOption;
        this.open = false;
        this.dropdown = dropwdown;
    }

    public int getWidth() {
        return this.width;
    }

    public String[] getOptions() {
        return this.options;
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean b) {
        this.open = b;
    }

    void setSelected(String selected) {
        this.selected = selected;
    }

    Dropdown getDrop() {
        return this.dropdown;
    }

    private void drawDropBox(RSInterface child, int x, int y, boolean hover) {
        int bgColor = hover ? 0x4C4237 : 0x483E33;
        int textColor = hover ? 0xFFB83F : 0xFD961E;

        Rasterizer2D.drawRectangle(x + 1, y + 1, width - 2, 22, 0);
        Rasterizer2D.fillRectangle(x + 2, y + 2, width - 4, 20, bgColor);

        RSFont font = Client.instance.newSmallFont;
        String message = !child.disabledMessage.isEmpty() ? child.disabledMessage : selected;

        x += child.centerText ? width / 2 - 2 : 6;
        font.drawString(message, x, y + 17, textColor, 0, child.centerText);
    }

    private void drawOpenBox(RSInterface child, int x, int y) {
        Sprite scrollBar = Client.spriteCache.get(29);
        if (child.hovered) {
            scrollBar.drawTransparentSprite(x + width - 20, y + 4, 125);
        } else {
            scrollBar.drawSprite(x + width - 20, y + 4, 0);
        }
        Rasterizer2D.drawRectangle(x + width - 21, y + 3, 17, 17, 0);
        int len = options.length;
        if (len > 5) len = 5;
        Rasterizer2D.fillRectangle(x + 1, y + 22, width - 2, SELECT_HEIGHT * len + 3, 0);

        int yy = 2;
        RSFont font = Client.instance.newSmallFont;
        for (int i = 0; i < len; i++) {
            int idx = i + scroll;
            int color = child.dropDownHover == idx ? 0x777067 : 0x534A3E;
            Rasterizer2D.fillRectangle(x + 2, y + yy + 22, width - 4 - (options.length > 5 ? 8 : 0), SELECT_HEIGHT, color);

            String option = options[idx];
            int textColor = child.dropDownHover == idx ? 0xFFB83F : 0xFD961E;

            if (child.centerText) {
                font.drawCenteredString(option, x + width / 2, y + 34 + yy, textColor, 0);
            } else {
                font.drawBasicString(option, x + 6, y + 34 + yy, textColor, 0);
            }

            yy += 13;
        }

        if (options.length > 5) {
            int hidden = options.length - 5;
            int scrollLength = 65 * hidden / options.length;
            int scrollPos = (65 - scrollLength) * scroll / hidden;
            Rasterizer2D.fillRectangle(x + width - 9, y + 24, 7, 13 * len, 0x534A3E);
            Rasterizer2D.fillRectangle(x + width - 9, y + 25 + scrollPos, 7, scrollLength - 1, 0x777067);
        }
    }

    public void drawDropdown(RSInterface child, int x, int y) {
        DropdownMenu d = child.dropDown;
        drawDropBox(child, x, y, child.hovered || open);

        if (open) {
            drawOpenBox(child, x, y);
        } else {
            Sprite scrollBar = Client.spriteCache.get(30);
            if (child.hovered) {
                scrollBar.drawTransparentSprite(x + width - 20, y + 4, 125);
            } else {
                scrollBar.drawSprite(x + width - 20, y + 4, 0);
            }
            Rasterizer2D.drawRectangle(x + width - 21, y + 4, 17, 17, 0);
        }
    }

    public void hover(RSInterface parent, RSInterface child, int hoverX, int hoverY, int xBounds, int yBounds) {
        if (hoverX < xBounds || hoverY < yBounds + 20) {
            child.dropDownHover = -1;
            return;
        }

        int height = 13;
        if (options.length > 5) {
            height *= 5;
        } else height *= options.length;

        if (hoverX > xBounds + width || hoverY >= yBounds + 21 + height) {
            child.dropDownHover = -1;
            return;
        }

        int yy = (yBounds + 21 + height + SELECT_HEIGHT) - hoverY;

        int len = options.length;
        if (len > 5) len = 5;

        int shit = yy / SELECT_HEIGHT;
        if (shit > len) shit = len;

        child.dropDownHover = (len - shit) + scroll;

        Client client = Client.instance;
        client.menuActionName[client.menuActionRow] = "Select " + options[child.dropDownHover];
        client.menuActionID[client.menuActionRow] = 770;
        client.menuActionCmd3[client.menuActionRow] = child.interfaceId;
        client.menuActionCmd2[client.menuActionRow] = child.dropDownHover;
        client.menuActionCmd1[client.menuActionRow] = parent.interfaceId;
        client.menuActionRow++;
    }

}
