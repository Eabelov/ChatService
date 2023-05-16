import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class ChatServiceTest {
    @Test
    fun testDeleteChat() {
        // Создаем сервис и добавляем несколько чатов
        val service = ChatService()
        service.createChat(1)
        service.createChat(2)
        service.createChat(3)

        // Удаляем один из чатов
        val chatIdToDelete = 2
        service.deleteChat(chatIdToDelete)

        // Проверяем, что удаленный чат отсутствует в списке чатов
        val chats = service.getChats(1)
        assertFalse(chats.any { it.id == chatIdToDelete })
    }

    @Test
    fun testCreateChat() {
        // Создаем сервис
        val service = ChatService()

        // Создаем новый чат
        val userId = 1
        service.createChat(userId)

        // Получаем список чатов для пользователя
        val chats = service.getChats(userId)

        // Проверяем, что список чатов содержит только один созданный чат
        assertEquals(1, chats.size)
        assertEquals(userId, chats[0].userId)
    }
}
