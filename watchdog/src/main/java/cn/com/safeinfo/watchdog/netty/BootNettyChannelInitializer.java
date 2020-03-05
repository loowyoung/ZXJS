package cn.com.safeinfo.watchdog.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 通道初始化
 *
 * @author: ly
 * @date: 2020/3/4 18:34
 */

public class BootNettyChannelInitializer<SocketChannel> extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        // ChannelOutboundHandler，依照逆序执行
        ch.pipeline().addLast("encoder", new StringEncoder());
        // 属于ChannelInboundHandler，依照顺序执行
        ch.pipeline().addLast("decoder", new StringDecoder());
        //自定义ChannelInboundHandlerAdapter
        ch.pipeline().addLast(new BootNettyChannelInboundHandlerAdapter());

    }

}