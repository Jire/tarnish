package com.osroyale.content.skill.impl.magic.teleport;

import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.UpdatePriority;

import java.util.Optional;

/**
 * Created by Daniel on 2018-02-07.
 */
public enum TeleportationData {
    TABLET(2, new Animation(4731), new Graphic(678, 5, false)),
    MODERN(3, new Animation(714), new Graphic(308, 43, true)),
    OBELISK(3, new Animation(1816), new Graphic(308, 43, true), new Animation(6304)),
    ANCIENT(3, new Animation(1979), new Graphic(392, false)),
    HOME(9, new Animation(6601), new Graphic(1118), null, false),
    LEVER(3, new Animation(714), new Graphic(308, 43, true)),
    DONATOR(4, new Animation(7121 ), new Graphic(284), new Animation(65535, UpdatePriority.VERY_HIGH)),
    CREVICE(2, new Animation(6301), new Graphic(571, false));

    private final int delay;
    private final Optional<Animation> startAnimation;
    private final Optional<Graphic> startGraphic;
    private final Optional<Animation> middleAnimation;
    private final Optional<Graphic> middleGraphic;
    private final Optional<Animation> endAnimation;
    private final Optional<Graphic> endGraphic;
    private final boolean lockMovement;

    TeleportationData(int delay, Animation startAnimation, Animation middleAnimation, Graphic middleGraphic) {
        this(delay, startAnimation, null, middleAnimation, middleGraphic, null, null, true);
    }

    TeleportationData(int delay, Animation startAnimation, Graphic startGraphic, Animation endAnimation) {
        this(delay, startAnimation, startGraphic, endAnimation, null, true);
    }

    TeleportationData(int delay, Animation startAnimation, Graphic startGraphic, boolean lockMovement) {
        this(delay, startAnimation, startGraphic, null, null, lockMovement);
    }

    TeleportationData(int delay, Animation startAnimation, Graphic startGraphic, Animation endAnimation, boolean lockMovement) {
        this(delay, startAnimation, startGraphic, endAnimation, null, lockMovement);
    }

    TeleportationData(int delay, Animation startAnimation, Graphic startGraphic, Animation endAnimation, Graphic endGraphic) {
        this(delay, startAnimation, startGraphic, endAnimation, endGraphic, true);
    }

    TeleportationData(int delay, Animation startAnimation, Graphic startGraphic) {
        this(delay, startAnimation, startGraphic, null, null, true);
    }

    TeleportationData(int delay, Animation startAnimation, Graphic startGraphic, Animation endAnimation, Graphic endGraphic, boolean lockMovement) {
        this(delay, startAnimation, startGraphic, null, null, endAnimation, endGraphic, lockMovement);
    }

    TeleportationData(int delay, Animation startAnimation, Graphic startGraphic, Animation middleAnimation, Graphic middleGraphic, Animation endAnimation, Graphic endGraphic, boolean lockMovement) {
        this.delay = delay;
        this.startAnimation = Optional.ofNullable(startAnimation);
        this.startGraphic = Optional.ofNullable(startGraphic);
        this.middleAnimation = Optional.ofNullable(middleAnimation);
        this.middleGraphic = Optional.ofNullable(middleGraphic);
        this.endAnimation = Optional.ofNullable(endAnimation);
        this.endGraphic = Optional.ofNullable(endGraphic);
        this.lockMovement = lockMovement;
    }

    public int getDelay() {
        return delay;
    }

    public Optional<Animation> getStartAnimation() {
        return startAnimation;
    }

    public Optional<Graphic> getStartGraphic() {
        return startGraphic;
    }

    public Optional<Animation> getMiddleAnimation() {
        return middleAnimation;
    }

    public Optional<Graphic> getMiddleGraphic() {
        return middleGraphic;
    }

    public Optional<Animation> getEndAnimation() {
        return endAnimation;
    }

    public Optional<Graphic> getEndGraphic() {
        return endGraphic;
    }

    public boolean lockMovement() {
        return lockMovement;
    }
}