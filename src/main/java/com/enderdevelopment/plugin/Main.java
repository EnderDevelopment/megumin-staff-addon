package com.enderdevelopment.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {
    private static final long COOLDOWN_MILLIS = 30_000L;
    private static final float EXPLOSION_POWER = 6.0F;
    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private NamespacedKey staffKey;

    @Override
    public void onEnable() {
        staffKey = new NamespacedKey(this, "megumin_staff");
        getServer().getPluginManager().registerEvents(this, this);
        if (getCommand("meguminstaff") != null) {
            getCommand("meguminstaff").setExecutor((sender, command, label, args) -> {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage("Only players can receive the staff.");
                    return true;
                }
                player.getInventory().addItem(createStaff());
                player.sendMessage(Component.text("Megumin's Explosion staff received.", NamedTextColor.LIGHT_PURPLE));
                return true;
            });
        }
    }

    @Override
    public void onDisable() {
        cooldowns.clear();
    }

    private ItemStack createStaff() {
        ItemStack staff = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = staff.getItemMeta();
        if (meta == null) {
            return staff;
        }
        meta.displayName(Component.text("Megumin's Explosion Staff", NamedTextColor.DARK_RED)
                .decoration(TextDecoration.BOLD, true));
        meta.lore(java.util.List.of(
                Component.text("Right-click to cast Explosion", NamedTextColor.RED),
                Component.text("Cooldown: 30 seconds", NamedTextColor.GRAY)));
        meta.getPersistentDataContainer().set(staffKey, PersistentDataType.BYTE, (byte) 1);
        staff.setItemMeta(meta);
        return staff;
    }

    private boolean isStaff(ItemStack item) {
        if (item == null || item.getType() != Material.BLAZE_ROD || !item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.getPersistentDataContainer().has(staffKey, PersistentDataType.BYTE);
    }

    @EventHandler
    public void onStaffUse(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Player player = event.getPlayer();
        if (!isStaff(event.getItem())) {
            return;
        }
        event.setCancelled(true);

        long now = System.currentTimeMillis();
        long availableAt = cooldowns.getOrDefault(player.getUniqueId(), 0L);
        if (availableAt > now) {
            long remaining = (availableAt - now + 999L) / 1000L;
            player.sendActionBar(Component.text("Explosion is recharging: " + remaining + "s", NamedTextColor.RED));
            return;
        }

        cooldowns.put(player.getUniqueId(), now + COOLDOWN_MILLIS);
        World world = player.getWorld();
        player.sendMessage(Component.text("EXPLOSION!", NamedTextColor.DARK_RED).decorate(TextDecoration.BOLD));
        world.playSound(player.getLocation(), Sound.ENTITY_GHAST_WARN, 1.0F, 0.6F);
        world.createExplosion(player.getLocation(), EXPLOSION_POWER, false, true, player);
        world.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2.0F, 0.7F);
    }
}
