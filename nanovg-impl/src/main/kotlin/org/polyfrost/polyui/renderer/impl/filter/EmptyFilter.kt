package org.polyfrost.polyui.renderer.impl.filter

import org.polyfrost.polyui.data.Framebuffer
import org.polyfrost.polyui.filter.Filter

class EmptyFilter : Filter {
	override fun apply(input: Framebuffer): Framebuffer {
		return input
	}
}
