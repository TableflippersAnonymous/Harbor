package xyz.nkomarn.harbor.afk;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.harbor.Harbor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * FallbackAfkTracker has no backing plugin
 * It uses the original Harbor fallback logic, updating a Map on every tracked event
 * Used when AFK detection is enabled, but no other supported plugins are available
 */
public final class FallbackAfkTrackerImpl implements Listener, IAfkTracker {

    private final Harbor harbor;
    private final Map<UUID, Long> playerActivity;

    public FallbackAfkTrackerImpl(final Harbor harbor) {
        this.harbor = harbor;
        this.playerActivity = new HashMap<>();
        this.harbor.getServer().getPluginManager().registerEvents(this, this.harbor);
    }

    @Override
    public boolean isAfk(@NotNull final Player player) {
        if (!playerActivity.containsKey(player.getUniqueId())) return false;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - playerActivity.get(player.getUniqueId()));
        return minutes >= harbor.getConfiguration().getInteger("afk-detection.timeout");
    }

    @Override
    public boolean isReady() {
        return true;
    }

    /**
     * Sets the given player's last activity to the current timestamp.
     *
     * @param player The player to update.
     */
    public void updateActivity(@NotNull Player player) {
        playerActivity.put(player.getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event) {
        updateActivity(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        updateActivity(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent event) {
        updateActivity(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onInventoryClick(InventoryClickEvent event) {
        updateActivity((Player) event.getWhoClicked());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        playerActivity.remove(event.getPlayer().getUniqueId());
    }
}
