package com.doubleclick.pizzastation.android.views.HiveLayoutManger

class HiveBucket {
    val BITS_PER_WORD = java.lang.Long.SIZE

    val LAST_BIT = 1L shl java.lang.Long.SIZE - 1

    var mData: Long = 0

    var mNext: HiveBucket? = null

    fun set(index: Int) {
        if (index >= BITS_PER_WORD) {
            ensureNext()
            mNext!!.set(index - BITS_PER_WORD)
        } else {
            mData = mData or (1L shl index)
        }
    }

    private fun ensureNext() {
        if (mNext == null) {
            mNext = HiveBucket()
        }
    }

    fun clear(index: Int) {
        if (index >= BITS_PER_WORD) {
            if (mNext != null) {
                mNext!!.clear(index - BITS_PER_WORD)
            }
        } else {
            mData = mData and (1L shl index).inv()
        }
    }

    operator fun get(index: Int): Boolean {
        return if (index >= BITS_PER_WORD) {
            ensureNext()
            mNext!![index - BITS_PER_WORD]
        } else {
            mData and (1L shl index) != 0L
        }
    }

    fun reset() {
        mData = 0
        if (mNext != null) {
            mNext!!.reset()
        }
    }

    fun insert(index: Int, value: Boolean) {
        if (index >= BITS_PER_WORD) {
            ensureNext()
            mNext!!.insert(index - BITS_PER_WORD, value)
        } else {
            val lastBit = mData and LAST_BIT != 0L
            val mask = (1L shl index) - 1
            val before = mData and mask
            val after = mData and mask.inv() shl 1
            mData = before or after
            if (value) {
                set(index)
            } else {
                clear(index)
            }
            if (lastBit || mNext != null) {
                ensureNext()
                mNext!!.insert(0, lastBit)
            }
        }
    }

    fun remove(index: Int): Boolean {
        return if (index >= BITS_PER_WORD) {
            ensureNext()
            mNext!!.remove(index - BITS_PER_WORD)
        } else {
            var mask = 1L shl index
            val value = mData and mask != 0L
            mData = mData and mask.inv()
            mask = mask - 1
            val before = mData and mask
            // cannot use >> because it adds one.
            val after = java.lang.Long.rotateRight(mData and mask.inv(), 1)
            mData = before or after
            if (mNext != null) {
                if (mNext!![0]) {
                    set(BITS_PER_WORD - 1)
                }
                mNext!!.remove(0)
            }
            value
        }
    }

    fun countOnesBefore(index: Int): Int {
        if (mNext == null) {
            return if (index >= BITS_PER_WORD) {
                java.lang.Long.bitCount(mData)
            } else java.lang.Long.bitCount(mData and (1L shl index) - 1)
        }
        return if (index < BITS_PER_WORD) {
            java.lang.Long.bitCount(mData and (1L shl index) - 1)
        } else {
            mNext!!.countOnesBefore(index - BITS_PER_WORD) + java.lang.Long.bitCount(mData)
        }
    }

    override fun toString(): String {
        return if (mNext == null) java.lang.Long.toBinaryString(mData) else mNext.toString() + "xx" + java.lang.Long.toBinaryString(
            mData
        )
    }
}
