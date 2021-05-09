package xyz.nkomarn.harbor.util;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.harbor.Harbor;
import xyz.nkomarn.harbor.afk.FallbackAfkTrackerImpl;
import xyz.nkomarn.harbor.afk.IAfkTracker;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager implements Listener {

    private final Harbor harbor;
    private final Map<UUID, Long> cooldowns;
    private final IAfkTracker afkTracker;

    public PlayerManager(@NotNull Harbor harbor) {
        this.harbor = harbor;
        this.cooldowns = new HashMap<>();
        this.afkTracker = harbor.getAfkTracker();
    }

    /**
     * Gets the last tracked cooldown time for a given player.
     *
     * @param player The player for which to return cooldown time.
     * @return The player's last cooldown time.
     */
    public long getCooldown(@NotNull Player player) {
        return cooldowns.getOrDefault(player.getUniqueId(), 0L);
    }

    /**
     * Sets a player's cooldown to a specific, fixed value.
     *
     * @param player   The player for which to set cooldown.
     * @param cooldown The cooldown value.
     */
    public void setCooldown(@NotNull Player player, long cooldown) {
        cooldowns.put(player.getUniqueId(), cooldown);
    }

    /**
     * Resets every players' message cooldown.
     */
    public void clearCooldowns() {
        cooldowns.clear();
    }

    /**
     * Checks if a player is considered "AFK" for Harbor's player checks.
     *
     * @param player The player to check.
     * @return Whether the player is considered AFK.
     */
    public boolean isAfk(@NotNull Player player) {
        return afkTracker.isAfk(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        cooldowns.remove(uuid);
    }

}
