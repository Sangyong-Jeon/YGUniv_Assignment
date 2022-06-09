package kr.tutorials.yguniv_anonymous_post.dto

data class ResponseData<T>(var header: Header, var body: T)