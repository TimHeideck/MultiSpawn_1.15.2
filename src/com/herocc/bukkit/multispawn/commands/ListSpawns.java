package com.herocc.bukkit.multispawn.commands;

import com.herocc.bukkit.multispawn.MultiSpawn;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ListSpawns implements CommandExecutor {
  private final MultiSpawn plugin = MultiSpawn.getPlugin();

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (sender.hasPermission("multispawn.list")){
      if (plugin.getSpawnUtils().getNumberOfSpawns() == 0) {
        sender.sendMessage(ChatColor.RED + "There are no configured spawns!");
        return true;
      }
      sender.sendMessage(ChatColor.GREEN + "There are " + plugin.getSpawnUtils().getNumberOfSpawns() + " spawns:");
      for (int i=0; i<plugin.getSpawnUtils().getNumberOfSpawns(); i++){
        String spawn = plugin.getSpawnUtils().getSpawn(i);
        Location spawnLoc = plugin.getSpawnUtils().getSpawnLocation(spawn);
        ChatColor spawnNameColor = sender.hasPermission("multispawn.spawn." + spawn) ? ChatColor.GREEN : ChatColor.RED;
        sender.sendMessage(spawnNameColor + Integer.toString(i+1) + ". " + spawn + ChatColor.GREEN + ": " +
            spawnLoc.getWorld().getName() + ", " + spawnLoc.getBlockX() + "X, " + spawnLoc.getBlockY() + "Y, " + spawnLoc.getBlockZ() + "Z");
      }
    } else {
      sender.sendMessage(ChatColor.RED + "You don't have permission to list spawns!");
    }
    return true;
  }

}
