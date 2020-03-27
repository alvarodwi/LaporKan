package com.pedo.laporkan.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

object Helpers {
    data class DoubleTups<out A, out B>(
        val first: A,
        val second: B
    )

    class DoubleTuple<A, B>(a: LiveData<A>, b: LiveData<B>) :
        MediatorLiveData<DoubleTups<A?, B?>>() {
        init {
            addSource(a) { value = DoubleTups(it, b.value) }
            addSource(b) { value = DoubleTups(a.value, it) }
        }
    }

    fun shortenName(name : String) : String{
        val splitName = name.split(" ")
        var length = 0
        var resultName = ""

        for(split in splitName){
            if(split.length + length + 1 < 20){
                length += split.length + 1
                resultName += split + " "
            }
        }

        return resultName
    }
}