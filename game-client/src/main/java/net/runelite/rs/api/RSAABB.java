package net.runelite.rs.api;

import net.runelite.api.AABB;
import net.runelite.mapping.Import;

public interface RSAABB extends AABB
{
    @Import("xMid")
    int getCenterX();

    @Import("yMid")
    int getCenterY();

    @Import("zMid")
    int getCenterZ();

    @Import("xMidOffset")
    int getExtremeX();

    @Import("yMidOffset")
    int getExtremeY();

    @Import("zMidOffset")
    int getExtremeZ();
}