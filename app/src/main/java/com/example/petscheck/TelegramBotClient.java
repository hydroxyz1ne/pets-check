package com.example.petscheck;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TelegramBotClient {
    private final String token;
    private final OkHttpClient client = new OkHttpClient();

    public TelegramBotClient(String token) {
        this.token = token;
    }

    // Метод для отправки сообщения в чат
    public void sendMessage(String chatId, String question) throws IOException {
        // Импорт MediaType
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        JSONObject json = null;
        try {
            json = new JSONObject();
            json.put("chat_id", chatId);
            json.put("text", question);
            // Выполните дальнейшие действия с объектом json
        } catch (JSONException e) {
            // Обработка исключения JSONException
            Log.e("TelegramBotClient", "JSON Error: " + e.getMessage());
            // Можете также вывести сообщение об ошибке пользователю, если необходимо
        }

        RequestBody body = RequestBody.create(JSON, json.toString());

        Request request = new Request.Builder()
                .url("https://api.telegram.org/bot" + token + "/sendMessage")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Failed to send question: " + response.message());
        }
    }

    // Метод для получения новых сообщений от Telegram-бота
    public String getLatestMessage() throws IOException, JSONException {
        String latestMessage = null;

        // URL для получения обновлений от бота
        String url = "https://api.telegram.org/bot" + token + "/getUpdates";

        // Создание запроса
        Request request = new Request.Builder()
                .url(url)
                .build();

        // Выполнение запроса и получение ответа
        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            // Получение данных в формате JSON
            String responseData = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseData);

            // Проверка на наличие обновлений (update)
            if (jsonResponse.getBoolean("ok")) {
                JSONArray updates = jsonResponse.getJSONArray("result");

                // Найдите последнее обновление
                if (updates.length() > 0) {
                    JSONObject latestUpdate = updates.getJSONObject(updates.length() - 1);

                    // Проверка на наличие сообщения в обновлении
                    if (latestUpdate.has("message")) {
                        JSONObject message = latestUpdate.getJSONObject("message");
                        if (message.has("text")) {
                            // Возвращаем текст последнего сообщения
                            latestMessage = message.getString("text");
                        }
                    }
                }
            }
        } else {
            throw new IOException("Failed to get updates: " + response.message());
        }

        return latestMessage;
    }
}
