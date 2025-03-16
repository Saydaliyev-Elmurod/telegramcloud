# Telegram Cloud Bot

Telegram Cloud Bot is a powerful tool that allows you to store, manage,
and organize your files, videos, images, and other media files directly within Telegram.
It functions like a cloud storage system, enabling you to access your data anytime and anywhere.

## Features

- **User-Friendly Interface**: Manage your files similarly to the "My Files" application on your computer.
- **Store Files**: Save videos, images, documents, and other media files.
- **Search & Retrieve**: Quickly find and access your stored files by file name or using **Telegram's inline query
  search**.
- **Organize**: Create folders and rename files as needed.
- **Delete Files**: Remove unwanted files with easy.

## How It Works

1. **Start the Bot**: When you first start the bot, a "My Files" folder will be created automatically.
2. **Create New Folders**: Click the **+** button to create a new folder.
3. **Upload Files**: Send files to the bot to store them securely.
4. **Manage Files**: Rename, search, and delete files as needed.

## Installation & Usage
If you want to run this bot on your own:
- First, create a Telegram bot using BotFather ([Tutorial](https://core.telegram.org/bots/tutorial)).
- Then, update the `application.yaml` file with the following:

```yaml
telegram.username={bot_username}  
telegram.token={bot_token}  
pageable.size = 8  
```

The `pageable.size` parameter determines the number of elements per page.

Create a new database using PostgreSQL and update the `application.yaml` file with the required credentials.
```yaml
spring.datasource.url=jdbc:postgresql://localhost:5434/{yourdatabase}
spring.datasource.username={db_username}
spring.datasource.password={db_password}
```

More information about  this bot https://telegra.ph/Telegram-Cloud-bot-05-24-2

 
