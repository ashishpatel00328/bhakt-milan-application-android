package com.infravo.bhaktmilan.ui.screens.myprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.infravo.bhaktmilan.ui.theme.AppBackground
import com.infravo.bhaktmilan.ui.theme.BhaktMaroon
import com.infravo.bhaktmilan.ui.theme.BhaktMaroonDark
import com.infravo.bhaktmilan.ui.theme.BhaktMaroonLight
import com.infravo.bhaktmilan.ui.theme.DividerColor
import com.infravo.bhaktmilan.ui.theme.PremiumGold
import com.infravo.bhaktmilan.ui.theme.PremiumGoldLight
import com.infravo.bhaktmilan.ui.theme.SurfaceBackground
import com.infravo.bhaktmilan.ui.theme.TextMuted
import com.infravo.bhaktmilan.ui.theme.TextPrimary
import com.infravo.bhaktmilan.ui.theme.TextSecondary

@Composable
fun PrivacySecurityScreen(
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {

        // =========================================================
        // TOP BAR
        // =========================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 10.dp,
                    vertical = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBack,
                modifier = Modifier.size(42.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }

            Column(
                modifier = Modifier.padding(start = 2.dp)
            ) {

                Text(
                    text = "Privacy & Security",
                    color = TextPrimary,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Your privacy matters to us",
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }
        }

        // =========================================================
        // CONTENT
        // =========================================================

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
                .padding(bottom = 30.dp)
        ) {

            // =====================================================
            // TRUST HERO
            // =====================================================

            TrustHeroCard()

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // =====================================================
            // PRIVACY MESSAGE
            // =====================================================

            PrivacyMessageCard()

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // =====================================================
            // OUR COMMITMENT
            // =====================================================

            CommitmentCard()

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            // =====================================================
            // TRUST FOOTER
            // =====================================================

            TrustFooter()
        }
    }
}

// =================================================================
// TRUST HERO CARD
// =================================================================

@Composable
private fun TrustHeroCard() {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 22.dp,
                    vertical = 24.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(
                        PremiumGoldLight
                    )
                    .border(
                        width = 1.dp,
                        color = PremiumGold.copy(
                            alpha = 0.45f
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Outlined.VerifiedUser,
                    contentDescription = null,
                    tint = PremiumGold,
                    modifier = Modifier.size(34.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "Your Privacy Matters",
                color = TextPrimary,
                fontSize = 23.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Trust is the foundation of every meaningful relationship.",
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

// =================================================================
// PRIVACY MESSAGE CARD
// =================================================================

@Composable
private fun PrivacyMessageCard() {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(19.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconContainer(
                    icon = Icons.Outlined.Lock
                )

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Column {

                    Text(
                        text = "Your data is in safe hands.",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = "We respect your privacy.",
                        color = BhaktMaroon,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "At Bhakt Milan, your personal information is handled with care. We take appropriate measures to help keep your information secure and to provide you with a safe and comfortable experience.",
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 21.sp
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Your information is used to provide and improve your Bhakt Milan experience and is not meant to be shared carelessly.",
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 21.sp
            )
        }
    }
}

// =================================================================
// COMMITMENT CARD
// =================================================================

@Composable
private fun CommitmentCard() {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(19.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconContainer(
                    icon = Icons.Outlined.Security,
                    gold = true
                )

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Column {

                    Text(
                        text = "Our Commitment",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "Built around trust and respect",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(17.dp)
            )

            CommitmentRow(
                title = "Privacy comes first",
                description = "We treat your personal information with care."
            )

            CommitmentDivider()

            CommitmentRow(
                title = "Respect for your information",
                description = "Your information is handled thoughtfully."
            )

            CommitmentDivider()

            CommitmentRow(
                title = "A trusted experience",
                description = "We aim to make every connection feel comfortable and meaningful."
            )
        }
    }
}

// =================================================================
// COMMITMENT ROW
// =================================================================

@Composable
private fun CommitmentRow(
    title: String,
    description: String
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {

        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(
                    BhaktMaroonLight
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = null,
                tint = BhaktMaroon,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(
            modifier = Modifier.width(11.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = description,
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 17.sp
            )
        }
    }
}

// =================================================================
// COMMITMENT DIVIDER
// =================================================================

@Composable
private fun CommitmentDivider() {

    Spacer(
        modifier = Modifier.height(13.dp)
    )

    androidx.compose.material3.HorizontalDivider(
        color = DividerColor
    )

    Spacer(
        modifier = Modifier.height(13.dp)
    )
}

// =================================================================
// ICON CONTAINER
// =================================================================

@Composable
private fun IconContainer(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    gold: Boolean = false
) {

    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(
                RoundedCornerShape(12.dp)
            )
            .background(
                if (gold) {
                    PremiumGoldLight
                } else {
                    BhaktMaroonLight
                }
            ),
        contentAlignment = Alignment.Center
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (gold) {
                PremiumGold
            } else {
                BhaktMaroon
            },
            modifier = Modifier.size(21.dp)
        )
    }
}

// =================================================================
// TRUST FOOTER
// =================================================================

@Composable
private fun TrustFooter() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 8.dp,
                vertical = 8.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(
                    BhaktMaroonLight
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                tint = BhaktMaroon,
                modifier = Modifier.size(21.dp)
            )
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = "Your trust is important to us.",
            color = BhaktMaroonDark,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = "Built on Trust • Designed for Meaningful Connections",
            color = TextMuted,
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
    }
}