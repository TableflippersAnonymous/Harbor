package xyz.nkomarn.harbor.afk;

import net.lapismc.afkplus.AFKPlus;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.harbor.Harbor;

/**
 * AFKPlusAfkTracker depends on AFKPlus
 */
public class AFKPlusAfkTrackerImpl implements IAfkTracker {
    private final AFKPlus afkPlus;

    public AFKPlusAfkTrackerImpl(final Harbor harbor) {
        afkPlus = (AFKPlus) harbor.getServer().getPluginManager().getPlugin("AFKPlus");
    }

    @Override
    public boolean isAfk(@NotNull final Player player) {
        return afkPlus.getPlayer(player.getUniqueId()).isAFK();
    }

    @Override
    public boolean isReady() {
        return afkPlus != null && afkPlus.isEnabled();
    }
}
