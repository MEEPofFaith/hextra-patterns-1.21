package com.meepoffaith.hextrapats.casting.arithmetic

import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic
import at.petrak.hexcasting.api.casting.arithmetic.engine.InvalidOperatorException
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBinary
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorUnary
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes.DOUBLE
import com.meepoffaith.hextrapats.casting.arithmetic.operators.num.OperatorApproach
import com.meepoffaith.hextrapats.casting.arithmetic.operators.num.OperatorTurn
import com.meepoffaith.hextrapats.registry.HextraActions.ANGLE_APPROACH
import com.meepoffaith.hextrapats.registry.HextraActions.ANGLE_DIST
import com.meepoffaith.hextrapats.registry.HextraActions.APPROACH
import com.meepoffaith.hextrapats.registry.HextraActions.DECREMENT
import com.meepoffaith.hextrapats.registry.HextraActions.INCREMENT
import com.meepoffaith.hextrapats.registry.HextraActions.INVERT
import com.meepoffaith.hextrapats.registry.HextraActions.ROUND_EXACT
import com.meepoffaith.hextrapats.registry.HextraActions.ROUND_INT
import com.meepoffaith.hextrapats.util.MathUtils
import com.meepoffaith.hextrapats.util.MathUtils.roundToInterval
import java.util.function.DoubleBinaryOperator
import java.util.function.DoubleUnaryOperator
import kotlin.math.roundToLong


object NumArithmetic : Arithmetic {
    private val OPS = listOf(
        INVERT,
        INCREMENT,
        DECREMENT,
        APPROACH,
        ANGLE_DIST,
        ANGLE_APPROACH,
        ROUND_INT,
        ROUND_EXACT
    );

    override fun arithName() = "hextrapats_double_math"

    override fun opTypes() = OPS

    override fun getOperator(pattern: HexPattern): Operator = when(pattern){
        INVERT -> make1{ d -> -d }
        INCREMENT -> make1{ d -> d + 1 }
        DECREMENT -> make1{ d -> d - 1 }
        APPROACH -> OperatorApproach
        ANGLE_DIST -> make2{ a, b -> MathUtils.angleDist(a, b) }
        ANGLE_APPROACH -> OperatorTurn
        ROUND_INT -> make1{ d -> d.roundToLong().toDouble() }
        ROUND_EXACT -> make2{ a, b -> a.roundToInterval(b) }
        else -> throw InvalidOperatorException("$pattern is not a valid operator in Arithmetic $this.")
    }

    //Directly taken from DoubleArithmetic.kt.
    val ACCEPTS: () -> IotaMultiPredicate = { IotaMultiPredicate.all(IotaPredicate.ofType(DOUBLE.get())) }

    fun make1(op: DoubleUnaryOperator) = OperatorUnary(ACCEPTS())
        { i: Iota -> DoubleIota(op.applyAsDouble(Operator.downcast(i, DOUBLE.get()).double)) }

    fun make2(op: DoubleBinaryOperator) = OperatorBinary(ACCEPTS())
        { i: Iota, j: Iota -> DoubleIota(op.applyAsDouble(Operator.downcast(i, DOUBLE.get()).double, Operator.downcast(j, DOUBLE.get()).double)) }
}
