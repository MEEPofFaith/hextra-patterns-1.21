package com.meepoffaith.hextrapats.registry

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.castables.OperationAction
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.HexRegistries
import at.petrak.hexcasting.common.lib.hex.HexActions
import com.meepoffaith.hextrapats.casting.actions.NoConsOperationAction
import com.meepoffaith.hextrapats.casting.actions.eval.OpConditionalEval
import com.meepoffaith.hextrapats.casting.actions.eval.OpConditionalHalt
import com.meepoffaith.hextrapats.casting.actions.eval.OpStackForEach
import com.meepoffaith.hextrapats.casting.actions.lists.*
import com.meepoffaith.hextrapats.casting.actions.logic.OpNoConsBoolCoerce
import com.meepoffaith.hextrapats.casting.actions.logic.OpNoConsEquality
import com.meepoffaith.hextrapats.casting.actions.math.OpDegRad
import com.meepoffaith.hextrapats.casting.actions.math.OpRadDeg
import com.meepoffaith.hextrapats.casting.actions.math.OpRandRange
import com.meepoffaith.hextrapats.casting.actions.math.OpRandZero
import com.meepoffaith.hextrapats.casting.actions.nullary.OpNullExecute
import com.meepoffaith.hextrapats.casting.actions.sets.*
import com.meepoffaith.hextrapats.casting.actions.stack.OpFloat
import com.meepoffaith.hextrapats.casting.actions.stack.OpSink
import com.meepoffaith.hextrapats.casting.actions.vecmanip.OpVecLeftShift
import com.meepoffaith.hextrapats.casting.actions.vecmanip.OpVecList
import com.meepoffaith.hextrapats.casting.actions.vecmanip.OpVecRightShift
import com.meepoffaith.hextrapats.casting.actions.vecmanip.OpVecSwapXY
import com.meepoffaith.hextrapats.casting.actions.vecmanip.OpVecSwapXZ
import com.meepoffaith.hextrapats.casting.actions.vecmanip.OpVecSwapYZ
import com.meepoffaith.hextrapats.casting.actions.vecmath.*
import net.minecraft.world.phys.Vec3

object HextraActions : HextrapatsRegistrar<ActionRegistryEntry>(
    HexRegistries.ACTION,
    { HexActions.REGISTRY },
) {
    val ROT_ABOUT_X = makeArithOp("rot_about/x", HexDir.SOUTH_WEST, "aaqqqqqea")
    val ROT_ABOUT_Y = makeArithOp("rot_about/y", HexDir.SOUTH_WEST, "aaqqqqqew")
    val ROT_ABOUT_Z = makeArithOp("rot_about/z", HexDir.SOUTH_WEST, "aaqqqqqed")
    val CONSTRUCT_ABOUT_X = makeArithOp("cons_about/x", HexDir.NORTH_WEST, "daqqqqqea")
    val CONSTRUCT_ABOUT_Y = makeArithOp("cons_about/y", HexDir.NORTH_WEST, "daqqqqqew")
    val CONSTRUCT_ABOUT_Z = makeArithOp("cons_about/z", HexDir.NORTH_WEST, "daqqqqqed")
    val NORMALIZE = makeArithOp("normalize", HexDir.SOUTH_WEST, "eeeeedww")
    val LEN_EQ = makeArithOp("len_eq", HexDir.EAST, "adqqaqw")
    val LEN_NEQ = makeArithOp("len_neq", HexDir.EAST, "daeedew")
    val IN_RANGE = makeArithOp("in_range", HexDir.SOUTH_WEST, "qqqq")
    val OUT_RANGE = makeArithOp("out_range", HexDir.SOUTH_EAST, "eaae")
    val INVERT = makeArithOp("invert", HexDir.SOUTH_WEST, "waqawqa")
    val INCREMENT = makeArithOp("increment", HexDir.NORTH_EAST, "waawawaaw")
    val DECREMENT = makeArithOp("decrement", HexDir.NORTH_WEST, "wddwdwddw")
    val APPROACH = makeArithOp("approach", HexDir.SOUTH_WEST, "dedqadeeed")
    val ANGLE_DIST = makeArithOp("angle_dist", HexDir.NORTH_EAST, "awdaqqqqqea")
    val ANGLE_APPROACH = makeArithOp("angle_approach", HexDir.NORTH_EAST, "awdaqqqqqwd")
    val SET_INSERT_RET = makeArithOp("set_insert_ret", HexDir.SOUTH_WEST, "edqdewd")
    val SET_REMOVE_RET = makeArithOp("set_remove_ret", HexDir.SOUTH_WEST, "edqdewaqaaed")
    val VEC_GET_X = makeArithOp("vec/get/x", HexDir.EAST, "qeeeeedwwa")
    val VEC_GET_Y = makeArithOp("vec/get/y", HexDir.EAST, "qeeeeedwww")
    val VEC_GET_Z = makeArithOp("vec/get/z", HexDir.EAST, "qeeeeedwwd")
    val VEC_SET_X = makeArithOp("vec/set/x", HexDir.EAST, "eqqqqqawwa")
    val VEC_SET_Y = makeArithOp("vec/set/y", HexDir.EAST, "eqqqqqawww")
    val VEC_SET_Z = makeArithOp("vec/set/z", HexDir.EAST, "eqqqqqawwd")
    val ROUND_INT = makeArithOp("round/int", HexDir.EAST, "qdwae")
    val ROUND_EXACT = makeArithOp("round/exact", HexDir.EAST, "eawdq")

    val DEG_TO_RAD = make("deg_to_rad", HexDir.WEST, "qqqqqdwdq", OpDegRad)
    val RAD_TO_DEG = make("rad_to_deg", HexDir.NORTH_EAST, "qdwdqqqqq", OpRadDeg)
    val RAND_ZERO = make("rand_zero", HexDir.EAST, "dedqeqqq", OpRandZero)
    val RAND_RANGE = make("rand_range", HexDir.SOUTH_WEST, "eeeqeqqq", OpRandRange)

    val RAND_VEC = make("rand_vec", HexDir.EAST, "eeeeeqeqqq", OpRandVec)
    val VEC_DIST = make("vec_dist", HexDir.EAST, "aqqqqqeqeeeeed", OpVecDist)
    val FROM_POLAR = make("from_polar", HexDir.EAST, "eqqadaqa", OpFromPolar)
    val TO_POLAR = make("to_polar", HexDir.EAST, "qedadeed", OpToPolar)
    val SWAP_VEC_LIST = make("swap_vec_list", HexDir.WEST, "qqqqqdeee", OpVecList)

    //Come on, Elise!
    val CONST_VEC_P1 = make("haha_ha_one", HexDir.NORTH_WEST, "qqqqqeq", Action.makeConstantOp(Vec3Iota(Vec3(1.0, 1.0, 1.0))))
    val CONST_VEC_N1 = make("eno_ah_ahah", HexDir.SOUTH_WEST, "eeeeeqq", Action.makeConstantOp(Vec3Iota(Vec3(-1.0, -1.0, -1.0))))

    val PEEK_FRONT = make("peek/front", HexDir.SOUTH_WEST, "aaqwqaaq", OpPeek(false))
    val PEEK_BACK = make("peek/back", HexDir.NORTH_WEST, "qaeaqe", OpPeek(true))
    val PEEK_INDEX = make("peek/index", HexDir.NORTH_WEST, "deeedew", OpPeekAt)
    val SPLIT_LIST = make("split_list", HexDir.EAST, "wdedqqa", OpSplitList)
    val DEL_ELEMENT_FIRST = make("del_element/first", HexDir.NORTH_EAST, "dedwqaeaqa", OpDelete)
    val DEL_ELEMENT_ALL = make("del_element/all", HexDir.NORTH_EAST, "dedwqaeaqaw", OpDeleteAll)
    val SWINDLE_LIST = make("swindle_list", HexDir.WEST, "dqdeqaawddea", OpListSwindle)
    val SHUFFLE_LIST = make("scronglwfijspoivjqwofklcrvewb", HexDir.EAST, "ddedqdaqwdwaqawdwqaqww", OpShuffle)

    val NOCON_BOOL_COERCE = make("nocon/bool_coerce", HexDir.SOUTH_EAST, "ddaw", OpNoConsBoolCoerce)
    val NOCON_GREATER = makeNoCons("nocon/greater", HexDir.WEST, "ddwe", Arithmetic.GREATER, 2)
    val NOCON_LESS = makeNoCons("nocon/less", HexDir.WEST, "ddeq", Arithmetic.LESS, 2)
    val NOCON_GREATER_EQ = makeNoCons("nocon/greater_eq", HexDir.WEST, "ddwee", Arithmetic.GREATER_EQ, 2)
    val NOCON_LESS_EQ = makeNoCons("nocon/less_eq", HexDir.WEST, "ddeqq", Arithmetic.LESS_EQ, 2)
    val NOCON_LEN_EQ = makeNoCons("nocon/len_eq", HexDir.WEST, "ddqadqqaqw", LEN_EQ, 2)
    val NOCON_LEN_NEQ = makeNoCons("nocon/len_neq", HexDir.WEST, "ddqdaeedew", LEN_NEQ, 2)
    val NOCON_EQ = make("nocon/eq", HexDir.WEST, "ddqad", OpNoConsEquality(false))
    val NOCON_NEQ = make("nocon/neq", HexDir.WEST, "ddqda", OpNoConsEquality(true))

    val EMPTY_SET = make("empty_set", HexDir.SOUTH_EAST, "eedwaaw", OpEmptySet)
    val LAST_N_SET = make("last_n_set", HexDir.SOUTH_WEST, "ewdwaawaqde", OpLastNToSet)
    val SPLAT_SET = make("splat_set", HexDir.NORTH_WEST, "qwawddwdeaq", OpSplatSet)
    val SET_TO_LIST = make("set_to_list", HexDir.NORTH_WEST, "eedqddeqaaeaqq", OpSetToList)
    val LIST_TO_SET = make("list_to_set", HexDir.NORTH_EAST, "qqaeaadwaddqdee", OpListToSet)

    val TRUE_EVAL = make("true_eval", HexDir.SOUTH_EAST, "deaqqaaqa", OpConditionalEval(true))
    val FALSE_EVAL = make("false_eval", HexDir.SOUTH_EAST, "deaqqdded", OpConditionalEval(false))
    val TRUE_HALT = make("true_halt", HexDir.SOUTH_WEST, "aqdeedded", OpConditionalHalt(true))
    val FALSE_HALT = make("false_halt", HexDir.SOUTH_WEST, "aqdeeaaqa", OpConditionalHalt(false))
    val NULL_EXEC = make("null_eval", HexDir.SOUTH_EAST, "wawaadq", OpNullExecute)
    val STACK_FOREACH = make("stack_for_each", HexDir.SOUTH_WEST, "awaaddwd", OpStackForEach(false))
    val STACK_INDEXED_FOREACH = make("stack_indexed_for_each", HexDir.SOUTH_WEST, "aqaaqwaaddwd", OpStackForEach(true))

    val SINK_IOTA = make("capsizing", HexDir.WEST, "ddadeq", OpSink(false))
    val SINK_IOTA_COPY = make("capsizing/copy", HexDir.EAST, "aadaqe", OpSink(true))
    val FLOAT_IOTA = make("dredging", HexDir.WEST, "ddadaq", OpFloat(false))
    val FLOAT_IOTA_COPY = make("dredging/copy", HexDir.EAST, "aadade", OpFloat(true))

    private fun make(name: String, startDir: HexDir, signature: String, action: Action) =
        make(name, startDir, signature) { action }

    private fun make(name: String, startDir: HexDir, signature: String, getAction: () -> Action) = register(name) {
        ActionRegistryEntry(HexPattern.fromAngles(signature, startDir), getAction())
    }

    private fun makeArithOp(name: String, startDir: HexDir, signature: String): HexPattern {
        val pattern = HexPattern.fromAngles(signature, startDir)
        make(name, startDir, signature, OperationAction(pattern))
        return pattern
    }

    private fun makeNoCons(name: String, startDir: HexDir, signature: String, copied: HexPattern, argc: Int) =
        make(name, startDir, signature, NoConsOperationAction(copied, argc))
}
