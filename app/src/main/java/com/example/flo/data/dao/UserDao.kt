package com.example.flo.data.dao

import androidx.room.*
import com.example.flo.data.vo.User

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Query("SELECT * FROM UserTable")
    fun getUsers(): List<User>

    @Query("SELECT * FROM UserTable WHERE email = :email AND password = :password")
    fun getUser(email: String, password: String): User?

    @Query("SELECT * FROM UserTable WHERE id = :id")
    fun getUserById(id: Int): User?
}