package org.http4k.streaming

import org.http4k.client.ApacheClient
import org.http4k.client.ResponseBodyMode
import org.http4k.core.HttpHandler
import org.http4k.server.ServerConfig
import org.http4k.server.SunHttp

class SunHttpStreamingTest : StreamingContract() {
    override fun serverConfig(port: Int): ServerConfig = SunHttp(port)

    override fun createClient(): HttpHandler = ApacheClient(bodyMode = ResponseBodyMode.Stream)

}