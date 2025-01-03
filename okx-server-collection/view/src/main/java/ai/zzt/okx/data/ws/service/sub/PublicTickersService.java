package ai.zzt.okx.data.ws.service.sub;

import ai.zzt.okx.common.constant.WsOpCode;
import ai.zzt.okx.common.enums.TrendType;
import ai.zzt.okx.common.utils.InstUtils;
import ai.zzt.okx.common.web.base.ws.IParam;
import ai.zzt.okx.common.web.emus.ChannelType;
import ai.zzt.okx.data.vo.TrendContext;
import ai.zzt.okx.data.ws.context.WsClientContext;
import ai.zzt.okx.data.ws.request.vo.ChannelParams;
import ai.zzt.okx.data.ws.service.BasePublicWsService;
import ai.zzt.okx.data.ws.vo.ChanelVO;
import ai.zzt.okx.data.ws.vo.TickersVO;
import ai.zzt.okx.okx_client.context.MarketContextList;
import ai.zzt.okx.v5.entity.ws.pub.MarkPrice;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/10/28
 **/
@Service
@Slf4j
public class PublicTickersService extends BasePublicWsService<List<ChanelVO<TickersVO>>> {

    @Resource
    private TrendContext trendContext;

    @Resource
    private MarketContextList marketContextList;

    @Override
    public List<ChanelVO<TickersVO>> doService(WsClientContext wsContext) {
        IParam<ChannelParams> args = wsContext.getArgs();
        WsOpCode opCode = args.getOpCode();
        // 只处理订阅数据
        if (opCode != WsOpCode.REQUEST_SUB) {
            return null;
        }
        List<ChannelParams> params = args.getArgs();
        if (params == null) {
            return null;
        }
        for (ChannelParams param : params) {
            ChannelType channel = param.getChannel();
            if (channel == ChannelType.TICKERS) {
                return process();
            }
        }
        return null;
    }

    private List<ChanelVO<TickersVO>> process() {
        ChanelVO<TickersVO> upChannel = doProcess(TrendType.UP);
        ChanelVO<TickersVO> downChannel = doProcess(TrendType.DOWN);
        List<ChanelVO<TickersVO>> chanelVOList = new ArrayList<>();
        chanelVOList.add(upChannel);
        chanelVOList.add(downChannel);
        return chanelVOList;
    }

    private ChanelVO<TickersVO> doProcess(TrendType trend) {
        List<String> list = trendContext.getList(trend);
        ChanelVO<TickersVO> chanelVO = new ChanelVO<>();
        chanelVO.setChannel(ChannelType.TICKERS);
        chanelVO.setTrend(trend);

        List<TickersVO> data = new ArrayList<>();

        for (String instId : list) {
            MarkPrice mp = marketContextList.getMarkPrice(instId);
            TickersVO tickers = new TickersVO();
            tickers.setInstId(InstUtils.getBase(instId));
            tickers.setInstType(InstrumentType.SWAP);
            tickers.setMarkPx(mp.getMarkPx());
            tickers.setTs(mp.getTs());
            data.add(tickers);
        }
        chanelVO.setData(data);
        return chanelVO;
    }

}
