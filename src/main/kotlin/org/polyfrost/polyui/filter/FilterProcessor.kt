package org.polyfrost.polyui.filter

import org.polyfrost.polyui.*
import org.polyfrost.polyui.data.*
import org.polyfrost.polyui.renderer.*

class FilterProcessor private constructor(private val _image: PolyImage, private vararg val filters: Filter) {
	private val isGr = _image is GrImage
	private lateinit var result: PolyImage
	private var framebuffer: Framebuffer? = null
	private var needsApply = true
	private lateinit var polyUI: PolyUI

	companion object {
		// Queue here...

		fun PolyImage.filter(vararg filters: Filter): FilterProcessor {
			return FilterProcessor(this, *filters)
		}
	}

	fun udpate() {
		val fc = polyUI.renderer as FramebufferController

		if (framebuffer == null) {
			framebuffer = fc.createFramebuffer(_image.size.x, _image.size.y)
		}

		framebuffer?.let {
			if (it.width != _image.size.x || it.height != _image.size.y) {
				framebuffer = null
				needsApply = true
				return
			}
		}

	}

	/**
	 * have effect queue which |will run during render. Instead of returning raw [PolyImage], return FilteredImage which will be similar to [GrImage] in sense that it will return a native image but after processing, which happens in the queue
	 */

	fun apply() {
		if (framebuffer == null) return

		val fc = polyUI.renderer as FramebufferController

	}

	fun get(polyUI: PolyUI): PolyImage {
		this.polyUI = polyUI

		require(polyUI.settings.framebuffersEnabled && polyUI.renderer is FramebufferController)

		udpate()
		if (needsApply) {
			apply()
			if (!isGr) needsApply = false
		}
		return result
	}
}
