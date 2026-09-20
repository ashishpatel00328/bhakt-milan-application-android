package com.infravo.bhaktmilan.ui.screens.premium

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.infravo.bhaktmilan.data.remote.response.PremiumPlan

private const val TAG = "PremiumPlanCard"

@Composable
fun PremiumPlanCard(
    plan: PremiumPlan,
    isSelected: Boolean,
    isPremium: Boolean,
    isSubmitting: Boolean,
    onClick: () -> Unit
) {
    val isPending =
        plan.id > 0 && false
    // Pending state is controlled by subscription state in
    // PremiumScreen/ViewModel, not by the plan itself.

    val clickable =
        !isPremium && !isSubmitting

    Log.d(
        TAG,
        """
        Rendering PremiumPlanCard
        -----------------------------
        planId       = ${plan.id}
        isSelected   = $isSelected
        isPremium    = $isPremium
        isSubmitting = $isSubmitting
        clickable    = $clickable
        -----------------------------
        """.trimIndent()
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 6.dp
            )
            .clickable(
                enabled = clickable,
                onClick = {

                    Log.d(
                        TAG,
                        """
                        PLAN CARD CLICKED
                        -------------------------
                        planId=${
                            plan.id
                        }
                        isSelected=$isSelected
                        isPremium=$isPremium
                        isSubmitting=$isSubmitting
                        -------------------------
                        """.trimIndent()
                    )

                    onClick()
                }
            ),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = MaterialTheme.colorScheme.outline
        ),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = plan.name,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = "Premium Membership",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Text(
                    text = "₹${plan.price}",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            /*
             * PLAN DESCRIPTION
             */
//            plan.description
//                ?.takeIf { it.isNotBlank() }
//                ?.let { description ->
//
//                    Text(
//                        text = description,
//                        style = MaterialTheme.typography.bodyMedium
//                    )
//
//                    Spacer(
//                        modifier = Modifier.height(8.dp)
//                    )
//                }

            /*
             * DURATION
             */
            plan.duration_days?.let { duration ->

                Text(
                    text = "$duration days validity",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            /*
             * SELECTED STATE
             */
            if (isSelected) {

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Selected",
                    style = MaterialTheme.typography.labelLarge
                )

                Log.d(
                    TAG,
                    "Plan ${plan.id} is currently SELECTED"
                )
            }

            /*
             * SUBMITTING STATE
             */
            if (isSubmitting) {

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Processing request...",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            /*
             * ALREADY PREMIUM
             */
            if (isPremium) {

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Premium Active",
                    style = MaterialTheme.typography.labelLarge
                )

                Log.d(
                    TAG,
                    "Plan ${plan.id} disabled -> user already premium"
                )
            }
        }
    }
}