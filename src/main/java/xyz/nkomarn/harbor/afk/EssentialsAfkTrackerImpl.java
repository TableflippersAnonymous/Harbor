package xyz.nkomarn.harbor.afk;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.harbor.Harbor;

/**
 * EssentialsAfkTracker depends on Essentials or EssentialsX's AFK system, which can be driven by other plugins
 */
public class EssentialsAfkTrackerImpl implements IAfkTracker {

    private final Essentials essentials;

    public EssentialsAfkTrackerImpl(final Harbor harbor) {
        essentials = (Essentials) harbor.getServer().getPluginManager().getPlugin("Essentials");
    }

    @Override
    public boolean isAfk(@NotNull final Player player) {
        User user = essentials.getUser(player);
        if(user != null) return user.isAfk();
        return false;
    }

    @Override
    public boolean isReady() {
        return essentials != null && essentials.isEnabled();
    }
}
