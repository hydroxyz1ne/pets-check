package com.example.petscheck;

import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;

public class CustomWebChromeClient extends WebChromeClient {
    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        // Фильтрация сообщений консоли
        if (!consoleMessage.message().contains("JQMIGRATE")) {
            Log.i("WebViewConsole", consoleMessage.message() + " -- From line "
                    + consoleMessage.lineNumber() + " of "
                    + consoleMessage.sourceId());
        }
        return true;
    }
}