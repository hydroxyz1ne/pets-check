package com.example.petscheck;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import org.json.JSONException;

public class TestActivity extends AppCompatActivity {

    private TelegramBotClient botClient;
    private EditText editTextMessage;
    private TextView textViewMessage;
    private Handler handler = new Handler();

    // Идентификатор чата для отправки и получения сообщений
    private static final String CHAT_ID = "6318519674";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        // Инициализация TelegramBotClient с вашим токеном
        String token = "6405019616:AAFJXHyvkZc5eRJpThKbZhISIDYVe_fzyOM";
        botClient = new TelegramBotClient(token);

        // Инициализация элементов интерфейса
        editTextMessage = findViewById(R.id.editTextMessage);
        textViewMessage = findViewById(R.id.textViewMessage);
        Button sendButton = findViewById(R.id.sendButton);

        // Обработчик нажатия кнопки отправки сообщения
        sendButton.setOnClickListener(view -> {
            // Получаем сообщение, введенное пользователем
            String userMessage = editTextMessage.getText().toString();
            // Отправляем сообщение в чат
            new SendMessageTask().execute(CHAT_ID, userMessage);
        });

        // Начинаем опрос
        startPolling();
    }

    private void startPolling() {
        // Начинаем опрос каждые 2 секунды
        handler.postDelayed(pollingTask, 2000);
    }

    private Runnable pollingTask = new Runnable() {
        @Override
        public void run() {
            // Получаем последнее сообщение
            new GetLatestMessageTask().execute();

            // Повторяем опрос через 2 секунды
            handler.postDelayed(this, 2000);
        }
    };

    // Класс для отправки сообщения
    private class SendMessageTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String chatId = params[0];
            String message = params[1];

            try {
                botClient.sendMessage(chatId, message);
                return true;
            } catch (IOException e) {
                Log.e("SendMessageTask", "Failed to send message: " + e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Log.d("SendMessageTask", "Message sent successfully.");
            } else {
                Log.e("SendMessageTask", "Failed to send message.");
            }
        }
    }

    // Класс для получения последнего сообщения
    private class GetLatestMessageTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                return botClient.getLatestMessage();
            } catch (IOException | JSONException e) {
                Log.e("GetLatestMessageTask", "Failed to get message: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String latestMessage) {
            if (latestMessage != null) {
                // Обновляем TextView с последним сообщением
                textViewMessage.setText(latestMessage);
            }
        }
    }
}
