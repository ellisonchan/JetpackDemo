package com.ellison.jetpackdemo.hilt.model.analysis

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class AnalysisModule {
    @Binds
    abstract fun bindAnalysisService(analysisService: AnalysisServiceImpl): AnalysisService
}