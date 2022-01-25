package com.example.maktab67

import kotlin.random.Random

fun List<Int>.twoLoop(target: Int) : Boolean {
    var value: Int
    for (i in this.indices) {
        value = this[i]
        for (j in i + 1 until this.size)
            if (value + this[j] == target) {
                return true
            }
    }
    return false
}

fun List<Int>.sort(): List<Int> {
    return mergeSort(this)
}

fun mergeSort(list: List<Int>): List<Int> {
    val mid = list.size / 2
    val left = mergeSort(list.subList(0, mid))
    val right = mergeSort(list.subList(mid, list.size))
    return merge(left, right)
}

fun merge(left: List<Int>, right: List<Int>) : List<Int> {
    val sizeL = left.size
    val sizeR = right.size
    val result = MutableList(sizeL + sizeR) {0}
    var l = 0
    var r = 0
    while (l < sizeL && r < sizeR) {
        if (left[l] > right[r]) {
            result.add(right[r++])
        } else {
            result.add(left[l++])
        }
    }
    while (l < sizeL) {
        result.add(left[l++])
    }
    while (r < sizeR) {
        result.add(right[r++])
    }
    return result
}

fun List<Int>.sortAndBinarySearch(target: Int) : Boolean {
    val sorted = this.sort()
    var i = 0
    var value = sorted[i]
    while (value < target) {
        if (binarySearch(target - value, i + 1, size, sorted)) return true
        value = sorted[++i]
    }
    return false
}

fun binarySearch(target: Int, l: Int, r: Int, list: List<Int>): Boolean {
    if (l + 1 >= r) {
        return false
    }
    val middle = (r + l) / 2
    return if (list[middle] > target) {
        binarySearch(target, l, middle, list)
    } else if (list[middle] < target) {
        binarySearch(target, middle, r, list)
    } else {
        true
    }
}

fun List<Int>.sortAndTwoPointerSearch(target: Int) : Boolean {
    val sorted = this.sort()
    var start = 0
    var end = size - 1
    var sum: Int
    while (start < end) {
        sum = this[start] + this[end]
        if (sum < target) {
            start++
        } else if (sum > target) {
            end--
        } else {
            return true
        }
    }
    return false
}

fun List<Int>.hashTable(target: Int) : Boolean {
    val hashTable = Array(target) { 0 }
    val size = target - 1
    var key: Int
    for (i in this) {
        key = i % target
        if (hashTable[size - key] == 1) {
            return true
        }
        hashTable[key] = 1
    }
    return false
}


fun main() {
    val random = Random(System.currentTimeMillis())
    val list = List(100_000) {
        random.nextInt(-10_000, 10_000)
    }

    val target = random.nextInt()

    var start = System.currentTimeMillis()
    println(list.hashTable(target))
    println("hashTable time: " + (System.currentTimeMillis() - start))

    start = System.currentTimeMillis()
    println(list.sortAndBinarySearch(target))
    println("sortAndBinarySearch time: " + (System.currentTimeMillis() - start))

    start = System.currentTimeMillis()
    println(list.sortAndTwoPointerSearch(target))
    println("sortAndTwoPointerSearch time: " + (System.currentTimeMillis() - start))

    start = System.currentTimeMillis()
    println(list.twoLoop(target))
    println("twoLoop time: " + (System.currentTimeMillis() - start))
}