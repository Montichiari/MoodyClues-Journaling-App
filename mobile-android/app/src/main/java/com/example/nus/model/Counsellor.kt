package com.example.nus.model

class Counsellor(
    id: String,
    email: String,
    password: String,
    firstName: String,
    lastName: String
) : User(id, email, password, firstName, lastName)
