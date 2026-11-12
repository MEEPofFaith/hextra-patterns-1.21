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


class SpecialHandlerVector1(val x: Double) : SpecialHandler{
    override fun act(): Action{
        return InnerAction(x)
    }

    override fun getName(): Component{
        val num = Action.DOUBLE_FORMATTER.format(x)
        return HextraUtils.specialHandlerLang(HextraSpecialHandlers.VEC_1)
            .asTranslatedComponent(num, num, num).lightPurple
    }

    class InnerAction(val x: Double) : ConstMediaAction{
        override val argc = 0
        override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
            return Vec3(x, x, x).asActionResult
        }
    }

    class Factory : SpecialHandler.Factory<SpecialHandlerVector1>{
        override fun tryMatch(pattern: HexPattern, env: CastingEnvironment): SpecialHandlerVector1? {
            val sig = pattern.getSignature().toAnglesString()
            if (sig.startsWith("qeaqaa") || sig.startsWith("qqdedd")){
                val num = numericalReflection(sig.substring(6)) *
                        (if (sig.startsWith("qqdedd")) -1.0 else 1.0)
                return SpecialHandlerVector1(num)
            } else {
                return null
            }
        }
    }
}
