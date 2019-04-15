package com.example.harit.marketapp.ui.model

data class Chat(
        var create : Long? = null,
        var chatId : String? = null,
        var messageId : String? = null,
        var message : String? = null,
        var messageType : String? = null,
        var senderId : String? = null,
        var mediaUrl : String? = null
)