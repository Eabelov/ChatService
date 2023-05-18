fun main() {
    val chatService = ChatService()

    chatService.createMessage(1, 1, "Привет!")
    chatService.createMessage(2, 1, "Привет, как дела?")
    chatService.createMessage(1, 2, "Hi!")
    chatService.createMessage(2, 2, "Hi, how are you?")

    val chatsUser1 = chatService.getChats(1)
    println("Чаты пользователя 1:")
    for (chat in chatsUser1) {
        println("ID чата: ${chat.id}, ID пользователя: ${chat.userId}")
    }

    val unreadChatsCountUser2 = chatService.getUnreadChatsCount(2)
    println("Количество непрочитанных чатов для пользователя 2: $unreadChatsCountUser2")

    val lastMessagesUser1 = chatService.getLastMessages(1)
    println("Последние сообщения пользователя 1:")
    for (message in lastMessagesUser1) {
        println(message)
    }

    val messagesFromChatUser1 = chatService.getMessagesFromChat(1, 1, 0, 5)
    println("Сообщения из чата для пользователя 1:")
    for (message in messagesFromChatUser1) {
        println("ID сообщения: ${message.id}, ID отправителя: ${message.senderId}, Текст: ${message.text}")
    }

    chatService.deleteMessage(1, 1, 1)
    chatService.deleteChat(2)

    val updatedChatsUser1 = chatService.getChats(1)
    println("Обновленные чаты пользователя 1:")
    for (chat in updatedChatsUser1) {
        println("ID чата: ${chat.id}, ID пользователя: ${chat.userId}")
    }
}

data class Message(val id: Int, val senderId: Int, var text: String, var isRead: Boolean)

data class Chat(val id: Int, val userId: Int, val messages: MutableList<Message> = mutableListOf())

class ChatService(private val chats: MutableList<Chat> = mutableListOf()) {
    private var chatIdCounter: Int = 0
    private var messageIdCounter: Int = 0

    private fun createChat(userId: Int) {
        val chatId = ++chatIdCounter
        val chat = Chat(chatId, userId)
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
        val chat = chats.find { it.userId == userId && it.id == chatId } ?: run {
            createChat(userId)
            return emptyList()
        }

        val messages = chat.messages
            .filter { it.id > lastMessageId }
            .take(count)

        messages.forEach { it.isRead = true }

        return messages
    }

    fun createMessage(userId: Int, chatId: Int, text: String) {
        val chat = chats.find { it.userId == userId && it.id == chatId } ?: return
        val messageId = ++messageIdCounter
        val message = Message(messageId, userId, text, isRead = true)
        chat.messages.add(message)
    }

    fun deleteMessage(userId: Int, chatId: Int, messageId: Int) {
        val chat = chats.find { it.userId == userId && it.id == chatId } ?: return
        chat.messages.removeAll { it.id == messageId }
    }
}