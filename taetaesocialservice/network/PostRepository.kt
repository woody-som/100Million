package com.taetae.taetaesocialservice.network

import Post
import PostRequest
import com.taetae.taetaesocialservice.BuildConfig
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

object PostRepository {
    //포스트 URL
    private const val postsUrl = "https://hkqnznjnnjgkylcscuyy.supabase.co/rest/v1/posts"
    private const val TAG = "PostRepository"

    //패치 올 포스트, 모든 포스트 가져오기
    suspend fun fetchAllPosts() : List<Post> {
        return KtorClient.httpClient.get(postsUrl) {
            url {
                parameters.append("select","*")
            }
            headers {
                headers.append("Authorization","Bearer ${BuildConfig.SUPABASE_KEY}")
            }
        }.body<List<Post>>()
    }
    // 단일 포스트 가져오기
    suspend fun fetchPostItem(postId: String) : Post {
        return KtorClient.httpClient.get(postsUrl) {
            url {
                parameters.append("select","*")
                parameters.append("id","eq.$postId")
            }
            headers {
                headers.append("Authorization","Bearer ${BuildConfig.SUPABASE_KEY}")
            }
        }.body<List<Post>>()[0]
    }
    //포스트 삭제
    suspend fun deletePostItem(postId: String) : HttpResponse {
        return KtorClient.httpClient.delete(postsUrl) {
            url {
                parameters.append("id","eq.$postId")
            }
            headers {
                headers.append("Authorization","Bearer ${BuildConfig.SUPABASE_KEY}")
            }
        }
    }
    // 단일 포스트 추가
    suspend fun addPostItem(
        title: String,
        content: String? = null
    ) : HttpResponse {
        return KtorClient.httpClient.post(postsUrl) {
            headers {
                headers.append("Authorization","Bearer ${BuildConfig.SUPABASE_KEY}")
            }
            setBody(PostRequest(title, content, UserInfo.userId))
        }
    }
    // 단일 포스트 수정하기
    suspend fun editPostItem(
        postId: String,
        title: String,
        content: String? = null
    ) : HttpResponse {
        return KtorClient.httpClient.patch(postsUrl) {
            url {
                parameters.append("id","eq.$postId")
            }
            headers {
                headers.append("Authorization","Bearer ${BuildConfig.SUPABASE_KEY}")
            }
            setBody(PostRequest(postId, title, content))
        }
    }
}