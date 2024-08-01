package com.osroyale.engine.impl;

import com.osroyale.Client;
import com.osroyale.RSInterface;
import net.runelite.rs.api.RSMouseWheelHandler;

import java.awt.*;
import java.awt.event.*;


public class MouseWheelHandler implements MouseWheelListener, RSMouseWheelHandler {

    int rotation;
    public static boolean mouseWheelDown;
    public static int mouseWheelX;
    public static int mouseWheelY;

    public synchronized int useRotation() {
        int rotation = this.rotation; 
        this.rotation = 0; 
        return rotation; 
    }

    public void addTo(Component component) {
        component.addMouseWheelListener(this);
    }

    public void removeFrom(Component component) {
        component.removeMouseWheelListener(this);
    }

    public boolean canZoom = true;

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int rotation = e.getWheelRotation();
        handleInterfaceScrolling(e);
        if (MouseHandler.mouseX > 0 && MouseHandler.mouseX < 512 && MouseHandler.mouseY > Client.canvasHeight - 165 && MouseHandler.mouseY < Client.canvasHeight - 25) {
            int scrollPos = Client.anInt1089;
            scrollPos -= rotation * 30;
            if (scrollPos < 0)
                scrollPos = 0;
            if (scrollPos > Client.anInt1211 - 110)
                scrollPos = Client.anInt1211 - 110;
            if (Client.anInt1089 != scrollPos) {
                Client.anInt1089 = scrollPos;
                Client.redrawDialogueBox = true;
            }
        }
        if (Client.openInterfaceID == -1) {
            if (canZoom) {
                if (MouseHandler.mouseX > 0 && MouseHandler.mouseX < 512 && MouseHandler.mouseY > Client.canvasHeight - 165 && MouseHandler.mouseY < Client.canvasHeight - 25) {
                    return;
                }

                Client.clientZoom -= rotation * 45;

                boolean fixed = !Client.instance.isResized();
                if (Client.clientZoom > (fixed ? 1100 : 2200)) {
                    Client.clientZoom = (fixed ? 1100 : 2200);
                } else if (Client.clientZoom < (fixed ? 180 : 240)) {
                    Client.clientZoom = (fixed ? 180 : 240);
                }
            }
        }
    }

    private void handleInterfaceScrolling(MouseWheelEvent event) {
        int rotation = event.getWheelRotation();
        int positionX = 0;
        int positionY = 0;
        int width = 0;
        int height = 0;
        int offsetX = 0;
        int offsetY = 0;
        int childID = 0;
        int tabInterfaceID = Client.tabInterfaceIDs[Client.tabID];
        if (tabInterfaceID != -1) {
            RSInterface tab = RSInterface.interfaceCache[tabInterfaceID];
            offsetX = !Client.instance.isResized() ? Client.canvasWidth - 218 : (!Client.instance.isResized() ? 28 : Client.canvasWidth - 197);
            offsetY = !Client.instance.isResized() ? Client.canvasHeight - 298 : (!Client.instance.isResized() ? 37 : Client.canvasHeight - (Client.canvasWidth >= 1000 ? 37 : 74) - 267);

            if (tab.children != null || tab.children.length != 0) {
                for (int index = 0; index < tab.children.length; index++) {
                    RSInterface tabChild = RSInterface.interfaceCache[tab.children[index]];
                    if (tabChild.children != null) {
                        for (int idx = 0; idx < tabChild.children.length; idx++) {
                            RSInterface tabChild2 = RSInterface.interfaceCache[tabChild.children[idx]];
                            if (tabChild2.dropDown != null && tabChild2.dropDown.isOpen() && tabChild2.dropDown.getOptions().length > 5) {
                                canZoom = false;
                                tabChild2.dropDown.scroll += rotation;
                                if (tabChild2.dropDown.scroll >= tabChild2.dropDown.getOptions().length - 5) {
                                    tabChild2.dropDown.scroll = tabChild2.dropDown.getOptions().length - 5;
                                }
                                if (tabChild2.dropDown.scroll < 0) {
                                    tabChild2.dropDown.scroll = 0;
                                }
                                return;
                            }
                        }
                    }
                    if (tabChild.scrollMax > 0) {
                        childID = index;
                        positionX = tab.childX[index];
                        positionY = tab.childY[index];
                        width = RSInterface.interfaceCache[tab.children[index]].width;
                        height = RSInterface.interfaceCache[tab.children[index]].height;
                        break;
                    }
                }
            }
            if (MouseHandler.mouseX > offsetX + positionX && MouseHandler.mouseY > offsetY + positionY && MouseHandler.mouseX < offsetX + positionX + width && MouseHandler.mouseY < offsetY + positionY + height) {
                canZoom = false;
                RSInterface.interfaceCache[tab.children[childID]].scrollPosition += rotation * 30;
            } else {
                canZoom = true;
            }
        }
        if (Client.openInterfaceID != -1) {
            int w = 512, h = 334;
            int x = (Client.canvasWidth / 2) - 256;
            int y = (Client.canvasHeight / 2) - 167;
            int count = !Client.changeTabArea ? 4 : 3;
            if (Client.instance.isResized()) {
                for (int i = 0; i < count; i++) {
                    if (x + w > (Client.canvasWidth - 225)) {
                        x = x - 30;
                        if (x < 0) {
                            x = 0;
                        }
                    }
                    if (y + h > (Client.canvasHeight - 182)) {
                        y = y - 30;
                        if (y < 0) {
                            y = 0;
                        }
                    }
                }
            }
            RSInterface rsi = RSInterface.interfaceCache[Client.openInterfaceID];


            if (rsi == null)
                return;

            if (Client.openInterfaceID == 60000) {
                offsetX = !Client.instance.isResized() ? 4 : (Client.canvasWidth / 2) - 356;
                offsetY = !Client.instance.isResized() ? 4 : (Client.canvasHeight / 2) - 230;
            } else {
                offsetX = !Client.instance.isResized() ? 4 : x;
                offsetY = !Client.instance.isResized() ? 4 : y;
            }
            if (rsi.children != null) {
                for (int index = 0; index < rsi.children.length; index++) {
                    RSInterface tabChild = RSInterface.interfaceCache[rsi.children[index]];
                    if (tabChild == null)
                        break;
                    if (tabChild.dropDown != null && tabChild.dropDown.isOpen() && tabChild.dropDown.getOptions().length > 5) {
                        tabChild.dropDown.scroll += rotation;
                        if (tabChild.dropDown.scroll >= tabChild.dropDown.getOptions().length - 5) {
                            tabChild.dropDown.scroll = tabChild.dropDown.getOptions().length - 5;
                        }
                        if (tabChild.dropDown.scroll < 0) {
                            tabChild.dropDown.scroll = 0;
                        }
                        return;
                    }
                    if (tabChild.scrollMax > 0) {
                        childID = index;
                        positionX = rsi.childX[index];
                        positionY = rsi.childY[index];
                        width = RSInterface.interfaceCache[rsi.children[index]].width;
                        height = RSInterface.interfaceCache[rsi.children[index]].height;
                        break;
                    }
                }
            }

            if ((MouseHandler.mouseX > (offsetX + positionX)) && (MouseHandler.mouseY > (offsetY + positionY)) && (MouseHandler.mouseX < (offsetX + positionX + width)) && (MouseHandler.mouseY < (offsetY + positionY + height))) {
                if (RSInterface.interfaceCache[rsi.children[childID]].scrollPosition > 0) {
                    RSInterface.interfaceCache[rsi.children[childID]].scrollPosition += rotation * rsi.scrollSpeed;
                    if (RSInterface.interfaceCache[rsi.children[childID]].scrollPosition < 0) {
                        RSInterface.interfaceCache[rsi.children[childID]].scrollPosition = 0;
                    }
                    return;
                } else {
                    if (rotation > 0) {
                        RSInterface.interfaceCache[rsi.children[childID]].scrollPosition += rotation * rsi.scrollSpeed;
                        if (RSInterface.interfaceCache[rsi.children[childID]].scrollPosition > RSInterface.interfaceCache[rsi.children[childID]].scrollMax) {
                            RSInterface.interfaceCache[rsi.children[childID]].scrollPosition = RSInterface.interfaceCache[rsi.children[childID]].scrollMax;
                        }
                        return;
                    }
                }
            }
            if (MouseHandler.mouseX > offsetX + positionX && MouseHandler.mouseY > offsetY + positionY && MouseHandler.mouseX < offsetX + positionX + width && MouseHandler.mouseY < offsetY + positionY + height) {
                canZoom = false;
            } else {
                canZoom = true;
            }
        }
    }

}
