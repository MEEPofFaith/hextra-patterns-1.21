package com.meepoffaith.hextrapats.casting.arithmetic

import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic
import at.petrak.hexcasting.api.casting.arithmetic.engine.InvalidOperatorException
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBinary
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorUnary
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes.DOUBLE
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes.VEC3
import com.meepoffaith.hextrapats.casting.arithmetic.operators.vec.OperatorApproachVec
import com.meepoffaith.hextrapats.casting.arithmetic.operators.vec.OperatorTurnVec
import com.meepoffaith.hextrapats.registry.HextraActions.ANGLE_APPROACH
import com.meepoffaith.hextrapats.registry.HextraActions.ANGLE_DIST
import com.meepoffaith.hextrapats.registry.HextraActions.APPROACH
import com.meepoffaith.hextrapats.registry.HextraActions.CONSTRUCT_ABOUT_X
import com.meepoffaith.hextrapats.registry.HextraActions.CONSTRUCT_ABOUT_Y
import com.meepoffaith.hextrapats.registry.HextraActions.CONSTRUCT_ABOUT_Z
import com.meepoffaith.hextrapats.registry.HextraActions.DECREMENT
import com.meepoffaith.hextrapats.registry.HextraActions.INCREMENT
import com.meepoffaith.hextrapats.registry.HextraActions.INVERT
import com.meepoffaith.hextrapats.registry.HextraActions.NORMALIZE
import com.meepoffaith.hextrapats.registry.HextraActions.ROT_ABOUT_X
import com.meepoffaith.hextrapats.registry.HextraActions.ROT_ABOUT_Y
import com.meepoffaith.hextrapats.registry.HextraActions.ROT_ABOUT_Z
import com.meepoffaith.hextrapats.registry.HextraActions.ROUND_EXACT
import com.meepoffaith.hextrapats.registry.HextraActions.ROUND_INT
import com.meepoffaith.hextrapats.registry.HextraActions.VEC_GET_X
import com.meepoffaith.hextrapats.registry.HextraActions.VEC_GET_Y
import com.meepoffaith.hextrapats.registry.HextraActions.VEC_GET_Z
import com.meepoffaith.hextrapats.registry.HextraActions.VEC_SET_X
import com.meepoffaith.hextrapats.registry.HextraActions.VEC_SET_Y
import com.meepoffaith.hextrapats.registry.HextraActions.VEC_SET_Z
import com.meepoffaith.hextrapats.util.MathUtils
import com.meepoffaith.hextrapats.util.MathUtils.round
import com.meepoffaith.hextrapats.util.MathUtils.roundToInterval
import com.meepoffaith.hextrapats.util.MultiPreds
import net.minecraft.world.phys.Vec3
import java.util.function.BiFunction
import java.util.function.Function
import java.util.function.UnaryOperator
import kotlin.math.cos
import kotlin.math.sin

object Vec3Arithmetic : Arithmetic {
    private val OPS = listOf(
        ROT_ABOUT_X,
        ROT_ABOUT_Y,
        ROT_ABOUT_Z,
        CONSTRUCT_ABOUT_X,
        CONSTRUCT_ABOUT_Y,
        CONSTRUCT_ABOUT_Z,
        NORMALIZE,
        INVERT,
        INCREMENT,
        DECREMENT,
        APPROACH,
        ANGLE_DIST,
        ANGLE_APPROACH,
        VEC_GET_X,
        VEC_GET_Y,
        VEC_GET_Z,
        VEC_SET_X,
        VEC_SET_Y,
        VEC_SET_Z,
        ROUND_INT,
        ROUND_EXACT
    )

    override fun arithName() = "hextrapats_vec3_math"

    override fun opTypes() = OPS

    override fun getOperator(pattern: HexPattern): Operator = when(pattern){
        ROT_ABOUT_X -> makeVecNumToVec{ v, x ->  //Already clockwise
            val c = cos(x)
            val s = sin(x)
            Vec3(v.x, c * v.y + s * v.z, c * v.z - s * v.y)
        }
        ROT_ABOUT_Y -> makeVecNumToVec{ v, x ->
            var x = x
            x = -x //Negate to make it a clockwise rotation
            val c = cos(x)
            val s = sin(x)
            Vec3(c * v.x + s * v.z, v.y, c * v.z - s * v.x)
        }
        ROT_ABOUT_Z -> makeVecNumToVec{ v, x ->  //Already clockwise
            val c = cos(x)
            val s = sin(x)
            Vec3(c * v.x + s * v.y, c * v.y - s * v.x, v.z)
        }
        CONSTRUCT_ABOUT_X -> makeNumToVec{ a -> Vec3(0.0, sin(a), cos(a)) } //+Z is 0 rad
        CONSTRUCT_ABOUT_Y -> makeNumToVec{ a -> Vec3(-sin(a), 0.0, cos(a)) } //+Z is 0 rad. Matches player yaw in F3.
        CONSTRUCT_ABOUT_Z -> makeNumToVec{ a -> Vec3(-cos(a), sin(a), 0.0) } //-X is 0 rad
        NORMALIZE -> makeVecToVec{ v -> v.normalize() }
        INVERT -> makeVecToVec{ v -> v.scale(-1.0) }
        INCREMENT -> makeVecToVec{ v ->
            val len = v.length()
            if (DoubleIota.tolerates(len, 0.0)) v else v.scale((len + 1) / len)
        }
        DECREMENT -> makeVecToVec{ v ->
            val len = v.length()
            if (DoubleIota.tolerates(len, 0.0)) v else v.scale((len - 1) / len)
        }
        APPROACH -> OperatorApproachVec
        ANGLE_DIST -> makeVecVecToNum{ v1, v2 -> MathUtils.vecAngleDist(v1, v2) }
        ANGLE_APPROACH -> OperatorTurnVec
        VEC_GET_X -> makeVecToNum{ v -> v.x }
        VEC_GET_Y -> makeVecToNum{ v -> v.y }
        VEC_GET_Z -> makeVecToNum{ v -> v.z }
        VEC_SET_X -> makeVecNumToVec{ v, x -> Vec3(x, v.y, v.z) }
        VEC_SET_Y -> makeVecNumToVec{ v, y -> Vec3(v.x, y, v.z) }
        VEC_SET_Z -> makeVecNumToVec{ v, z -> Vec3(v.x, v.y, z) }
        ROUND_INT -> makeVecToVec{ v -> v.round() }
        ROUND_EXACT -> makeVecNumToVec{ v, n -> v.roundToInterval(n) }
        else -> throw InvalidOperatorException("$pattern is not a valid operator in Arithmetic $this.")
    }

    fun makeVecToVec(op: UnaryOperator<Vec3>) = OperatorUnary(MultiPreds.all(VEC3.get()))
        { i: Iota -> Vec3Iota(op.apply(Operator.downcast(i, VEC3.get()).vec3)) }

    fun makeNumToVec(op: Function<Double, Vec3>) = OperatorUnary(MultiPreds.all(DOUBLE.get()))
        { i: Iota -> Vec3Iota(op.apply(Operator.downcast(i, DOUBLE.get()).double)) }

    fun makeVecToNum(op: Function<Vec3, Double>) = OperatorUnary(MultiPreds.all(VEC3.get()))
        { i: Iota -> DoubleIota(op.apply(Operator.downcast(i, VEC3.get()).vec3)) }

    fun makeVecNumToVec(op: BiFunction<Vec3, Double, Vec3>) = OperatorBinary(MultiPreds.pair(VEC3.get(), DOUBLE.get()))
        { i: Iota, j: Iota -> Vec3Iota(op.apply(Operator.downcast(i, VEC3.get()).vec3, Operator.downcast(j, DOUBLE.get()).double)) }

    fun makeVecVecToNum(op: BiFunction<Vec3, Vec3, Double>) = OperatorBinary(MultiPreds.all(VEC3.get()))
        { i: Iota, j: Iota -> DoubleIota(op.apply(Operator.downcast(i, VEC3.get()).vec3, Operator.downcast(j, VEC3.get()).vec3)) }
}
