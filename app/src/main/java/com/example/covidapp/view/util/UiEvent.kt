package com.example.covidapp.view.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class EventObserver<T>(private val handler: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event?.getContentIfNotHandled()?.let { handler(it) }
    }
}

class EventLiveData<T> : MutableLiveData<Event<T>>() {
    fun postAsEvent(value: T) {
        super.postValue(Event(value))
    }

    fun setEventValue(value: T) {
        super.setValue(Event(value))
    }

    fun observeEvents(lifecycleOwner: LifecycleOwner, observer: (T) -> Unit): EventObserver<T> {
        val eventObserver = EventObserver<T> { observer(it) }
        observe(lifecycleOwner, eventObserver)
        return eventObserver
    }

    fun observeForever(observer: (T) -> Unit) {
        observeForever(EventObserver { observer(it) })
    }
}

open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}
