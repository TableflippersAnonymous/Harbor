package xyz.nkomarn.harbor.afk;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.harbor.Harbor;

/**
 * Provides a basic contract between AFK plugins and {@link xyz.nkomarn.harbor.util.PlayerManager} to find if a player's AFK
 */
public interface IAfkTracker {

    /**
     * Tries to initialize all the trackers in a sensible order and returns the first one that can track AFK players
     * @param harbor
     * @return the IAfkTracker that responded
     */
    static IAfkTracker pickBestAvailable(final Harbor harbor) {
        IAfkTracker afkTracker = new EssentialsAfkTrackerImpl(harbor);
        if(afkTracker.isReady()) return afkTracker;
        afkTracker = new AFKPlusAfkTrackerImpl(harbor);
        if(afkTracker.isReady()) return afkTracker;
        harbor.getLogger().warning("No supported AFK plugins detected - using built-in detection system");
        return new FallbackAfkTrackerImpl(harbor);
    }

    /**
     * Query whether a player is marked as AFK
     * @param player The player to check
     * @return Whether the player is considered AFK.
     */
    boolean isAfk(@NotNull Player player);

    /**
     * Check if this implementation can track AFK players
     * @return false if the underlaying tracking plugin is unavailable
     */
    boolean isReady();
}
