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

class SpecialHandlerVectorX(val x: Double) : SpecialHandler{
    override fun act(): Action{
        return InnerAction(x)
    }

    override fun getName(): Component {
        return HextraUtils.specialHandlerLang(HextraSpecialHandlers.VEC_X)
            .asTranslatedComponent(Action.DOUBLE_FORMATTER.format(x)).lightPurple
    }

    class InnerAction(val x: Double) : ConstMediaAction{
        override val argc = 0
        override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
            return Vec3(x, 0.0, 0.0).asActionResult
        }
    }

    class Factory : SpecialHandler.Factory<SpecialHandlerVectorX>{
        override fun tryMatch(pattern: HexPattern, env: CastingEnvironment): SpecialHandlerVectorX? {
            val sig = pattern.getSignature().toAnglesString()
            if(sig.startsWith("aeaqaa") || sig.startsWith("aqdedd")){
                val num = numericalReflection(sig.substring(6)) *
                        (if (sig.startsWith("aqdedd")) -1.0 else 1.0)
                return SpecialHandlerVectorX(num)
            } else {
                return null
            }
        }
    }
}
