fun main() {
    val service = ChatService()

    // Создание чата
    service.createChat(1)
    service.createChat(2)
    service.createChat(1)

    // Получение списка чатов для пользователя
    val userChats = service.getChats(1)
    println("Список чатов для пользователя 1:")
    userChats.forEach { println(it) }
    println()

    // Получение количества непрочитанных чатов
    val unreadChatsCount = service.getUnreadChatsCount(1)
    println("Количество непрочитанных чатов для пользователя 1: $unreadChatsCount")
    println()

    // Создание сообщений
    service.createMessage(1, 1, "Привет!")
    service.createMessage(2, 1, "Привет!!!")
    service.createMessage(1, 2, "Hi!")
    service.createMessage(1, 1, "Hi!!!")
    service.createMessage(2, 1, "Это сообщение будет удалено.")
    service.createMessage(1, 3, "Третий чат.")
    // Получение списка последних сообщений
    val lastMessages = service.getLastMessages(1)
    println("Список последних сообщений для пользователя 1:")
    lastMessages.forEach { println(it) }
    println()

// Получение сообщений из чата
    val messages = service.getMessagesFromChat(1, 1, 0, 10)
    println("Сообщения из чата 1:")
    messages.forEach { println(it) }
    println()

// Удаление сообщения
    service.deleteMessage(1, 1, 2)

// Удаление чата
    service.deleteChat(1)

// Получение списка чатов после удаления
    val remainingChats = service.getChats(1)
    println("Оставшиеся чаты для пользователя 1:")
    remainingChats.forEach { println(it) }
}

data class Message(val id: Int, val senderId: Int, var text: String, var isRead: Boolean)

data class Chat(val id: Int, val userId: Int, val messages: MutableList<Message> = mutableListOf())

class ChatService(private val chats: MutableList<Chat> = mutableListOf()) {
    fun createChat(userId: Int) {
        val chat = Chat(chats.size + 1, userId)
        chats.add(chat)
    }

    fun deleteChat(chatId: Int) {
        chats.removeAll { it.id == chatId }
    }

    fun getChats(userId: Int): List<Chat> {
        return chats.filter { it.userId == userId }
    }

    fun getUnreadChatsCount(userId: Int): Int {
        return chats.count { chat ->
            chat.userId == userId && chat.messages.any { !it.isRead }
        }
    }

    fun getLastMessages(userId: Int): List<String> {
        return chats
            .filter { it.userId == userId }
            .mapNotNull { chat ->
                chat.messages.lastOrNull()?.text ?: "нет сообщений"
            }
    }

    fun getMessagesFromChat(userId: Int, chatId: Int, lastMessageId: Int, count: Int): List<Message> {
        val chat = chats.find { it.userId == userId && it.id == chatId } ?: return emptyList()

        chat.messages.forEach { message ->
            if (message.id > lastMessageId) {
                message.isRead = true
            }
        }

        return chat.messages
            .filter { it.id > lastMessageId }
            .take(count)
    }

    fun createMessage(userId: Int, chatId: Int, text: String) {
        val chat = chats.find { it.userId == userId && it.id == chatId } ?: return
        val messageId = chat.messages.size + 1
        val message = Message(messageId, userId, text, isRead = true)
        chat.messages.add(message)
    }

    fun deleteMessage(userId: Int, chatId: Int, messageId: Int) {
        val chat = chats.find { it.userId == userId && it.id == chatId } ?: return
        chat.messages.removeAll { it.id == messageId }
    }
}
