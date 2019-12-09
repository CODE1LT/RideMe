package com.mytaxi.rideme.base

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import polanski.option.Option
import polanski.option.OptionUnsafe

class UnwrapOptionTransformer<T> : ObservableTransformer<Option<T>, T> {

    override fun apply(upstream: Observable<Option<T>>): ObservableSource<T> {
        return upstream.filter { it.isSome }
            .map { OptionUnsafe.getUnsafe(it) }
    }

    companion object {
        fun <T> create(): UnwrapOptionTransformer<T> {
            return UnwrapOptionTransformer()
        }
    }
}