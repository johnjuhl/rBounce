package me.rainbow.rbounce;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.mcstats.Metrics;

import java.io.IOException;
import java.util.ArrayList;

public class Core extends JavaPlugin implements Listener {

    public void onDisable() {
        // TODO: Place any custom disable code here.
    }

    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        try {
            Metrics metrics = new Metrics(this);
            metrics.start();

        } catch (IOException e) {
            Bukkit.getLogger().info("[rBounce]: Metrics failure!");
        }
    }

    ArrayList<Integer> bouncingEntities = new ArrayList<Integer>();

    @EventHandler
    public void onPlateActive(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.PHYSICAL)) {
            if (event.getClickedBlock().getType().equals(Material.STONE_PLATE)) {
                if (event.getClickedBlock().getRelative(BlockFace.DOWN).getType().equals(Material.MOSSY_COBBLESTONE)) {
                    onBounce(new BounceEvent(event.getClickedBlock(), event.getClickedBlock().getRelative(BlockFace.DOWN), event.getPlayer()));
                }
            }
        }
    }

    @EventHandler
    public void onPlateActiveByEnt(EntityInteractEvent event) {
        if (event.getBlock().getType().equals(Material.STONE_PLATE)) {
            if (event.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.MOSSY_COBBLESTONE)) {
                onBounce(new BounceEvent(event.getBlock(), event.getBlock().getRelative(BlockFace.DOWN), event.getEntity()));
            }
        }
    }

    @EventHandler
    public void onHurt(EntityDamageEvent event) {
        if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            Entity e = event.getEntity();
            if (bouncingEntities.contains(e.getEntityId())) {
                event.setCancelled(true);
                e.setFallDistance(0);
                bouncingEntities.remove(bouncingEntities.indexOf(e.getEntityId()));
            }

        }
    }

    public void onBounce(BounceEvent event) {
        Block b = event.getBounceBlock();
        World w = b.getWorld();

        int power = 0;

        for (int i = 0; i < 3; i++) {
            if (w.getBlockAt(b.getX(), b.getY() - i, b.getZ()).getType().equals(Material.MOSSY_COBBLESTONE)) {
                power++;
            }
        }

        final Entity e = event.getTriggeringEntity();
        e.setVelocity(new Vector(0, (2 * power) - 1, 0));

        //Work around for timing issue.
        Bukkit.getScheduler().runTaskLater(this, new Runnable() {
            @Override
            public void run() {
                if (!bouncingEntities.contains(e.getEntityId())) {
                    bouncingEntities.add(e.getEntityId());
                }
            }
        }, 5);
    }
}

