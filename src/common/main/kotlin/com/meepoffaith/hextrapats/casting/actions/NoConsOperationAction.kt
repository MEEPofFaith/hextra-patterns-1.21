package com.meepoffaith.hextrapats.casting.actions

import at.petrak.hexcasting.api.casting.arithmetic.engine.NoOperatorCandidatesException
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidOperatorArgs
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.common.lib.hex.HexArithmetics

class NoConsOperationAction(val pattern: HexPattern, val argc: Int) : Action {
    override fun operate(
        env: CastingEnvironment,
        image: CastingImage,
        continuation: SpellContinuation
    ): OperationResult {
        val oldStack = image.stack
        val size = oldStack.size
        if(oldStack.size < argc){
            throw MishapNotEnoughArgs(argc, size)
        }

        try{
            val result = HexArithmetics.getEngine().run(pattern, env, image, continuation)
            val resultStack = result.newImage.stack
            return OperationResult(
                result.newImage.copy(stack = oldStack.appendedAll(resultStack.slice(size - argc, resultStack.size))),
                result.sideEffects, result.newContinuation, result.sound
            )
        } catch (e: NoOperatorCandidatesException) {
            throw MishapInvalidOperatorArgs(e.args)
        }
    }
}
