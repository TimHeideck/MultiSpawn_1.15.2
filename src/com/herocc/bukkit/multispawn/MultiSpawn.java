package com.herocc.bukkit.multispawn;

import com.herocc.bukkit.multispawn.commands.*;
import com.herocc.bukkit.multispawn.events.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class MultiSpawn extends JavaPlugin  {
  private static MultiSpawn instance;
  protected Random random = new Random();

  public static final MultiSpawn getPlugin() { return instance; }
  public SpawnUtils getSpawnUtils() { return new SpawnUtils(); }

  @Override
  public void onEnable(){
    instance = this;

    getConfig().options().copyDefaults(true);
    this.saveConfig();

    this.getCommand("spawn").setExecutor(new SpawnCommand());
    this.getCommand("setspawn").setExecutor(new SetSpawn());
    this.getCommand("removespawn").setExecutor(new RemoveSpawn());
    this.getCommand("listspawns").setExecutor(new ListSpawns());

    this.getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
    this.getServer().getPluginManager().registerEvents(new PlayerDeath(), this);

  }

  @Override
  public void onDisable(){
    this.saveConfig();
  }
}

