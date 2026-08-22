package com.meepoffaith.hextrapats.casting.handlers

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.castables.SpecialHandler
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexAngle
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import at.petrak.hexcasting.api.utils.lightPurple
import com.meepoffaith.hextrapats.registry.HextraSpecialHandlers
import com.meepoffaith.hextrapats.util.HextraUtils
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec3

class SpecialHandlerVecSwizzling(val swizzle: Int) : SpecialHandler{
    override fun act(): Action{
        return InnerAction(swizzle)
    }

    override fun getName(): Component{
        return HextraUtils.specialHandlerLang(HextraSpecialHandlers.VEC_SWIZZLE)
            .asTranslatedComponent(
                getChar(swizzle shr 4),
                getChar(swizzle shr 2),
                getChar(swizzle)
            ).lightPurple
    }

    fun getChar(bits: Int): Char = when(bits and 0b11){
        0b00 -> 'X'
        0b01 -> 'Y'
        0b10 -> 'Z'
        else -> throw IllegalStateException()
    }

    class InnerAction(val swizzle: Int): ConstMediaAction{
        override val argc = 1
        override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
            val vec = args.getVec3(0)
            val x = getComponent(vec, swizzle shr 4)
            val y = getComponent(vec, swizzle shr 2)
            val z = getComponent(vec, swizzle)
            return Vec3(x, y, z).asActionResult
        }

        fun getComponent(vec: Vec3, bits: Int): Double = when(bits and 0b11){
            0b00 -> vec.x
            0b01 -> vec.y
            0b10 -> vec.z
            else -> throw IllegalStateException()
        }
    }

    class Factory : SpecialHandler.Factory<SpecialHandlerVecSwizzling>{
        override fun tryMatch(pattern: HexPattern, env: CastingEnvironment): SpecialHandlerVecSwizzling? {
            val sig = pattern.anglesSignature()
            if(sig.startsWith("eeeeqaawddea")){
                val directions = pattern.directions().drop(13)
                val flatDir = pattern.startDir.rotatedBy(HexAngle.BACK) // Start is to the left. Flip over to the right.

                var vals = 0
                var swizzle = 0
                var i = 0;
                while(i < directions.size){
                    // Pattern is too long! Already have the 3 vals.
                    if(vals == 3) return null

                    // Angle with respect to the *start direction*
                    val angle = directions[i].angleFrom(flatDir)
                    if(angle == HexAngle.FORWARD){
                        swizzle = (swizzle shl 2) or 0b01 // 01 -> y
                        vals++
                        i++
                        continue
                    }

                    // Out of angles to process!
                    if (i >= directions.size - 1) return null

                    val angle2 = directions[i + 1].angleFrom(flatDir)
                    if(angle == HexAngle.LEFT && angle2 == HexAngle.RIGHT){
                        swizzle = swizzle shl 2 // 00 -> x
                        vals++
                        i += 2
                        continue
                    }
                    if(angle == HexAngle.RIGHT && angle2 == HexAngle.LEFT){
                        swizzle = (swizzle shl 2) or 0b10 // 10 -> z
                        vals++
                        i += 2
                        continue
                    }
                    return null
                }
                return if(vals == 3) SpecialHandlerVecSwizzling(swizzle) else null
            }
            return null
        }
    }
}
