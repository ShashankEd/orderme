package com.product.orderfromhere.model.server

import com.apollographql.apollo.ApolloClient

//val apolloClient = ApolloClient.Builder()
//    .serverUrl("http://10.0.2.2:8000/graphql/authenticate")
//    .build()


fun createApolloClient(url: String): ApolloClient {
    return ApolloClient.Builder()
        .serverUrl(url)
        .build()
}