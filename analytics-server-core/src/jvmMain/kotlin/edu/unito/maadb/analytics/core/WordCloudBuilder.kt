package edu.unito.maadb.analytics.core

import com.kennycason.kumo.CollisionMode
import com.kennycason.kumo.WordCloud
import com.kennycason.kumo.WordFrequency
import com.kennycason.kumo.bg.Background
import com.kennycason.kumo.bg.RectangleBackground
import com.kennycason.kumo.font.scale.FontScalar
import com.kennycason.kumo.font.scale.SqrtFontScalar
import com.kennycason.kumo.palette.ColorPalette
import java.awt.Color
import java.awt.Dimension

@WordCloudDSL
class WordCloudBuilder(
    var width: Int = 500,
    var height: Int = 500,
    var padding: Int = 0,
    var background: Background = RectangleBackground(Dimension(width, height)),
    var fontScalar: FontScalar = SqrtFontScalar(10, 40),
    var words: Map<String, Int> = emptyMap(),
    var collisionMode: CollisionMode = CollisionMode.RECTANGLE
) {

    @WordCloudDSL
    class ColorPaletteBuilder(
        private val palette: MutableList<Color> = mutableListOf()
    ) {
        fun color(rgb: Int) =
            palette.add(Color(rgb))

        fun build() = ColorPalette(palette)
    }

    private var colorPalette: ColorPalette? = null

    fun colorPalette(action: ColorPaletteBuilder.() -> Unit) {
        colorPalette = ColorPaletteBuilder().apply(action).build()
    }

    fun build() = WordCloud(Dimension(width, height), collisionMode).apply {
        setPadding(padding)
        setBackground(background)
        setFontScalar(fontScalar)
        colorPalette?.let { setColorPalette(it) }
        build(words.entries.map { WordFrequency(it.key, it.value) })
    }
}
