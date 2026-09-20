package com.infravo.bhaktmilan.ui.screens.premium

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.infravo.bhaktmilan.data.remote.response.MySubscription

private const val TAG = "PremiumSubscriptionCard"

@Composable
fun SubscriptionStatusCard(
    subscription: MySubscription
) {

    Log.d(
        TAG,
        """
        Rendering subscription status
        -----------------------------
        isPremium = ${subscription.is_premium}
        status    = ${subscription.status}
        plan      = ${subscription.plan}
        daysLeft  = ${subscription.days_left}
        startsAt  = ${subscription.starts_at}
        expiresAt = ${subscription.expires_at}
        -----------------------------
        """.trimIndent()
    )

    val status = subscription.status
        ?.trim()
        ?.uppercase()
        ?: "UNKNOWN"

    val title = when {

        subscription.is_premium ->
            "Premium Active"

        status == "PENDING" ->
            "Request Pending"

        status == "REJECTED" ->
            "Request Rejected"

        status == "EXPIRED" ->
            "Premium Expired"

        else ->
            "Premium Status"
    }

    val message = when {

        subscription.is_premium ->
            "Your premium membership is currently active."

        status == "PENDING" ->
            "Your premium request has been sent to the admin and is waiting for approval."

        status == "REJECTED" ->
            "Your previous premium request was rejected."

        status == "EXPIRED" ->
            "Your premium membership has expired."

        else ->
            "You do not currently have an active premium membership."
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            verticalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            // ==========================================
            // TITLE
            // ==========================================

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            // ==========================================
            // MESSAGE
            // ==========================================

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            // ==========================================
            // STATUS
            // ==========================================

            Row(
                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween,

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text = "Status",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = status,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            // ==========================================
            // PLAN
            // ==========================================

            subscription.plan
                ?.takeIf {
                    it.isNotBlank()
                }
                ?.let { plan ->

                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = "Plan",
                            style =
                                MaterialTheme.typography.bodySmall
                        )

                        Text(
                            text = plan,
                            style =
                                MaterialTheme.typography.bodySmall
                        )
                    }
                }

            // ==========================================
            // PREMIUM INFORMATION
            // ==========================================

            if (subscription.is_premium) {

                Log.d(
                    TAG,
                    "User has active premium subscription"
                )

                // ======================================
                // START DATE
                // ======================================

                subscription.starts_at
                    ?.takeIf {
                        it.isNotBlank()
                    }
                    ?.let { startDate ->

                        Row(
                            modifier =
                                Modifier.fillMaxWidth(),

                            horizontalArrangement =
                                Arrangement.SpaceBetween
                        ) {

                            Text(
                                text = "Started",
                                style =
                                    MaterialTheme.typography.bodySmall
                            )

                            Text(
                                text = startDate,
                                style =
                                    MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                // ======================================
                // EXPIRY DATE
                // ======================================

                subscription.expires_at
                    ?.takeIf {
                        it.isNotBlank()
                    }
                    ?.let { expiryDate ->

                        Row(
                            modifier =
                                Modifier.fillMaxWidth(),

                            horizontalArrangement =
                                Arrangement.SpaceBetween
                        ) {

                            Text(
                                text = "Expires",
                                style =
                                    MaterialTheme.typography.bodySmall
                            )

                            Text(
                                text = expiryDate,
                                style =
                                    MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                // ======================================
                // DAYS LEFT
                // ======================================

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {

                    Text(
                        text = "Days Left",
                        style =
                            MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = subscription.days_left.toString(),
                        style =
                            MaterialTheme.typography.bodySmall
                    )
                }
            }

            // ==========================================
            // PENDING DEBUG
            // ==========================================

            if (status == "PENDING") {

                Log.d(
                    TAG,
                    "Subscription status is PENDING"
                )
            }
        }
    }
}