package com.meepoffaith.hextrapats.util

import at.petrak.hexcasting.api.casting.iota.DoubleIota
import net.minecraft.world.phys.Vec3
import kotlin.math.acos
import kotlin.math.min
import kotlin.math.roundToLong


object MathUtils {
    const val TAU = Math.PI * 2.0

    /** Modulo that works properly for negative numbers. Taken from Anuken/Arc.  */
    fun mod(a: Double, b: Double): Double{
        return ((a % b) + b) % b
    }

    fun angleDist(a: Double, b: Double): Double{
        var a = a
        var b = b
        a = mod(a, TAU)
        b = mod(b, TAU)

        val distBack = if ((a - b) < 0) a - b + TAU else a - b
        val distFwd = if ((b - a) < 0) b - a + TAU else b - a

        return min(distBack, distFwd)
    }

    fun vecAngleDist(a: Vec3, b: Vec3): Double{
        val dot = a.dot(b)
        val len2 = a.length() * b.length()

        return acos(dot / len2)
    }

    fun Double.roundToInterval(interval: Double): Double{
        return (this / interval).roundToLong() * interval
    }

    fun Double.roundToTolerance(): Double{
        return this.roundToInterval(DoubleIota.TOLERANCE)
    }

    fun Vec3.round(): Vec3{
        return Vec3(
            this.x.roundToLong().toDouble(),
            this.y.roundToLong().toDouble(),
            this.z.roundToLong().toDouble()
        )
    }

    fun Vec3.roundToInterval(interval: Double): Vec3{
        return Vec3(
            this.x.roundToInterval(interval),
            this.y.roundToInterval(interval),
            this.z.roundToInterval(interval)
        )
    }

    fun Vec3.roundToTolerance(): Vec3{
        return Vec3(
            this.x.roundToTolerance(),
            this.y.roundToTolerance(),
            this.z.roundToTolerance()
        )
    }
}
