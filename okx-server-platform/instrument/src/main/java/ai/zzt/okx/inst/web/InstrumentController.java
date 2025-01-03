package ai.zzt.okx.inst.web;

import ai.zzt.okx.common.vo.R;
import ai.zzt.okx.okx_client.context.InstrumentContext;
import ai.zzt.okx.v5.entity.ws.pub.Instruments;
import ai.zzt.okx.v5.enumeration.ws.InstrumentType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhouzhitong
 * @since 2024/12/15
 **/
@RestController
@RequestMapping("/api/v1/instruments")
public class InstrumentController {

    @GetMapping("/swap/list")
    public R<List<String>> listId() {
        List<String> instIds = InstrumentContext.getAllEnableInstrumentsOfId(InstrumentType.SWAP);
        return R.ok(instIds);
    }

    @GetMapping("/leverInfo/{instId}")
    public R<?> getLever(@PathVariable("instId") String instId) {
        Instruments instruments = InstrumentContext.get(instId, InstrumentType.SWAP);
        // TODO
        return null;
    }

}
