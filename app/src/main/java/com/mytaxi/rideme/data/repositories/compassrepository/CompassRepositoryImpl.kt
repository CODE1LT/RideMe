package com.mytaxi.rideme.data.repositories.compassrepository

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import com.mytaxi.rideme.logger.Logger
import javax.inject.Inject

class CompassRepositoryImpl @Inject constructor(
    private val logger: Logger,
    private val compass: Compass
): CompassRepository, CompassListener  {

    companion object {
        private const val TAG = "CompassRepositoryImpl"
    }

    private val azimuthSubject = PublishSubject.create<Float>()

    override fun getAzimuth(): Observable<Float> {
        logger.d(TAG, "getAzimuth()")
        return azimuthSubject
    }

    override fun onNewAzimuth(azimuth: Float) {
        azimuthSubject.onNext(azimuth)
    }

    override fun startCompass(): Completable {
        logger.d(TAG, "startCompass()")
        compass.setListener(this)
        compass.start()
        return Completable.complete()
    }

    override fun stopCompass(): Completable {
        logger.d(TAG, "stopCompass()")
        compass.stop()
        return Completable.complete()
    }

}