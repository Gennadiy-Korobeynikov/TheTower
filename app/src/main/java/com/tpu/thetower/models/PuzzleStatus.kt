package com.tpu.thetower.models

enum class PuzzleStatus(val value: String) {
    LOCKED("locked"),
    IN_PROGRESS("in_progress"),
    COMPLETED("completed");

    companion object {
        fun from(raw: String?): PuzzleStatus =
            entries.firstOrNull { it.value == raw } ?: LOCKED
    }
}
