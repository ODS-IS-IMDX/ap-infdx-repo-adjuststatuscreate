// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import com.spatialid.app.Common;
import com.spatialid.app.model.FacilityDataTaskAdjustmentManagementBean;

/**
 * 設備データ整備管理ハンドラークラス
 * 設備データ整備管理テーブルに対する操作を行う
 * 
 * @author Ishii Yuki
 * @version 1.0
 */
public class FacilityDataTaskAdjustmentHandler {

	/**
	 * B2設備データ整備ステータス作成SQL実行
	 * 
	 * @param connection 接続情報
	 * @param taskInfo   整備タスク情報
	 * @throws SQLException
	 */
	public static void executeCreateFacilityDataImportManagementImportingStatement(
			Connection connection,
			FacilityDataTaskAdjustmentManagementBean taskInfo)
			throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = prepareCreateFacilityDataTaskAdjustmentStatement(
					connection, taskInfo);
			ps.executeUpdate();
		} catch (SQLException e) {
			Common.outputLogMessage("error.ChangeStatus.00004",
					Common.getCurrentMethodName(),
					(ps == null ? "" : ps.toString()),
					Common.getErrorInfoString(e));
			throw e;
		}
		Common.outputLogMessage("info.ChangeStatus.00006",
				Common.getCurrentMethodName(), ps.toString());
	}

	/**
	 * B2設備データ整備ステータス作成SQL作成
	 * 
	 * @param connection 接続情報
	 * @param taskInfo   整備タスク情報
	 * @return B2設備データ整備ステータス作成SQL
	 * @throws SQLException
	 */
	public static PreparedStatement prepareCreateFacilityDataTaskAdjustmentStatement(
			Connection connection,
			FacilityDataTaskAdjustmentManagementBean taskInfo)
			throws SQLException {
		String sql = "insert into facility_data_adjustment_management values (?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, taskInfo.getZipFileName());
		pstmt.setString(2, taskInfo.getInfraCompanyId());
		pstmt.setTimestamp(3, taskInfo.getProcessStartDate());
		pstmt.setTimestamp(4, taskInfo.getProcessEndDate());
		pstmt.setString(5, taskInfo.getAdjustStatus());
		pstmt.setString(6, taskInfo.getAdjustMessage());
		pstmt.setTimestamp(7,
				new Timestamp(System.currentTimeMillis()));

		return pstmt;
	}
}