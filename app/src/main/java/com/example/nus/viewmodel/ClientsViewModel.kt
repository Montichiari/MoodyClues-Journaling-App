package com.example.nus.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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
        loadUsersData()
    }
    
    fun setCounsellorId(id: String) {
        counsellorId = id
        println("ClientsViewModel: Setting counsellorId = '$id'")
        // 重新加载用户数据
        loadUsersData()
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

                _clients.value = users
                _filteredClients.value = users

                println("ClientsViewModel: Loaded ${users.size} users from PersistentUserManager")
            } catch (e: Exception) {
                println("ClientsViewModel: Error loading users: ${e.message}")
                _clients.value = emptyList()
                _filteredClients.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 添加新用户的方法
    fun addUser(firstName: String, lastName: String, email: String) {
        userManager.addUser(firstName, lastName, email)
        // 重新加载数据以更新UI
        loadUsersData()
    }

    // 刷新用户数据的方法
    fun refreshUsers() {
        loadUsersData()
    }
    
    // 保留API方法以备将来使用
    private fun loadClientsFromApi(counsellorId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // API call will go here when backend is ready
                // val response = apiService.getClients(counsellorId)
                // _clients.value = response.map { it.toClient() }
                // _filteredClients.value = _clients.value
                println("ClientsViewModel: API call not implemented yet, using persistent data")
                loadUsersData()
            } catch (e: Exception) {
                println("ClientsViewModel: Error loading clients: ${e.message}")
                loadUsersData() // Fallback to persistent data
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun onJournalClick(client: Client) {
        println("ClientsViewModel: Journal clicked for client: ${client.displayName}")
        // TODO: Navigate to client's journal
    }
    
    fun onDashboardClick(client: Client) {
        println("ClientsViewModel: Dashboard clicked for client: ${client.displayName}")
        // TODO: Navigate to client's dashboard
    }
}
