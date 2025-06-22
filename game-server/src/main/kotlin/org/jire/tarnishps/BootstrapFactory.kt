package org.jire.tarnishps

import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.ByteBufAllocator
import io.netty.buffer.PooledByteBufAllocator
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.IoHandlerFactory
import io.netty.channel.MultiThreadIoEventLoopGroup
import io.netty.channel.WriteBufferWaterMark
import io.netty.channel.epoll.Epoll
import io.netty.channel.epoll.EpollIoHandler
import io.netty.channel.epoll.EpollServerSocketChannel
import io.netty.channel.kqueue.KQueue
import io.netty.channel.kqueue.KQueueIoHandler
import io.netty.channel.kqueue.KQueueServerSocketChannel
import io.netty.channel.nio.NioIoHandler
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.uring.IoUring
import io.netty.channel.uring.IoUringIoHandler
import io.netty.channel.uring.IoUringServerSocketChannel

/**
 * @author Jire
 * @author Kris
 */
class BootstrapFactory
@JvmOverloads
constructor(
    private val alloc: ByteBufAllocator = PooledByteBufAllocator.DEFAULT,
) {

    /**
     * Creates an IO handler factory based on the best available event loop group.
     */
    fun createIoHandlerFactory(): IoHandlerFactory =
        when {
            IoUring.isAvailable() -> IoUringIoHandler.newFactory()
            Epoll.isAvailable() -> EpollIoHandler.newFactory()
            KQueue.isAvailable() -> KQueueIoHandler.newFactory()
            else -> NioIoHandler.newFactory()
        }

    /**
     * Creates a parent loop group with a single thread behind it, based on the best
     * available event loop group.
     */
    @JvmOverloads
    fun createParentLoopGroup(nThreads: Int = 1): EventLoopGroup =
        MultiThreadIoEventLoopGroup(nThreads, createIoHandlerFactory())

    /**
     * Creates a child loop group with a number of threads based on availableProcessors * 2,
     * which is done at Netty level.
     */
    @JvmOverloads
    fun createChildLoopGroup(nThreads: Int = 0): EventLoopGroup =
        MultiThreadIoEventLoopGroup(nThreads, createIoHandlerFactory())

    /**
     * Creates a server bootstrap using the parent and child event loop groups with
     * a configuration that closely resembles the values found in the client.
     */
    fun createServerBootstrap(
        parentGroup: EventLoopGroup,
        childGroup: EventLoopGroup,
    ): ServerBootstrap {
        val channel =
            when {
                IoUring.isAvailable() -> IoUringServerSocketChannel::class.java
                Epoll.isAvailable() -> EpollServerSocketChannel::class.java
                KQueue.isAvailable() -> KQueueServerSocketChannel::class.java
                else -> NioServerSocketChannel::class.java
            }
        return ServerBootstrap()
            .group(parentGroup, childGroup)
            .channel(channel)
            .option(ChannelOption.ALLOCATOR, alloc)
            .childOption(ChannelOption.ALLOCATOR, alloc)
            .childOption(ChannelOption.AUTO_READ, false)
            .childOption(ChannelOption.TCP_NODELAY, true)
            .childOption(ChannelOption.SO_RCVBUF, 65536)
            .childOption(ChannelOption.SO_SNDBUF, 65536)
            .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30_000)
            .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, WriteBufferWaterMark(524_288, 2_097_152))
    }

}
