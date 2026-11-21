package com.meepoffaith.hextrapats.casting.handlers

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.castables.SpecialHandler
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import at.petrak.hexcasting.api.utils.lightPurple
import com.meepoffaith.hextrapats.registry.HextraSpecialHandlers
import com.meepoffaith.hextrapats.util.HextraUtils
import com.meepoffaith.hextrapats.util.HextraUtils.numericalReflection
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec3

class SpecialHandlerVectorY(val y: Double) : SpecialHandler{
    override fun act(): Action{
        return InnerAction(y)
    }

    override fun getName(): Component{
        return HextraUtils.specialHandlerLang(HextraSpecialHandlers.VEC_Y)
            .asTranslatedComponent(Action.DOUBLE_FORMATTER.format(y)).lightPurple
    }

    class InnerAction(val y: Double) : ConstMediaAction{
        override val argc = 0
        override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
            return Vec3(0.0, y, 0.0).asActionResult
        }
    }

    class Factory : SpecialHandler.Factory<SpecialHandlerVectorY>{
        override fun tryMatch(pattern: HexPattern, env: CastingEnvironment): SpecialHandlerVectorY? {
            val sig = pattern.getSignature().toAnglesString()
            if(sig.startsWith("weaqaa") || sig.startsWith("wqdedd")){
                val num = numericalReflection(sig.substring(6)) *
                        (if (sig.startsWith("wqdedd")) -1.0 else 1.0)
                return SpecialHandlerVectorY(num)
            } else {
                return null
            }
        }
    }
}
