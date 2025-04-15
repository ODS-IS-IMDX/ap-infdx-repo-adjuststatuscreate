// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.model;

import lombok.Data;

/**
 * リクエスト項目情報クラス
 * リクエスト項目情報(項目名、値)を格納する
 * 
 * @author Ishii Yuki
 * @version 1.0
 */
@Data
public class RequestItemInfo {

    /**
     * 論理名
     */
    private String japaneseName;

    /**
     * 物理名
     */
    private String physicalName;

    public RequestItemInfo(String japaneseName, String physicalName) {
        this.japaneseName = japaneseName;
        this.physicalName = physicalName;
    }
}