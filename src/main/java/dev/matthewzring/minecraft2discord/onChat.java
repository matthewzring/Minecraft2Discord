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

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 *
 * @author Matthew Ring
 */
public class onChat implements Listener
{
    final String url;
    final String avatarApiUrl;

    public onChat (String url, String avatarApiUrl)
    {
        this.url = url;
        this.avatarApiUrl = avatarApiUrl;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event)
    {
        DiscordWebhook wh = new DiscordWebhook(url);
        String username = event.getPlayer().getDisplayName();
        wh.setUsername(username);
        String message = event.getMessage();
        wh.setContent(message);
        String avatarUrl = String.format(avatarApiUrl, event.getPlayer().getUniqueId());
        wh.setAvatarUrl(avatarUrl);
        try
        {
            wh.execute();
        }
        catch (MalformedURLException e)
        {
            System.out.println("Invalid webhook URL!");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}