import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ChatServiceTest {
    private lateinit var chatService: ChatService

    @Before
    fun setUp() {
        chatService = ChatService()
    }

    @Test
    fun createMessage_existingChat_chatHasMessage() {
        // Arrange
        val userId = 1
        val chatId = 1
        val content = "Hello!"

        // Act
        chatService.createMessage(userId, chatId, content)

        // Assert
        val chat = chatService.getChats(userId).find { it.id == chatId }
        val messages = chat?.messages
        assertEquals(1, messages?.size)
        assertEquals(content, messages?.first()?.content)
    }

    @Test
    fun createMessage_newChat_chatIsCreated() {
        // Arrange
        val userId = 1
        val chatId = 1
        val content = "Hello!"

        // Act
        chatService.createMessage(userId, chatId, content)

        // Assert
        val chat = chatService.getChats(userId).find { it.id == chatId }
        assertTrue(chat != null)
    }

    @Test
    fun deleteMessage_existingMessage_messageIsDeleted() {
        // Arrange
        val userId = 1
        val chatId = 1
        val messageId = 1
        val content = "Hello!"
        chatService.createMessage(userId, chatId, content)

        // Act
        chatService.deleteMessage(userId, chatId, messageId)

        // Assert
        val chat = chatService.getChats(userId).find { it.id == chatId }
        val messages = chat?.messages
        assertEquals(0, messages?.size)
    }

    @Test
    fun deleteMessage_nonExistingMessage_noChange() {
        // Arrange
        val userId = 1
        val chatId = 1
        val messageId = 1
        val content = "Hello!"
        chatService.createMessage(userId, chatId, content)

        // Act
        chatService.deleteMessage(userId, chatId, messageId + 1)

        // Assert
        val chat = chatService.getChats(userId).find { it.id == chatId }
        val messages = chat?.messages
        assertEquals(1, messages?.size)
    }
}
