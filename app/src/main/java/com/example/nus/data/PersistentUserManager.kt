package com.example.nus.data

import android.content.Context
import com.example.nus.model.Client
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.util.UUID

class PersistentUserManager(private val context: Context) {
    private val prefs = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    private val gson = Gson()
    
    companion object {
        @Volatile
        private var INSTANCE: PersistentUserManager? = null
        
        fun getInstance(context: Context): PersistentUserManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PersistentUserManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
    
    fun addUser(firstName: String, lastName: String, email: String) {
        val users = getAllUsers().toMutableList()
        
        // 检查是否已存在
        if (users.any { it.email == email }) {
            println("PersistentUserManager: User with email $email already exists")
            return
        }
        
        val newUser = Client(
            clientId = UUID.randomUUID().toString(),
            firstName = firstName,
            lastName = lastName,
            email = email,
            linkedDate = LocalDateTime.now()
        )
        
        users.add(newUser)
        saveUsers(users)
        println("PersistentUserManager: Added user: ${newUser.displayName} (${newUser.email})")
    }
    
    fun getAllUsers(): List<Client> {
        val usersJson = prefs.getString("users", null)
        return if (usersJson != null) {
            try {
                val type = object : TypeToken<List<ClientData>>() {}.type
                val clientDataList: List<ClientData> = gson.fromJson(usersJson, type) ?: emptyList()
                clientDataList.map { it.toClient() }
            } catch (e: Exception) {
                println("PersistentUserManager: Error parsing users from SharedPreferences: ${e.message}")
                val defaultUsers = getDefaultUsers()
                saveUsers(defaultUsers)
                defaultUsers
            }
        } else {
            val defaultUsers = getDefaultUsers()
            saveUsers(defaultUsers)
            defaultUsers
        }
    }
    
    fun getUserByEmail(email: String): Client? {
        return getAllUsers().find { it.email == email }
    }
    
    fun removeUser(email: String) {
        val users = getAllUsers().toMutableList()
        val removed = users.removeAll { it.email == email }
        if (removed) {
            saveUsers(users)
            println("PersistentUserManager: Removed user with email: $email")
        }
    }
    
    fun clearAllUsers() {
        prefs.edit().remove("users").apply()
        println("PersistentUserManager: Cleared all users")
    }
    
    private fun saveUsers(users: List<Client>) {
        try {
            val clientDataList = users.map { ClientData.fromClient(it) }
            val usersJson = gson.toJson(clientDataList)
            prefs.edit().putString("users", usersJson).apply()
            println("PersistentUserManager: Saved ${users.size} users to SharedPreferences")
        } catch (e: Exception) {
            println("PersistentUserManager: Error saving users: ${e.message}")
        }
    }
    
    private fun getDefaultUsers(): List<Client> {
        return listOf(
            Client(
                clientId = "default-1",
                firstName = "Joe",
                lastName = "",
                email = "joe@email.com",
                linkedDate = LocalDateTime.of(2025, 7, 21, 0, 0)
            ),
            Client(
                clientId = "default-2",
                firstName = "Rick",
                lastName = "Deckard",
                email = "rick.deckard@email.com",
                linkedDate = LocalDateTime.of(2025, 7, 22, 0, 0)
            ),
            Client(
                clientId = "default-3",
                firstName = "Tony",
                lastName = "Soprano",
                email = "tony.soprano@email.com",
                linkedDate = LocalDateTime.of(2025, 7, 23, 0, 0)
            ),
            Client(
                clientId = "default-4",
                firstName = "Alice",
                lastName = "Johnson",
                email = "alice.johnson@email.com",
                linkedDate = LocalDateTime.of(2025, 7, 24, 0, 0)
            ),
            Client(
                clientId = "default-5",
                firstName = "Bob",
                lastName = "Smith",
                email = "bob.smith@email.com",
                linkedDate = LocalDateTime.of(2025, 7, 25, 0, 0)
            ),
            Client(
                clientId = "default-6",
                firstName = "Carol",
                lastName = "Davis",
                email = "carol.davis@email.com",
                linkedDate = LocalDateTime.of(2025, 7, 26, 0, 0)
            )
        )
    }
}

// 用于JSON序列化的数据类，因为LocalDateTime需要特殊处理
data class ClientData(
    val clientId: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val linkedDateString: String // ISO格式的日期字符串
) {
    fun toClient(): Client {
        return Client(
            clientId = clientId,
            firstName = firstName,
            lastName = lastName,
            email = email,
            linkedDate = LocalDateTime.parse(linkedDateString)
        )
    }
    
    companion object {
        fun fromClient(client: Client): ClientData {
            return ClientData(
                clientId = client.clientId,
                firstName = client.firstName,
                lastName = client.lastName,
                email = client.email,
                linkedDateString = client.linkedDate.toString()
            )
        }
    }
}
