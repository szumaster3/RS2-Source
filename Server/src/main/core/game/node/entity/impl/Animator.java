package core.game.node.entity.impl;

import core.game.interaction.Clocks;
import core.game.node.entity.Entity;
import core.game.world.GameWorld;
import core.game.world.update.flag.EntityFlag;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;

/**
 * This class handles the animation and graphical effects for a given entity.
 * It allows managing animations, graphics, and their priorities.
 * It also handles the timing for when animations or graphics end.
 */
public final class Animator {

    /**
     * A constant for resetting animation.
     */
    public static final Animation RESET_A = new Animation(-1);

    /**
     * A constant for resetting graphics.
     */
    public static final Graphics RESET_G = new Graphics(-1);

    /**
     * The entity associated with this animator.
     */
    private Entity entity;

    /**
     * The current animation applied to the entity.
     */
    private Animation animation;

    /**
     * The current graphic applied to the entity.
     */
    private Graphics graphics;

    /**
     * The priority of the animation or graphic.
     */
    private Priority priority = Priority.LOW;

    /**
     * The number of ticks for when the animation should end.
     */
    private int ticks;

    /**
     * Constructs an animator for the given entity.
     *
     * @param entity The entity to associate with this animator.
     */
    public Animator(Entity entity) {
        this.entity = entity;
    }

    /**
     * Enum representing the priority of an animation or graphic.
     */
    public enum Priority {

        /**
         * Low priority.
         */
        LOW,

        /**
         * Medium priority.
         */
        MID,

        /**
         * High priority.
         */
        HIGH,

        /**
         * Very high priority.
         */
        VERY_HIGH;
    }

    /**
     * Applies the given animation to the entity.
     *
     * @param animation The animation to apply.
     * @return True if the animation was successfully applied, false otherwise.
     */
    public boolean animate(Animation animation) {
        return animate(animation, null);
    }

    /**
     * Applies the given graphics to the entity.
     *
     * @param graphics The graphics to apply.
     * @return True if the graphics were successfully applied, false otherwise.
     */
    public boolean graphics(Graphics graphics) {
        return animate(null, graphics);
    }

    /**
     * Applies both an animation and/or graphics to the entity. This method manages the timing and priority.
     *
     * @param animation The animation to apply (can be null).
     * @param graphics  The graphics to apply (can be null).
     * @return True if the animation/graphics were successfully applied, false otherwise.
     */
    public boolean animate(Animation animation, Graphics graphics) {
        if (animation != null) {
            if (ticks > GameWorld.getTicks() && priority.ordinal() > animation.getPriority().ordinal()) {
                return false;
            }
            if (animation.getId() == 0) {
                animation.setId(-1);
            }
            this.animation = animation;
            if (animation.getId() != -1) {
                ticks = GameWorld.getTicks() + animation.getDuration();
            } else {
                ticks = 0;
            }
            entity.clocks[Clocks.getANIMATION_END()] = ticks;
            entity.getUpdateMasks().register(EntityFlag.Animate, animation);
            priority = animation.getPriority();
        }
        if (graphics != null) {
            this.graphics = graphics;
            entity.getUpdateMasks().register(EntityFlag.SpotAnim, graphics);
        }
        return true;
    }

    /**
     * Stops the current animation and resets it to the default (RESET_A).
     */
    public void stop() {
        animate(RESET_A);
    }

    /**
     * Forces the application of a specific animation, overriding the priority.
     *
     * @param animation The animation to force.
     */
    public void forceAnimation(Animation animation) {
        ticks = -1;
        animate(animation);
        priority = Priority.HIGH;
    }

    /**
     * Resets the animation and graphics to their default state.
     */
    public void reset() {
        animate(RESET_A);
        entity.clocks[Clocks.getANIMATION_END()] = 0;
        ticks = 0;
    }

    /**
     * Checks if the entity is currently animating.
     *
     * @return True if the entity is currently animating, false otherwise.
     */
    public boolean isAnimating() {
        return animation != null && animation.getId() != -1 && ticks > GameWorld.getTicks();
    }

    /**
     * Gets the current animation applied to the entity.
     *
     * @return The current animation.
     */
    public Animation getAnimation() {
        return animation;
    }

    /**
     * Sets a new animation for the entity.
     *
     * @param animation The animation to set.
     */
    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    /**
     * Gets the current graphic applied to the entity.
     *
     * @return The current graphics.
     */
    public Graphics getGraphics() {
        return graphics;
    }

    /**
     * Sets a new graphic for the entity.
     *
     * @param graphics The graphics to set.
     */
    public void setGraphics(Graphics graphics) {
        this.graphics = graphics;
    }

    /**
     * Gets the priority of the current animation or graphics.
     *
     * @return The priority of the current animation/graphics.
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * Sets the priority for the animation or graphics.
     *
     * @param priority The priority to set.
     */
    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}