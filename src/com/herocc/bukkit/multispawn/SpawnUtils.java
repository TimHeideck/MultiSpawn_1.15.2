package com.herocc.bukkit.multispawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class SpawnUtils {
  private final MultiSpawn plugin = MultiSpawn.getPlugin();

  public Location getSpawnLocation(String name){
    String[] loc = plugin.getConfig().getString("spawns." + name + ".loc").split("\\,");
    World w = Bukkit.getWorld(loc[0]);
    Double x = Double.parseDouble(loc[1]);
    Double y = Double.parseDouble(loc[2]);
    Double z = Double.parseDouble(loc[3]);
    float yaw = Float.parseFloat(loc[4]);
    float pitch = Float.parseFloat(loc[5]);
    return new Location(w, x, y, z, yaw, pitch);
  }

  public ArrayList<String> getDisallowedWorlds(String spawnName){
    return new ArrayList<>(plugin.getConfig().getStringList("spawns." + spawnName + ".bannedWorlds"));
  }

  /**
   * Sets a spawn
   * @param loc Location of the new spawn
   * @param name Name of the new spawn
   */
  public void setSpawn(Location loc, String name) {
    String location = loc.getWorld().getName() + "," + loc.getX() + "," + (loc.getY() + 0.6) + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch();
    plugin.getConfig().set("spawns." + name + ".loc", location);
    plugin.saveConfig();
  }

  /**
   * Removes a spawn from the config
   * @param name Name of the spawn to remove
   */
  public void removeSpawn(String name){
    plugin.getConfig().set("spawns." + name, null);
    plugin.saveConfig();
  }

  /**
   * Gets all spawns on the server
   * @return All spawns on the server
   */
  public ArrayList<String> getSpawns(){
    Set<String> spawns = plugin.getConfig().getConfigurationSection("spawns").getKeys(false);
    ArrayList<String> spawnsNew = new ArrayList<>();
    spawnsNew.addAll(spawns);
    return spawnsNew;
  }

  /**
   * Get all spawns that the player is allowed to teleport to
   * @param p Who to check spawns for
   * @param fromAllWorlds Should we check if the player is in a banned world
   * @return All spawns the player is allowed to teleport to
   */
  public ArrayList<String> getSpawns(CommandSender p, boolean fromAllWorlds){
    ArrayList<String> allowedSpawns = new ArrayList<>();
    for (String name : getSpawns()) {
      if (p.hasPermission("multispawn.spawn." + name)){ allowedSpawns.add(name); }
    }
    if (p instanceof Player && !fromAllWorlds && !p.hasPermission("multispawn.bypassBannedWorlds")) {
      Player player = (Player) p;
      String currentWorld = player.getWorld().getName();
      for (String spawnName : allowedSpawns){
        if (getDisallowedWorlds(spawnName).contains(currentWorld)) { allowedSpawns.remove(spawnName); }
      }
    }
    Collections.shuffle(allowedSpawns, plugin.random); // Randomize the Array if there are multiple possible spawns
    if (allowedSpawns.size() == 0 && plugin.getConfig().getBoolean("useDefaultAsFallback", true) && (getSpawnLocation("default") != null)) {
      // In future updates, possibly use world spawn rather than default?
      allowedSpawns.add("default");
    }
    return allowedSpawns;
  }

  /**
   * Gets a random allowed spawn
   * @param p The player to get the spawn for
   * @return A random spawn name
   */
  public String getRandomSpawn(Player p){ return getSpawns(p, false).get(0); }

  public int getNumberOfSpawns(){
    try { getSpawns().size(); } catch (NullPointerException e) { return 0; }
    return getSpawns().size();
  }

  public void sendPlayerToSpawn(Player p, String spawn){
    if ("random".equals(spawn) && !getSpawns().contains(spawn)) {
      p.teleport(getSpawnLocation(getRandomSpawn(p)));
    } else {
      p.teleport(getSpawnLocation(spawn));
    }
  }

  public void sendPlayerToSpawn(Player p){
    if (getSpawns(p, false).size() != 0) {
      sendPlayerToSpawn(p, getRandomSpawn(p));
    }
  }

  public String getSpawn(int index){ return getSpawns().get(index); }

}
