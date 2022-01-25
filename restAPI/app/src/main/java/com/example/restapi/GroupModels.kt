package com.example.restapi

data class GroupInfo(
    val name: String,
    val lights: ArrayList<String>,
    val sensors: ArrayList<String>,
    val type: String,
    val state: States,
    val recycle: Boolean,
    val classes: String,
    val action: Action
)

data class States(
    val all_on: Boolean,
    val any_on: Boolean
)

data class Action(
    val on: Boolean,
    val bri: Int,
    val alert: String
)