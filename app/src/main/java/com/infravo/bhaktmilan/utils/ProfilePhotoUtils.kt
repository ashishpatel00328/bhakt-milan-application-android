package com.infravo.bhaktmilan.utils

import com.infravo.bhaktmilan.BuildConfig

object ProfilePhotoUtils {

    fun resolveUrl(photoPath: String?): String? {
        val path = photoPath
            ?.trim()
            ?.takeIf { it.isNotEmpty() }
            ?: return null

        // Already an absolute URL
        if (
            path.startsWith("https://", ignoreCase = true) ||
            path.startsWith("http://", ignoreCase = true)
        ) {
            return path
        }

        // Remove /api/v1/ from the configured API base URL
        val serverOrigin = BuildConfig.BASE_URL
            .trim()
            .trimEnd('/')
            .substringBefore("/api/")

        // Support both "media/profiles/..." and "/media/profiles/..."
        val normalizedPath = path.trimStart('/')

        return "$serverOrigin/$normalizedPath"
    }
}