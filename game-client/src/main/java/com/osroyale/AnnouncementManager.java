package com.osroyale;

class AnnouncementManager {
    static final int START = 0, WAIT = 1, FINISH = 2, MAX = 200, YPOSITION = 25, STALL_TIME = 1550;
    private int tick;
    private int count;
    public int state;
    private Announcement message;

    void addMessage(Client client, String title, String context, int firstColor, int secondColor) {
        this.tick = 0;
        this.count = 0;
        this.state = START;
        this.message = new Announcement(client, title, context, firstColor, secondColor);
    }

    private boolean isActive() {
        return message != null;
    }

    void draw() {
        if (isActive()) {
            int messageFade = 150 - (int) (65 * Math.sin(count++ / 20.0));
            Client client = message.getClient();
            message.update();

            if (state == START) {
                messageFade = tick += 3;

                if (messageFade == MAX) {
                    state = WAIT;
                    tick = -1;
                }
            } else if (state == FINISH) {
                if (tick == -1) {
                    tick = MAX;
                }

                messageFade = tick -= 3;

                if (messageFade <= 0) {
                    message = null;
                    return;
                }
            }

            if (messageFade > MAX)
                messageFade = MAX;
            if (messageFade < 0)
                messageFade = 0;

            Rasterizer2D.drawAlphaGradient(0, YPOSITION, Client.canvasWidth, 50, message.getFirstColor(), message.getFirstColor(), messageFade);
            Rasterizer2D.drawRectangle(0, YPOSITION, Client.canvasWidth, 50, 0x000000, messageFade);
            client.newFancyFont.drawRAString(message.getTitle(), message.getFirstLinePosition(), YPOSITION + 20, 0xffffff, 0x0);
            client.newRegularFont.drawRAString(message.getContext(), message.getSecondLinePosition(), YPOSITION + 38, 0xffffff, 0x0);
        }
    }
}
