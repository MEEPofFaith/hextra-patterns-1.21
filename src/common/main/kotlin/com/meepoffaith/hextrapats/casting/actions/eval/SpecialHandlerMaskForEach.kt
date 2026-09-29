package com.meepoffaith.hextrapats.casting.actions.eval

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.castables.SpecialHandler
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.FrameForEach
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.getEvaluatable
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.api.utils.TreeList
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import at.petrak.hexcasting.api.utils.lightPurple
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import com.meepoffaith.hextrapats.registry.HextraSpecialHandlers
import com.meepoffaith.hextrapats.util.HextraUtils
import it.unimi.dsi.fastutil.booleans.BooleanArrayList
import it.unimi.dsi.fastutil.booleans.BooleanList
import net.minecraft.network.chat.Component

class SpecialHandlerMaskForEach(val mask: BooleanList) : SpecialHandler {
    override fun act(): Action{
        return InnerAction(mask)
    }

    override fun getName(): Component {
        val fingerprint = mask.map { if(it) '<' else '-' }.joinToString("")
        return HextraUtils.specialHandlerLang(HextraSpecialHandlers.MASK_FOR_EACH)
            .asTranslatedComponent(fingerprint).lightPurple
    }

    class InnerAction(val mask: BooleanList) : Action {
        override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
            var stack = image.stack
            val n = mask.size

            if(stack.size < 2 + n)
                throw MishapNotEnoughArgs(2 + n, stack.size)

            val datums = stack.getList(stack.lastIndex - 1, stack.size)
            val instrs = stack.getEvaluatable(stack.lastIndex, stack.size)
            stack = stack.dropRight(2)

            val instrList = instrs.map({ TreeList.from(listOf(it)) }, { it })

            val unmaskedContextStack = stack.takeRight(n)
            var stashedStack = stack.dropRight(n)
            var contextStack = TreeList.empty<Iota>()

            for((i, include) in mask.withIndex()){
                if(include){
                    contextStack = contextStack.appended(unmaskedContextStack[i])
                }else{
                    stashedStack = stashedStack.appended(unmaskedContextStack[i])
                }
            }

            val frame = FrameForEach(datums, instrList, contextStack, stashedStack, TreeList.empty())
            val image2 = image.withUsedOp().copy(stack = TreeList.empty())

            return OperationResult(image2, listOf(), continuation.pushFrame(frame), HexEvalSounds.THOTH.get())
        }
    }

    companion object{
        @JvmField
        val PREFIX = "awaaddw"

        fun createMask(prefix: String, pat: HexPattern): BooleanArrayList? {
            val sig = pat.anglesSignature()
            if (!sig.startsWith(prefix)) return null

            val tail = sig.substring(prefix.length)
            val mask = BooleanArrayList()

            var side = true
            var index = 0
            while (index < tail.length) {
                val curr = tail[index]
                if(curr == 'w'){
                    mask.add(false)
                }else if(curr == (if (side) 'd' else 'a')){
                    index++
                    if(index >= tail.length) return null
                    if(tail[index] == (if (side) 'a' else 'd')){
                        mask.add(true)
                    }else{
                        return null
                    }
                    side = !side
                }
                index++
            }

            return mask
        }
    }

    class Factory : SpecialHandler.Factory<SpecialHandlerMaskForEach> {
        override fun tryMatch(pat: HexPattern, env: CastingEnvironment): SpecialHandlerMaskForEach? {
            val mask = createMask(PREFIX, pat) ?: return null

            return SpecialHandlerMaskForEach(mask)
        }
    }
}
