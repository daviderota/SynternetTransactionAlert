package ro.da.synternet.common

import java.lang.RuntimeException

/**
 * Wrapper class for data that needs to be loaded.
 *
 * This class serves a wrapper for data that needs to be loaded, and carries information about the
 * status of the loading job, along with the data itself or, in case of failure, the cause of that.
 *
 * @param T The type of data wrapped by the [Resource].
 *
 * @since 1.0.0
 */
sealed class Resource<T> {
    /**
     * Utility method to obtain data from any resource.
     *
     * This method is meant to be used as a way of unpacking the data regardless of the state of the
     * resource.
     *
     * @return The data held by the resource, in nullable form, for there is no guarantee that this
     * resource is a [Success].
     *
     * @since 1.1.0
     */
    abstract fun unpack(): T?
}

/**
 * Wrapper class for successfully loaded data.
 *
 * This concrete [Resource] extension represents successfully loaded data.
 * A non-null reference to the data is required to instance it.
 *
 * @constructor Returns the Success [Resource] with the provided data of type [T].
 * @param T The type of data wrapped by the [Resource].
 * @param data The data loaded by this resource.
 *
 * @see Failure
 * @see Loading
 * @since 1.0.0
 */
data class Success<T>(val data: T): Resource<T>() {
    /**
     * Utility method to obtain data from any resource.
     *
     * This method is meant to be used as a way of unpacking the data regardless of the state of the
     * resource.
     *
     * @return The data held by the resource, in nullable form. It is equal to accessing the [data]
     * property.
     *
     * @since 1.1.0
     */
    override fun unpack(): T? = data
}

/**
 * Wrapper class for a resource that failed in loading its data.
 *
 * This concrete [Resource] extension represents data that failed to load.
 * It optionally carries the cause of the failure in the form of a [Throwable].
 * If no custom throwable is provided, a default [RuntimeException] is set.
 *
 * @constructor Returns the Failure [Resource] with the provided cause.
 * @param T The type of data wrapped by the [Resource].
 * @param error Optional error that caused the resource to fail. If no value is provided, a default
 * [RuntimeException] is set in its place.
 *
 * @property DEFAULT_ERROR The default [RuntimeException] used when no custom [Throwable] is provided.
 *
 * @see Success
 * @see Loading
 * @since 1.0.0
 */
data class Failure<T>(val error: Throwable = DEFAULT_ERROR): Resource<T>() {
    /**
     * Utility method to obtain data from any resource.
     *
     * This method is meant to be used as a way of unpacking the data regardless of the state of the
     * resource.
     *
     * @return The data held by the resource, in nullable form. For a Failure resource, this always
     * returns null.
     *
     * @since 1.1.0
     */
    override fun unpack(): T? = null

    companion object {
        val DEFAULT_ERROR = RuntimeException("Resource Failed to load.")
    }
}

/**
 * Wrapper class for a resource that is still loading its data.
 *
 * This concrete [Resource] extension represents a loading job that is still running, and does not
 * have any data yet.
 *
 * @param T The type of data wrapped by the [Resource].
 *
 * @see Success
 * @see Failure
 * @since 1.0.0
 */
class Loading<T> : Resource<T>() {
    /**
     * Utility method to obtain data from any resource.
     *
     * This method is meant to be used as a way of unpacking the data regardless of the state of the
     * resource.
     *
     * @return The data held by the resource, in nullable form, For a Loading resource, this always
     * returns null.
     *
     * @since 1.1.0
     */
    override fun unpack(): T? = null

    /**
     * Custom Equals override.
     *
     * Implemented so that two objects of type [Loading] will be considered equal when compared.
     *
     * @param other The second term of the comparison
     * @return true if [other] is of type [Loading], false otherwise.
     *
     * @since 1.1.1
     */
    override fun equals(other: Any?): Boolean {
        return other is Loading<*>
    }
}