# logback.telegram
Logback Appender for Telegram

## Info

This library will be send log messages of your application into Telegram chat or user

## Usage

```
<appender name="telegram" class="gcg.logback.TelegramAppender">
    <minimumEventLevel>LOG_LEVEL</minimumEventLevel>
    <options>
      <key>BOT_KEY</key>
      <chat>CHAT_ID</chat>
      <app>APP_NAME</app>
    </options>
  </appender>
 ```

Set these variables to your values:

`LOG_LEVEL` - standart logback levels (DEBUG, TRACE, INFO, WARN, ERROR), default is ERROR;

`BOT_KEY` - api key of telegram bot;

`CHAT_ID` - chat_id or user_id where messages will be sent;

`APP_NAME` - your (preferably unique) application name.
