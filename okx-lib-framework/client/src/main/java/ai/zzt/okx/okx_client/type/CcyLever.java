package ai.zzt.okx.okx_client.type;

import lombok.Getter;

/**
 * 币币分类等级
 *
 * @author zhouzhitong
 * @since 2024/6/20
 **/
@Getter
@Deprecated
public enum CcyLever {

    // 币币分类等级: 主流、热门、Layer1、Layer2、Meme、铭文、人工智能
    MAINSTREAM("主流"),
    HOT("热门"),
    LAYER1("Layer1"),
    LAYER2("Layer2"),
    MEME("Meme"),
    INSCRIPTION("铭文"),
    AI("人工智能"),
    ;

    private final String name;

    CcyLever(String name) {
        this.name = name;
    }
}
