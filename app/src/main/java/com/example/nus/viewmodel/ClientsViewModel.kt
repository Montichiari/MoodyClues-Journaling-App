package com.example.nus.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nus.api.ApiClient
import com.example.nus.data.PersistentUserManager
import com.example.nus.model.Client
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ClientsViewModel(application: Application) : AndroidViewModel(application) {

    private val userManager = PersistentUserManager.getInstance(application)

    private val _clients = MutableStateFlow<List<Client>>(emptyList())
    val clients: StateFlow<List<Client>> = _clients.asStateFlow()

    private val _filteredClients = MutableStateFlow<List<Client>>(emptyList())
    val filteredClients: StateFlow<List<Client>> = _filteredClients.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var counsellorId: String = ""

    init {
        println("ClientsViewModel: 初始化，开始加载用户数据")
        loadUsersData()
    }
    
    fun setCounsellorId(id: String) {
        counsellorId = id
        println("ClientsViewModel: Setting counsellorId = '$id'")
        // 如果有counsellorId，从API加载数据；否则使用本地数据
        if (id.isNotEmpty()) {
            println("ClientsViewModel: 尝试从API加载数据...")
            loadClientsFromApi(id)
        } else {
            println("ClientsViewModel: counsellorId为空，使用本地数据...")
            loadUsersData()
        }
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        filterClients(query)
    }
    
    private fun filterClients(query: String) {
        val allClients = _clients.value
        _filteredClients.value = if (query.isBlank()) {
            allClients
        } else {
            allClients.filter { client ->
                client.displayName.contains(query, ignoreCase = true) ||
                client.email.contains(query, ignoreCase = true)
            }
        }
    }
    
    private fun loadUsersData() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                // 从PersistentUserManager获取用户数据
                val users = userManager.getAllUsers()

                // 如果没有用户数据，添加一些测试数据
                val finalUsers = if (users.isEmpty()) {
                    println("ClientsViewModel: 没有本地用户数据，添加测试数据")
                    createTestClients()
                } else {
                    users
                }

                _clients.value = finalUsers
                _filteredClients.value = finalUsers

                println("ClientsViewModel: Loaded ${finalUsers.size} users (${users.size} from PersistentUserManager)")
            } catch (e: Exception) {
                println("ClientsViewModel: Error loading users: ${e.message}")
                // 如果出错，也提供测试数据
                val testUsers = createTestClients()
                _clients.value = testUsers
                _filteredClients.value = testUsers
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 创建测试客户数据
    private fun createTestClients(): List<Client> {
        return listOf(
            Client(
                clientId = "test-client-1",
                firstName = "Joe",
                lastName = "",
                email = "joe@email.com",
                linkedDate = LocalDateTime.now().minusDays(4)
            ),
            Client(
                clientId = "test-client-2",
                firstName = "Rick",
                lastName = "Deckard",
                email = "rick.deckard@email.com",
                linkedDate = LocalDateTime.now().minusDays(3)
            ),
            Client(
                clientId = "test-client-3",
                firstName = "Tony",
                lastName = "Soprano",
                email = "tony.soprano@email.com",
                linkedDate = LocalDateTime.now().minusDays(2)
            ),
            Client(
                clientId = "test-client-4",
                firstName = "Alice",
                lastName = "Johnson",
                email = "alice.johnson@email.com",
                linkedDate = LocalDateTime.now().minusDays(1)
            )
        )
    }

    // 添加新用户的方法
    fun addUser(firstName: String, lastName: String, email: String) {
        userManager.addUser(firstName, lastName, email)
        // 重新加载数据以更新UI
        loadUsersData()
    }

    // 刷新用户数据的方法
    fun refreshUsers() {
        println("ClientsViewModel: 手动刷新用户数据")
        loadUsersData()
    }

    // 强制加载测试数据
    fun loadTestData() {
        viewModelScope.launch {
            _isLoading.value = true
            val testClients = createTestClients()
            _clients.value = testClients
            _filteredClients.value = testClients
            _isLoading.value = false
            println("ClientsViewModel: 强制加载了 ${testClients.size} 个测试客户")
        }
    }
    
    // 从API加载客户数据
    private fun loadClientsFromApi(counsellorId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                println("ClientsViewModel: Loading clients from API for counsellor: $counsellorId")
                val response = ApiClient.counsellorClientApiService.listClients()

                if (response.isSuccessful) {
                    val journalUsers = response.body() ?: emptyList()
                    val clients = journalUsers.map { it.toClient() }

                    _clients.value = clients
                    _filteredClients.value = clients

                    println("ClientsViewModel: Successfully loaded ${clients.size} clients from API")
                } else {
                    val errorMessage = "API error: ${response.code()} - ${response.message()}"
                    println("ClientsViewModel: $errorMessage")
                    // 如果API失败，回退到本地数据
                    loadUsersData()
                }
            } catch (e: Exception) {
                println("ClientsViewModel: Exception loading clients from API: ${e.message}")
                e.printStackTrace()
                // 如果API调用失败，回退到本地数据
                loadUsersData()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun onJournalClick(client: Client) {
        println("ClientsViewModel: Journal clicked for client: ${client.displayName}")
        // TODO: Navigate to client's journal
    }

    // 测试API连接的方法
    fun testApiConnection() {
        viewModelScope.launch {
            try {
                println("ClientsViewModel: Testing API connection...")
                val response = ApiClient.counsellorClientApiService.listClients()

                if (response.isSuccessful) {
                    val clients = response.body() ?: emptyList()
                    println("ClientsViewModel: API test successful. Found ${clients.size} clients")
                    clients.forEach { client ->
                        println("  - ${client.firstName} ${client.lastName} (${client.email})")
                    }
                } else {
                    println("ClientsViewModel: API test failed: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                println("ClientsViewModel: API test exception: ${e.message}")
                e.printStackTrace()
            }
        }
    }

}
