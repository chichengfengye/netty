package gateway.handler.ab;

import gateway.common.ReturnResult;
import gateway.exception.NettyHandlerException;
import gateway.helper.ResponseBackHelper;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;

public abstract class AbstractInBoundSimpleHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    protected abstract void businessRead2(ChannelHandlerContext ctx, FullHttpRequest msg) throws NettyHandlerException;

    /**
     * 需要重写，如果是最后一个handler，需要重写为true
     * @return
     */
    protected boolean isLast(){
        return false;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        try {
            businessRead2(ctx, msg);
            if (!isLast()) {

                //                ctx.fireChannelRead(msg);
            }
        }catch (NettyHandlerException e){
            System.out.println(e.getCode() + ":" + e.getMsg());
            ctx.writeAndFlush(ResponseBackHelper.httpJsonResponse(Unpooled.copiedBuffer("404 \n resource not found", CharsetUtil.UTF_8)));
            ctx.close();
        }
      /*  ReturnResult returnResult = businessRead(ctx, msg);
        if (ReturnResult.isSuccess(returnResult)) {
            ctx.fireChannelRead(msg);
        } else {
            System.out.println(returnResult.getCode() + ":" + returnResult.getMsg());
            ctx.writeAndFlush(ResponseBackHelper.httpJsonResponse(Unpooled.copiedBuffer("404 \n resource not found", CharsetUtil.UTF_8)));
            ctx.close();
        }*/
    }


}
