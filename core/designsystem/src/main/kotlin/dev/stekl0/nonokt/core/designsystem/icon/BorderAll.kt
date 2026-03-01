package dev.stekl0.nonokt.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val BorderAll: ImageVector
    get() {
        if (_BorderAll != null) {
            return _BorderAll!!
        }
        _BorderAll =
            ImageVector
                .Builder(
                    name = "BorderAll",
                    defaultWidth = 24.dp,
                    defaultHeight = 24.dp,
                    viewportWidth = 960f,
                    viewportHeight = 960f,
                ).apply {
                    path(fill = SolidColor(Color.Black)) {
                        moveTo(120f, 760f)
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
                        quadToRelative(-33f, 0f, -56.5f, -23.5f)
                        reflectiveQuadTo(120f, 760f)
                        close()
                        moveTo(520f, 520f)
                        verticalLineToRelative(240f)
                        horizontalLineToRelative(240f)
                        verticalLineToRelative(-240f)
                        lineTo(520f, 520f)
                        close()
                        moveTo(520f, 440f)
                        horizontalLineToRelative(240f)
                        verticalLineToRelative(-240f)
                        lineTo(520f, 200f)
                        verticalLineToRelative(240f)
                        close()
                        moveTo(440f, 440f)
                        verticalLineToRelative(-240f)
                        lineTo(200f, 200f)
                        verticalLineToRelative(240f)
                        horizontalLineToRelative(240f)
                        close()
                        moveTo(440f, 520f)
                        lineTo(200f, 520f)
                        verticalLineToRelative(240f)
                        horizontalLineToRelative(240f)
                        verticalLineToRelative(-240f)
                        close()
                    }
                }.build()

        return _BorderAll!!
    }

@Suppress("ObjectPropertyName")
private var _BorderAll: ImageVector? = null
