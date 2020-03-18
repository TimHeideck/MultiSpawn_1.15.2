package com.herocc.bukkit.multispawn.events;

import com.herocc.bukkit.multispawn.MultiSpawn;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
  private final MultiSpawn plugin = MultiSpawn.getPlugin();

  @EventHandler
  @SuppressWarnings("unused")
  // PlayerJoinEvent is a successful login, PlayerLoginEvent is when player pushes join
  // https://bukkit.org/threads/difference-between-playerjoinevent-and-playerloginevent.76836/#post-1129849
  public void onPlayerJoin(PlayerJoinEvent ev) {
    final Player p = ev.getPlayer();
    if (p.hasPermission("multispawn.noteleport") // If player is excluded
        || plugin.getSpawnUtils().getSpawns(p, false) == null // If spawns are null
        || plugin.getSpawnUtils().getSpawns(p, false).isEmpty()) return; // If spawns are empty
  
    if (plugin.getSpawnUtils().getSpawns(p, true).size() == 1
        && plugin.getSpawnUtils().getSpawns(p, true).contains("default")
        && !plugin.getConfig().getBoolean("useDefaultAsFallback", true)) return;
        
    plugin.getSpawnUtils().sendPlayerToSpawn(p);
  }
}
