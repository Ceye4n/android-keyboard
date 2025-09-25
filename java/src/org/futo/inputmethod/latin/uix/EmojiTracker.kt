package org.futo.inputmethod.latin.uix

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey


val lastUsedEmoji = stringPreferencesKey("last_used_emoji")
val lastUsedColor = stringPreferencesKey("last_used_color")

object EmojiTracker {
    suspend fun Context.setLastUsedColor(color: String) {
        if(isDeviceLocked) return
        
        dataStore.edit {
            it[lastUsedColor] = color
        }
    }

    suspend fun Context.useEmoji(emoji: String) {
        if(isDeviceLocked) return

        dataStore.edit {
            val combined = emoji + "<|>" + (it[lastUsedEmoji] ?: "")
            it[lastUsedEmoji] = combined.split("<|>").take(128).joinToString("<|>")
        }
    }

    suspend fun Context.getRecentEmojis(): List<String> {
        if(isDeviceLocked) return listOf()

        return getSetting(lastUsedEmoji, "")
            .split("<|>")
            .filter { it.isNotBlank() }
            .distinct()
    }

    suspend fun Context.resetRecentEmojis() {
        if(isDeviceLocked) return

        setSetting(lastUsedEmoji, "")
    }
}