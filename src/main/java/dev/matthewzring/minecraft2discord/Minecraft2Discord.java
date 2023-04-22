/*
 * Copyright 2023 Matthew Ring
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.matthewzring.minecraft2discord;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Matthew Ring
 */
public final class Minecraft2Discord extends JavaPlugin
{
    private String url;
    private FileConfiguration config;

    @Override
    public void onEnable()
    {
        this.saveDefaultConfig();
        config = this.getConfig();
        url = config.getString("url");

        String joinMessage = config.getString("joinMessage");
        getServer().getPluginManager().registerEvents(new onJoin(url, joinMessage), this);

        String quitMessage = config.getString("quitMessage");
        getServer().getPluginManager().registerEvents(new onQuit(url, quitMessage), this);

        String avatarApiUrl = config.getString("avatarApiUrl");
        getServer().getPluginManager().registerEvents(new onChat(url, avatarApiUrl), this);
    }
}