package com.meepoffaith.hextrapats.casting.handlers

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
import net.minecraft.network.chat.Component

class SpecialHandlerDuplicateAt(val depth: Int) : SpecialHandler {
    override fun act(): Action{
        return InnerAction(depth)
    }

    override fun getName(): Component{
        return HextraUtils.specialHandlerLang(HextraSpecialHandlers.DUPLICATE_AT)
            .asTranslatedComponent(depth).lightPurple
    }

    class InnerAction(depth: Int) : ConstMediaAction {
        override val argc = depth + 1
        override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
            val out = mutableListOf(args[0])
            out.addAll(args)
            return out
        }
    }

    class Factory : SpecialHandler.Factory<SpecialHandlerDuplicateAt>{
        override fun tryMatch(pattern: HexPattern, env: CastingEnvironment): SpecialHandlerDuplicateAt? {
            val sig = pattern.anglesSignature()
            if(sig.endsWith("wddad")){
                val chars = sig.substring(0, sig.length - 5).toCharArray()
                for(c in chars){
                    if(c != 'w') return null
                }
                return SpecialHandlerDuplicateAt(chars.size)
            }
            return null
        }
    }
}
