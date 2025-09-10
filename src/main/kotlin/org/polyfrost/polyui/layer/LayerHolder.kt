package org.polyfrost.polyui.layer

import org.polyfrost.polyui.component.*
import org.polyfrost.polyui.unit.*

class LayerHolder(val content: Component, val isMaster: Boolean = false, val order: Int = 0) : Drawable(alignment = Align(
	padEdges = Vec2.ZERO,
	padBetween = Vec2.ZERO,
)) {
	init {
		useFramebuffer = true
		drawResult = false
		needsRedraw = true
		addChild(content)
	}
	override fun render() {}

//	override fun draw() {
//
//	}
}
