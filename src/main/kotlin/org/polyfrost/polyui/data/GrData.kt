package org.polyfrost.polyui.data

class GrData(vararg val data: Any) {
	override fun toString(): String {
		return "GrData(${data.joinToString()})"
	}
}
