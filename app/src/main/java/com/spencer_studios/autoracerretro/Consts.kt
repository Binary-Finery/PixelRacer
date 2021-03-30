package com.spencer_studios.autoracerretro

const val MAX_BLOCKS = 7
const val STEP = 500

fun launchFrequency(): Array<Int> {
    val max = MAX_BLOCKS * STEP
    val list = ArrayList<Int>()
    (0..max step STEP).forEach { i ->
        list.add((i..(i + 35)).random())
    }
    return list.toTypedArray()
}
