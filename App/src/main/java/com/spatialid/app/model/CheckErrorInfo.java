// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.model;

import lombok.Data;

/**
 * 入力チェックエラー情報クラス
 * 入力チェックエラー情報を格納する
 * 
 * @author Ishii Yuki
 * @version 1.0
 */
@Data
public class CheckErrorInfo {

    /**
     * エラー情報
     */
    private String errorString;

    public CheckErrorInfo(String errorString) {
        this.errorString = errorString;
    }
}