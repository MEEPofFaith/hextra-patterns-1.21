package com.meepoffaith.hextrapats.casting.actions.eval

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.getEvaluatable
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.common.casting.actions.eval.OpEval
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds

class OpConditionalEval(val evalCond: Boolean) : Action {
    override fun operate(
        env: CastingEnvironment,
        image: CastingImage,
        continuation: SpellContinuation
    ): OperationResult {
        var stack = image.stack
        if(stack.size < 2) throw MishapNotEnoughArgs(2, stack.size)

        val instrs = stack.getEvaluatable(stack.lastIndex, stack.size)
        val bool = stack[stack.lastIndex - 1].isTruthy(env)
        stack = stack.dropRight(2)

        if(bool == evalCond){
            return OpEval.exec(env, image, continuation, stack, instrs)
        }else{
            val image2 = image.withUsedOp().copy(stack = stack)
            return OperationResult(image2, listOf(), continuation, HexEvalSounds.NORMAL_EXECUTE.get())
        }
    }
}
