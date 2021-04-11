package com.ellison.jetpackdemo.hilt.model.interceptor

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CallServerInterceptorOkHttpClient()
