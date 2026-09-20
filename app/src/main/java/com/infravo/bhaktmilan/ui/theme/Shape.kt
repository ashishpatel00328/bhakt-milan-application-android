package com.infravo.bhaktmilan.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// ==========================================
// BhaktMilan Shape System
// ==========================================
//
// Small   -> 12dp
// Medium  -> 16dp
// Large   -> 20dp
//
// Custom components can use the reusable
// shape values below.
// ==========================================

// ------------------------------------------
// Reusable Shapes
// ------------------------------------------

val BhaktShapeSmall = RoundedCornerShape(12.dp)

val BhaktShapeMedium = RoundedCornerShape(16.dp)

val BhaktShapeLarge = RoundedCornerShape(20.dp)

val BhaktShapeExtraLarge = RoundedCornerShape(24.dp)

val BhaktShapePill = RoundedCornerShape(50)

// ------------------------------------------
// Material 3 Shapes
// ------------------------------------------

val AppShapes = Shapes(

    small = BhaktShapeSmall,

    medium = BhaktShapeMedium,

    large = BhaktShapeLarge
)