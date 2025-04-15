// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.model;

import java.sql.Timestamp;
import lombok.Data;

/**
 * 設備データ整備管理タスク情報Beanクラス
 * 設備データ整備管理テーブルから取得する情報を格納する
 * 
 * @author Ishii Yuki
 * @version 1.0
 */
@Data
public class FacilityDataTaskAdjustmentManagementBean {

    /**
     * Zipファイル名
     */
    private String zipFileName;

    /**
     * インフラ事業者ID
     */
    private String infraCompanyId;

    /**
     * 処理開始日時
     */
    private Timestamp processStartDate;

    /**
     * 処理終了日時
     */
    private Timestamp processEndDate;

    /**
     * 整備状況
     */
    private String adjustStatus;

    /**
     * 整備メッセージ
     */
    private String adjustMessage;

    public FacilityDataTaskAdjustmentManagementBean(String highestVoxelSid,
        String infraCompanyId,
        Timestamp processStartDate, Timestamp processEndDate,
        String adjustStatus, String adjustMessage) {
        this.zipFileName = highestVoxelSid;
        this.infraCompanyId = infraCompanyId;
        this.processStartDate = processStartDate;
        this.processEndDate = processEndDate;
        this.adjustStatus = adjustStatus;
        this.adjustMessage = adjustMessage;
    }
}