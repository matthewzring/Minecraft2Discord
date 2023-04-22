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

import org.json.simple.JSONArray;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Matthew Ring
 */
public class DiscordWebhook
{
    private final String url;
    private String content;
    private String username;
    private String avatarUrl;

    public DiscordWebhook(String url)
    {
        this.url = url;
    }

    public void setContent(String content)
    {
        this.content = content.replace("_", "\\_");
        this.content = this.content.replace("\\", "\\\\");
        this.content = this.content.replace("\"", "\\\"");
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setAvatarUrl(String avatarUrl)
    {
        this.avatarUrl = avatarUrl;
    }

    public void execute() throws IOException
    {
        if (this.content == null)
        {
            throw new IllegalArgumentException("There is no webhook content!");
        }

        JSONObject json = new JSONObject();

        json.put("content", this.content);
        json.put("username", this.username);
        json.put("avatar_url", this.avatarUrl);

        JSONObject allowedMentions = new JSONObject();
        allowedMentions.put("parse", new JSONArray[0]);
        json.put("allowed_mentions", allowedMentions);

        URL url = new URL(this.url);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "application/json");
        connection.addRequestProperty("User-Agent", "Minecraft2DiscordWebhook");

        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        OutputStream stream = connection.getOutputStream();
        stream.write(json.toString().getBytes());
        stream.flush();
        stream.close();

        connection.getInputStream().close();
        connection.disconnect();
    }

    private class JSONObject
    {
        void put(String key, Object value)
        {
            if (value != null)
            {
                map.put(key, value);
            }
        }

        private final HashMap<String, Object> map = new HashMap<>();

        @Override
        public String toString()
        {
            StringBuilder builder = new StringBuilder();
            Set<Map.Entry<String, Object>> entrySet = map.entrySet();
            builder.append("{");

            int i = 0;
            for (Map.Entry<String, Object> entry : entrySet)
            {
                Object val = entry.getValue();
                builder.append(quote(entry.getKey())).append(":");

                if (val instanceof String)
                {
                    builder.append(quote(String.valueOf(val)));
                }
                else if (val instanceof Integer)
                {
                    builder.append(Integer.valueOf(String.valueOf(val)));
                }
                else if (val instanceof Boolean)
                {
                    builder.append(val);
                }
                else if (val instanceof JSONObject)
                {
                    builder.append(val);
                }
                else if (val.getClass().isArray())
                {
                    builder.append("[");
                    int len = Array.getLength(val);
                    for (int j = 0; j < len; j++)
                    {
                        builder.append(Array.get(val, j).toString()).append(j != len - 1 ? "," : "");
                    }
                    builder.append("]");
                }

                builder.append(++i == entrySet.size() ? "}" : ",");
            }

            return builder.toString();
        }

        private String quote(String string)
        {
            return "\"" + string + "\"";
        }
    }
}