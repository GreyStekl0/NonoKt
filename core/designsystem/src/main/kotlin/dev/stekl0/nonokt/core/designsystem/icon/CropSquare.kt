package dev.stekl0.nonokt.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val CropSquare: ImageVector
    get() {
        if (_CropSquare != null) {
            return _CropSquare!!
        }
        _CropSquare =
            ImageVector
                .Builder(
                    name = "CropSquare",
                    defaultWidth = 24.dp,
                    defaultHeight = 24.dp,
                    viewportWidth = 960f,
                    viewportHeight = 960f,
                ).apply {
                    path(fill = SolidColor(Color.Black)) {
                        moveTo(200f, 840f)
                        quadToRelative(-33f, 0f, -56.5f, -23.5f)
                        reflectiveQuadTo(120f, 760f)
                        verticalLineToRelative(-560f)
                        quadToRelative(0f, -33f, 23.5f, -56.5f)
                        reflectiveQuadTo(200f, 120f)
                        horizontalLineToRelative(560f)
                        quadToRelative(33f, 0f, 56.5f, 23.5f)
                        reflectiveQuadTo(840f, 200f)
                        verticalLineToRelative(560f)
                        quadToRelative(0f, 33f, -23.5f, 56.5f)
                        reflectiveQuadTo(760f, 840f)
                        lineTo(200f, 840f)
                        close()
                        moveTo(200f, 760f)
                        horizontalLineToRelative(560f)
                        verticalLineToRelative(-560f)
                        lineTo(200f, 200f)
                        verticalLineToRelative(560f)
                        close()
                        moveTo(200f, 760f)
                        verticalLineToRelative(-560f)
                        verticalLineToRelative(560f)
                        close()
                    }
                }.build()

        return _CropSquare!!
    }

@Suppress("ObjectPropertyName")
private var _CropSquare: ImageVector? = null
