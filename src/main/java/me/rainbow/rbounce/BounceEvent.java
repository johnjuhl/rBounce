package me.rainbow.rbounce;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

/**
 * User: rainbow
 * Date: 02/01/13
 * Time: 13:28
 */
public class BounceEvent {
    final private Block pressurePlate;
    final private Block bounceBlock;
    final private Entity triggeringEntity;

    /**
     * Event for bouncing.
     *
     * @param pressurePlate    Triggering pressure plate
     * @param bounceBlock      Block below pressurePlate
     * @param triggeringEntity Player who triggered the event
     */
    public BounceEvent(Block pressurePlate, Block bounceBlock, Entity triggeringEntity) {
        this.pressurePlate = pressurePlate;
        this.bounceBlock = bounceBlock;
        this.triggeringEntity = triggeringEntity;
    }

    public Block getPressurePlate() {
        return pressurePlate;
    }

    public Block getBounceBlock() {
        return bounceBlock;
    }

    public Entity getTriggeringEntity() {
        return triggeringEntity;
    }
}
