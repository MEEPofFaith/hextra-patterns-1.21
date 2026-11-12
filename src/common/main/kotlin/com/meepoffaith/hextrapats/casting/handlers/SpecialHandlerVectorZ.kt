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

class SpecialHandlerVectorZ(val z: Double) : SpecialHandler{
    override fun act(): Action{
        return InnerAction(z)
    }

    override fun getName(): Component{
        return HextraUtils.specialHandlerLang(HextraSpecialHandlers.VEC_Z)
            .asTranslatedComponent(Action.DOUBLE_FORMATTER.format(z)).lightPurple
    }

    class InnerAction(val z: Double) : ConstMediaAction{
        override val argc = 0
        override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
            return Vec3(0.0, 0.0, z).asActionResult
        }
    }

    class Factory : SpecialHandler.Factory<SpecialHandlerVectorZ>{
        override fun tryMatch(pattern: HexPattern, env: CastingEnvironment): SpecialHandlerVectorZ? {
            val sig = pattern.getSignature().toAnglesString()
            if(sig.startsWith("deaqaa") || sig.startsWith("dqdedd")){
                val num = numericalReflection(sig.substring(6)) *
                        (if (sig.startsWith("dqdedd")) -1.0 else 1.0)
                return SpecialHandlerVectorZ(num)
            } else {
                return null
            }
        }
    }
}
