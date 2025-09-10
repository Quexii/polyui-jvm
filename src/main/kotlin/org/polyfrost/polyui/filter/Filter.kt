package org.polyfrost.polyui.filter

import org.polyfrost.polyui.data.Framebuffer

interface Filter {
	fun apply(input: Framebuffer): Framebuffer
}
