package dev.stekl0.nonokt.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val GridOn: ImageVector
    get() {
        if (_GridOn != null) {
            return _GridOn!!
        }
        _GridOn =
            ImageVector
                .Builder(
                    name = "GridOn",
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
                        horizontalLineToRelative(133f)
                        verticalLineToRelative(-133f)
                        lineTo(200f, 627f)
                        verticalLineToRelative(133f)
                        close()
                        moveTo(413f, 760f)
                        horizontalLineToRelative(134f)
                        verticalLineToRelative(-133f)
                        lineTo(413f, 627f)
                        verticalLineToRelative(133f)
                        close()
                        moveTo(627f, 760f)
                        horizontalLineToRelative(133f)
                        verticalLineToRelative(-133f)
                        lineTo(627f, 627f)
                        verticalLineToRelative(133f)
                        close()
                        moveTo(200f, 547f)
                        horizontalLineToRelative(133f)
                        verticalLineToRelative(-134f)
                        lineTo(200f, 413f)
                        verticalLineToRelative(134f)
                        close()
                        moveTo(413f, 547f)
                        horizontalLineToRelative(134f)
                        verticalLineToRelative(-134f)
                        lineTo(413f, 413f)
                        verticalLineToRelative(134f)
                        close()
                        moveTo(627f, 547f)
                        horizontalLineToRelative(133f)
                        verticalLineToRelative(-134f)
                        lineTo(627f, 413f)
                        verticalLineToRelative(134f)
                        close()
                        moveTo(200f, 333f)
                        horizontalLineToRelative(133f)
                        verticalLineToRelative(-133f)
                        lineTo(200f, 200f)
                        verticalLineToRelative(133f)
                        close()
                        moveTo(413f, 333f)
                        horizontalLineToRelative(134f)
                        verticalLineToRelative(-133f)
                        lineTo(413f, 200f)
                        verticalLineToRelative(133f)
                        close()
                        moveTo(627f, 333f)
                        horizontalLineToRelative(133f)
                        verticalLineToRelative(-133f)
                        lineTo(627f, 200f)
                        verticalLineToRelative(133f)
                        close()
                    }
                }.build()

        return _GridOn!!
    }

@Suppress("ObjectPropertyName")
private var _GridOn: ImageVector? = null
