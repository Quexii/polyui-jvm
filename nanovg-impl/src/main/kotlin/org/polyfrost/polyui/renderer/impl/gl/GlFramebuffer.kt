package org.polyfrost.polyui.renderer.impl.gl

import org.lwjgl.opengl.*
import org.lwjgl.system.MemoryUtil.*
import org.polyfrost.polyui.PolyUI

class GlFramebuffer {
	var id: Int = 0
		private set
	var width: Int = 0
		private set
	var height: Int = 0
		private set

	private var colorTex: Int = 0

	private val prevViewport = IntArray(4)

	companion object {
		fun unbind0() {
			GL30C.glBindFramebuffer(GL30C.GL_FRAMEBUFFER, 0)
		}
	}

	fun create(width: Int, height: Int) {
		if (id != 0) delete()
		this.width = width
		this.height = height

		id = GL30C.glGenFramebuffers()
		GL30C.glBindFramebuffer(GL30C.GL_FRAMEBUFFER, id)

		colorTex = GL11C.glGenTextures()
		GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, colorTex)
		GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_MIN_FILTER, GL11C.GL_LINEAR)
		GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_MAG_FILTER, GL11C.GL_LINEAR)
		GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_WRAP_S, GL12C.GL_CLAMP_TO_EDGE)
		GL11C.glTexParameteri(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_WRAP_T, GL12C.GL_CLAMP_TO_EDGE)
		GL11C.glTexImage2D(
			GL11C.GL_TEXTURE_2D, 0, GL11C.GL_RGBA8, width, height, 0, GL11C.GL_RGBA, GL11C.GL_UNSIGNED_BYTE, NULL
		)
		GL30C.glFramebufferTexture2D(
			GL30C.GL_FRAMEBUFFER, GL30C.GL_COLOR_ATTACHMENT0, GL11C.GL_TEXTURE_2D, colorTex, 0
		)

		GL20C.glDrawBuffers(GL30C.GL_COLOR_ATTACHMENT0)

		val status = GL30C.glCheckFramebufferStatus(GL30C.GL_FRAMEBUFFER)
		if (status != GL30C.GL_FRAMEBUFFER_COMPLETE) {
			GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, 0)
			GL30C.glBindRenderbuffer(GL30C.GL_RENDERBUFFER, 0)
			GL30C.glBindFramebuffer(GL30C.GL_FRAMEBUFFER, 0)
			delete()
			throw IllegalStateException("Framebuffer incomplete: 0x${Integer.toHexString(status)}")
		}

		GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, 0)
		GL30C.glBindRenderbuffer(GL30C.GL_RENDERBUFFER, 0)
		GL30C.glBindFramebuffer(GL30C.GL_FRAMEBUFFER, 0)
	}

	fun bind() {
		if (id == 0) throw IllegalStateException("FBO not created")

		if (prevViewport[2] == 0 && prevViewport[3] == 0)
			GL11C.glGetIntegerv(GL11C.GL_VIEWPORT, prevViewport)

		GL30C.glBindFramebuffer(GL30C.GL_FRAMEBUFFER, id)
		GL11C.glViewport(0, 0, width, height)
	}

	fun unbind() {
		GL30C.glBindFramebuffer(GL30C.GL_FRAMEBUFFER, 0)
//		GL11C.glViewport(0, 0, prevViewport[2], prevViewport[3])
	}

	fun clear() {
		if (id == 0) throw IllegalStateException("FBO not created")
		GL11C.glClearColor(0f, 0f, 0f, 0f)
		GL11C.glClear(GL11C.GL_COLOR_BUFFER_BIT)
	}

	fun delete() {
		if (colorTex != 0) {
			GL11C.glDeleteTextures(colorTex)
			colorTex = 0
		}
		if (id != 0) {
			GL30C.glDeleteFramebuffers(id)
			id = 0
		}
		width = 0
		height = 0
	}

	val framebufferId: Int get() = id
	val colorTextureId: Int get() = colorTex

	override fun toString(): String {
		return "GlFramebuffer(id=$id, width=$width, height=$height, colorTex=$colorTex)"
	}
}

fun GlFramebuffer.draw(x: Float, y: Float, w: Float, h: Float) {
	if (framebufferId == 0) throw IllegalStateException("FBO not created")

//	val prevReadFbo = GL11C.glGetInteger(GL30C.GL_READ_FRAMEBUFFER_BINDING)
//	val prevDrawFbo = GL11C.glGetInteger(GL30C.GL_DRAW_FRAMEBUFFER_BINDING)
//	val prevReadBuf = GL11C.glGetInteger(GL11C.GL_READ_BUFFER)

//	GL30C.glBindFramebuffer(GL30C.GL_READ_FRAMEBUFFER, framebufferId)
//	GL11C.glReadBuffer(GL30C.GL_COLOR_ATTACHMENT0)
//	GL30C.glBindFramebuffer(GL30C.GL_DRAW_FRAMEBUFFER, 0)

	bind()
	GL30C.glBlitFramebuffer(
		0, 0, width, height, x.toInt(), y.toInt(), (x + w).toInt(), (y + h).toInt(), GL11C.GL_COLOR_BUFFER_BIT, GL11C.GL_LINEAR
	)
	unbind()

//	GL30C.glBindFramebuffer(GL30C.GL_READ_FRAMEBUFFER, prevReadFbo)
//	GL30C.glBindFramebuffer(GL30C.GL_DRAW_FRAMEBUFFER, prevDrawFbo)
//	GL11C.glReadBuffer(prevReadBuf)
}

