package com.prplmnstr.mynotesapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.prplmnstr.mynotesapp.utils.Constants


@Entity(tableName = Constants.NOTES_TABLE)
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var user: String,
    var title: String,
    var creationDate: String,
    var description: String,
)
