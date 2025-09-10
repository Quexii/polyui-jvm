package org.polyfrost.polyui.data

/**
 * A [PolyImage] for native images created directly by the rendering backend.
 *
 * @param nativeData The native data associated with the image. (Texture ID in OpenGL, ...).
 */
class GrImage(val nativeData: GrData) : PolyImage(nativeData.hashCode().toString(), Type.Unknown) {
	companion object Cache {
		private val fboImages = mutableMapOf<Int, GrImage>()

		fun put(fbo: Framebuffer, image: GrImage): GrImage {
			fboImages[fbo.instanceId] = image
			return image
		}

		fun get(fbo: Framebuffer) = fboImages[fbo.instanceId]

		fun remove(fbo: Framebuffer) {
			fboImages.remove(fbo.instanceId)
		}
	}
}
