package com.meepoffaith.hextrapats.casting.handlers

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.castables.SpecialHandler
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getNumOrVec
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import at.petrak.hexcasting.api.utils.lightPurple
import com.meepoffaith.hextrapats.registry.HextraSpecialHandlers
import com.meepoffaith.hextrapats.util.HextraUtils
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec3
import kotlin.math.pow

class SpecialHandlerScientificExponent(val exp: Int) : SpecialHandler{
    override fun act(): Action{
        return InnerAction(exp)
    }

    override fun getName(): Component{
        return HextraUtils.specialHandlerLang(HextraSpecialHandlers.SCI_EXP)
            .asTranslatedComponent(Action.DOUBLE_FORMATTER.format(exp)).lightPurple
    }

    class InnerAction(val exp: Int) : ConstMediaAction{
        override val argc = 1
        override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
            val arg = args.getNumOrVec(0, argc)
            val pow = 10.0.pow(exp)
            return arg.map(
                { num -> (num * pow).asActionResult },
                { vec -> Vec3(vec.x * pow, vec.y * pow, vec.z * pow).asActionResult }
            )
        }
    }

    class Factory : SpecialHandler.Factory<SpecialHandlerScientificExponent> {
        override fun tryMatch(pattern: HexPattern, env: CastingEnvironment): SpecialHandlerScientificExponent? {
            val sig = pattern.getSignature().toAnglesString()
            if(sig.startsWith("waqe") || sig.startsWith("wdeq")){
                val divide = sig.startsWith("wdeq")
                val chars = sig.substring(4).toCharArray()
                var exponent = 1
                for(i in chars.indices){ //Code based on Sekhmet from Overevaluate
                    if(chars[i] != "qe"[(i + (if (divide) 1 else 0)) % 2]){
                        return null
                    }
                    exponent++
                }
                return SpecialHandlerScientificExponent(exponent * (if (divide) -1 else 1))
            }
            return null
        }
    }
}
