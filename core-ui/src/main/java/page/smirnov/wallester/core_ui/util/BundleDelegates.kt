package page.smirnov.wallester.core_ui.util

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class BundleParcelable<B : Parcelable>(
    private val key: String
) : ReadWriteProperty<Fragment, B> {
    private var cache: B? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): B = cache
        ?: thisRef.arguments?.getParcelable<B>(key).also { cache = it }
        ?: throw IllegalArgumentException()

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: B) {
        (thisRef.arguments ?: Bundle().also { thisRef.arguments = it }).putParcelable(key, value)
        cache = value
    }
}

class BundleParcelableList<B : Parcelable>(
    private val key: String
) : ReadWriteProperty<Fragment, List<B>> {
    private var cache: List<B>? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): List<B> = cache
        ?: thisRef.arguments?.getParcelableArrayList<B>(key).also { cache = it }
        ?: throw IllegalArgumentException()

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: List<B>) {
        (thisRef.arguments ?: Bundle().also { thisRef.arguments = it }).getParcelableArrayList<B>(key)
        cache = value
    }
}

class BundleLong(
    private val key: String,
    private val defaultValue: Long = Long.MIN_VALUE
) : ReadWriteProperty<Fragment, Long> {
    private var cache: Long? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): Long = cache
        ?: thisRef.arguments?.getLong(key, defaultValue).also { cache = it }
        ?: throw IllegalArgumentException()

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: Long) {
        (thisRef.arguments ?: Bundle().also { thisRef.arguments = it }).putLong(key, value)
        cache = value
    }
}

class BundleDouble(
    private val key: String,
    private val defaultValue: Double = Double.NaN
) : ReadWriteProperty<Fragment, Double> {
    private var cache: Double? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): Double = cache
        ?: thisRef.arguments?.getDouble(key, defaultValue).also { cache = it }
        ?: throw IllegalArgumentException()

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: Double) {
        (thisRef.arguments ?: Bundle().also { thisRef.arguments = it }).putDouble(key, value)
        cache = value
    }
}

class BundleInt(
    private val key: String,
    private val defaultValue: Int = Int.MIN_VALUE
) : ReadWriteProperty<Fragment, Int> {
    private var cache: Int? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): Int = cache
        ?: thisRef.arguments?.getInt(key, defaultValue).also { cache = it }
        ?: throw IllegalArgumentException()

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: Int) {
        (thisRef.arguments ?: Bundle().also { thisRef.arguments = it }).putInt(key, value)
        cache = value
    }
}

class BundleBoolean(
    private val key: String,
    private val defaultValue: Boolean = false
) : ReadWriteProperty<Fragment, Boolean> {
    private var cache: Boolean? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): Boolean = cache
        ?: thisRef.arguments?.getBoolean(key, defaultValue).also { cache = it }
        ?: throw IllegalArgumentException()

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: Boolean) {
        (thisRef.arguments ?: Bundle().also { thisRef.arguments = it }).putBoolean(key, value)
        cache = value
    }
}

class BundleString(
    private val key: String,
    private val defaultValue: String = ""
) : ReadWriteProperty<Fragment, String> {
    private var cache: String? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): String = cache
        ?: thisRef.arguments?.getString(key, defaultValue).also { cache = it }
        ?: throw IllegalArgumentException()

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: String) {
        (thisRef.arguments ?: Bundle().also { thisRef.arguments = it }).putString(key, value)
        cache = value
    }
}

class BundleLongArray(
    private val key: String
) : ReadWriteProperty<Fragment, LongArray> {
    private var cache: LongArray? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): LongArray = cache
        ?: (thisRef.arguments?.getSerializable(key) as? LongArray).also { cache = it }
        ?: throw IllegalArgumentException()

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: LongArray) {
        (thisRef.arguments ?: Bundle().also { thisRef.arguments = it }).putSerializable(key, value)
        cache = value
    }
}
