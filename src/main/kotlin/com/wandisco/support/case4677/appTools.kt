package com.wandisco.support.case4677

fun Array<String>.getAsArg(name: String): String? {
    val item: String? = firstOrNull {
        it.startsWith("${name}=")
    }
    return if (item == null) {
        null
    } else {
        item.drop(name.length + 1)
    }
}

fun Array<String>.getAsArg(name: String, default: String): String {
    return getAsArg(name) ?: default
}

fun <E: Throwable> handleException(e: E) {
    println("Got ${e::class.simpleName}")
    println("Message: ${e.message}")
    println("Cause: ${e.cause}")
    e.printStackTrace(System.out)
}

inline fun <reified E: Throwable> Throwable.filterThrowable(block: (te: E) -> Unit) {
    if (this is E) {
        block(this)
    }
}
