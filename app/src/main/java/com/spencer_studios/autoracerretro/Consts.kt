package com.spencer_studios.autoracerretro

fun launchFrequency(): Array<Int> {
    val list = ArrayList<Int>()
    (0..4_000 step 500).forEach { i -> list.add((i..(i +75)).random()) }
    list.add(6000)
    list.add(10_000)
    return list.toTypedArray()
}
