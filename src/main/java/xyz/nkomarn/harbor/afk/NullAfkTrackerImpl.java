package xyz.nkomarn.harbor.afk;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * NullAfkTracker has no backing plugin, marking everyone as not AFK.
 * Used when AFK detection is disabled.
 */
public class NullAfkTrackerImpl implements IAfkTracker {
    @Override
    public boolean isAfk(@NotNull final Player player) {
        return false;
    }

    @Override
    public boolean isReady() {
        return true;
    }
}
