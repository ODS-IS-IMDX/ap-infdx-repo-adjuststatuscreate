// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.ApplicationLoadBalancerRequestEvent;
import com.amazonaws.services.lambda.runtime.events.ApplicationLoadBalancerResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spatialid.app.dao.FacilityDataTaskAdjustmentHandler;
import com.spatialid.app.model.CheckErrorInfo;
import com.spatialid.app.model.FacilityDataTaskAdjustmentManagementBean;

/**
 * 設備データ整備完了受付(B2設備データ整備ステータス作成)クラス
 * 関数でB2設備データ整備ステータスの作成を行う
 * 
 * @author Ishii Yuki
 * @version 1.0
 */
public class LambdaHandler implements
		RequestHandler<ApplicationLoadBalancerRequestEvent, ApplicationLoadBalancerResponseEvent> {

	@Override
	public ApplicationLoadBalancerResponseEvent handleRequest(
			ApplicationLoadBalancerRequestEvent request, Context context) {
		// ログの出力(バッチ処理開始)
		Common.outputLogMessage("info.ChangeStatus.00001",
				Const.FUNCTION_NAME);
		LoggerFactory.getLogger(LambdaHandler.class).info(request.toString());

		FacilityDataTaskAdjustmentManagementBean task = null;
		CheckErrorInfo checkErrorInfo = new CheckErrorInfo(null);

		try {
			// 設定読込(プロパティファイル)
			Common.readProperty();
			// 設定読込(SecretManager)
			Common.readSecretManager();
		} catch (Exception e) {
			// ログの出力(システムエラー)
			Common.outputLogMessage("error.ChangeStatus.00001",
					Const.FUNCTION_NAME,
					Common.getErrorInfoString(e));
			return getResponse(Const.HTTP_500, Common.getLogMessage(
					"error.ChangeStatus.20001"));
		}

		try {
			// リクエスト情報取得
			task = getFacilityDataTaskAdjustmentManagementBean(
					request, checkErrorInfo);
		} catch (Exception e) {
			// ログの出力(リクエスト項目エラー)
			Common.outputLogMessage("error.ChangeStatus.00002",
					Const.FUNCTION_NAME,
					Common.getErrorInfoString(e));
			return getResponse(Const.HTTP_422,
					checkErrorInfo.getErrorString());
		}

		try (Connection connection = DriverManager.getConnection(
				Common.getDburl(), Common.getDbuser(),
				Common.getDbpassword())) {

			FacilityDataTaskAdjustmentHandler
					.executeCreateFacilityDataImportManagementImportingStatement(
							connection, task);
		} catch (SQLException e) {
			if ("23505".equals(e.getSQLState())) {
				// キー重複エラー
				return getResponse(Const.HTTP_409, Common
						.getLogMessage(
								"error.ChangeStatus.20003"));
			} else {
				// ログの出力(システムエラー)
				Common.outputLogMessage(
						"error.ChangeStatus.00001",
						Const.FUNCTION_NAME,
						Common.getErrorInfoString(
								e));
				return getResponse(Const.HTTP_500,
						Common.getLogMessage(
								"error.ChangeStatus.20001"));
			}
		}

		// ログの出力(バッチ処理正常終了)
		Common.outputLogMessage("info.ChangeStatus.00002",
				Const.FUNCTION_NAME);
		return getResponse(Const.HTTP_200, null);
	}

	/**
	 * レスポンスを取得する
	 * 
	 * @param httpStatus  HTTPステータスコード
	 * @param errorString エラー文
	 * @return レスポンス
	 */
	private ApplicationLoadBalancerResponseEvent getResponse(int httpStatus,
			String errorString) {
		ApplicationLoadBalancerResponseEvent res = new ApplicationLoadBalancerResponseEvent();
		// ヘッダを設定
		res.setStatusCode(httpStatus);

		// ボディを作成
		Map<String, Object> response = new LinkedHashMap<>();
		response.put(Const.RESPONSE_NAME_HTTPSTATUS, httpStatus);

		// エラー情報が存在する場合、エラー詳細を追加
		if (errorString != null) {
			Map<String, Object> errorDetail = new LinkedHashMap<>();
			errorDetail.put(Const.RESPONSE_NAME_DETAIL_MESSAGE,
					errorString);

			response.put(Const.RESPONSE_NAME_ERROR_DETAIL,
					new Object[] { errorDetail });
		}

		// ObjectMapperのインスタンスを作成
		ObjectMapper objectMapper = new ObjectMapper();
		String responseString = null;

		// MapをJSON文字列に変換
		try {
			responseString = objectMapper
					.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// ログの出力(レスポンス作成失敗)
			Common.outputLogMessage("error.ChangeStatus.10004",
					Common.getErrorInfoString(e));
			res.setStatusCode(Const.HTTP_500);
			return res;
		}

        // Content-Typeを設定
        Map<String, String> headers = new LinkedHashMap<String, String>();
        headers.put("Content-Type", "application/json");
        res.setHeaders(headers);

		// ボディを設定
		res.setBody(responseString);

		return res;
	}

	/**
	 * リクエストから設備データ取込管理テーブル情報を取得する
	 * 
	 * @param request        リクエスト
	 * @param checkErrorInfo エラー情報
	 * @return 設備データ整備管理テーブル情報
	 * @throws Exception
	 */
	private FacilityDataTaskAdjustmentManagementBean getFacilityDataTaskAdjustmentManagementBean(
			ApplicationLoadBalancerRequestEvent request,
			CheckErrorInfo checkErrorInfo)
			throws Exception {

		JsonNode body = new ObjectMapper().readTree(request.getBody());
		Map<String, String> requestMap = new HashMap<>();

		requestMap.put(Const.ITEM_NAME_ZIP_FILE_NAME.getJapaneseName(),
				getRequestBodyItem(body, Const.ITEM_NAME_ZIP_FILE_NAME.getPhysicalName()));
		requestMap.put(Const.ITEM_NAME_INFRA_COMPANY_ID.getJapaneseName(),
				getRequestBodyItem(body, Const.ITEM_NAME_INFRA_COMPANY_ID.getPhysicalName()));
		requestMap.put(Const.ITEM_NAME_PROCESS_START_DATE.getJapaneseName(),
				getRequestBodyItem(body, Const.ITEM_NAME_PROCESS_START_DATE.getPhysicalName()));
		requestMap.put(Const.ITEM_NAME_PROCESS_END_DATE.getJapaneseName(),
				getRequestBodyItem(body, Const.ITEM_NAME_PROCESS_END_DATE.getPhysicalName()));
		requestMap.put(Const.ITEM_NAME_ADJUST_STATUS.getJapaneseName(),
				getRequestBodyItem(body, Const.ITEM_NAME_ADJUST_STATUS.getPhysicalName()));
		requestMap.put(Const.ITEM_NAME_ADJUST_MESSAGE.getJapaneseName(),
				getRequestBodyItem(body, Const.ITEM_NAME_ADJUST_MESSAGE.getPhysicalName()));

		// 入力値チェック
		for (Map.Entry<String, String> input : requestMap.entrySet()) {
			// 必須チェック
			// Zipファイル名、インフラ事業者ID、処理開始日時、整備状況
			if (input.getKey() == Const.ITEM_NAME_ZIP_FILE_NAME
					.getJapaneseName()
					|| input.getKey() == Const.ITEM_NAME_INFRA_COMPANY_ID
							.getJapaneseName()
					|| input.getKey() == Const.ITEM_NAME_PROCESS_START_DATE
							.getJapaneseName()
					|| input.getKey() == Const.ITEM_NAME_ADJUST_STATUS
							.getJapaneseName()) {
				if (checkExistance(input.getValue())) {
					checkErrorInfo.setErrorString(
							Common.getLogMessage(
									"error.ChangeStatus.10001",
									input.getKey()));
					throw new Exception(
							Common.getLogMessage(
									"error.ChangeStatus.20004",
									input.getKey()));
				}
			}
			// 桁数チェック
			// インフラ事業者ID
			if (input.getKey() == Const.ITEM_NAME_INFRA_COMPANY_ID
					.getJapaneseName()) {
				if (checkDigits(input.getValue(),
						Const.MAX_DIGITS_INFRA_COMPANY_ID)) {
					checkErrorInfo.setErrorString(
							Common.getLogMessage(
									"error.ChangeStatus.10002",
									input.getKey()));
					throw new Exception(Common
							.getLogMessage(
									"error.ChangeStatus.20005",
									input.getKey()));
				}
			}
		}

		String zipFileName = requestMap.get(Const.ITEM_NAME_ZIP_FILE_NAME
				.getJapaneseName());
		String infraCompanyId = requestMap.get(Const.ITEM_NAME_INFRA_COMPANY_ID
				.getJapaneseName());
		Timestamp processStartDate = null;
		Timestamp processEndDate = null;
		String adjustStatus = requestMap.get(Const.ITEM_NAME_ADJUST_STATUS
				.getJapaneseName());
		String adjustMessage = requestMap.get(Const.ITEM_NAME_ADJUST_MESSAGE
				.getJapaneseName());

		// 形式チェック
		try {
			// 処理開始日時
			String processStartDateString = requestMap
					.get(Const.ITEM_NAME_PROCESS_START_DATE.getJapaneseName());
			processStartDate = new Timestamp(
					new SimpleDateFormat(Const.PROCESS_START_DATE_FORMAT)
							.parse(processStartDateString).getTime());
		} catch (Exception e) {
			checkErrorInfo.setErrorString(Common.getLogMessage("error.ChangeStatus.10003",
					Const.ITEM_NAME_PROCESS_START_DATE.getJapaneseName()));
			throw new Exception(Common.getLogMessage("error.ChangeStatus.20006",
					Const.ITEM_NAME_PROCESS_START_DATE.getJapaneseName()));
		}

		try {
			// 処理終了日時
			String processEndDateString = requestMap
					.get(Const.ITEM_NAME_PROCESS_END_DATE.getJapaneseName());
			if (processEndDateString != null) {
				processEndDate = new Timestamp(
						new SimpleDateFormat(Const.PROCESS_END_DATE_FORMAT)
								.parse(processEndDateString).getTime());
			}
		} catch (Exception e) {
			checkErrorInfo.setErrorString(Common.getLogMessage(
					"error.ChangeStatus.10003",
					Const.ITEM_NAME_PROCESS_END_DATE
							.getJapaneseName()));
			throw new Exception(Common.getLogMessage(
					"error.ChangeStatus.20006",
					Const.ITEM_NAME_PROCESS_END_DATE
							.getJapaneseName()));
		}

		return new FacilityDataTaskAdjustmentManagementBean(zipFileName,
				infraCompanyId, processStartDate,
				processEndDate, adjustStatus, adjustMessage);
	}

	/**
	 * リクエストbodyから指定した値を取得する
	 * 
	 * @param body リクエストbody
	 * @param key  項目名
	 * @return 取得した値
	 */
	private String getRequestBodyItem(JsonNode body, String key) {
		try {
			return body.get(key).asText();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 値が空でないことを確認する
	 * 
	 * @param value 確認対象文字列
	 * @return 確認結果
	 */
	private boolean checkExistance(String value) {
		return (value == null || value.isEmpty());
	}

	/**
	 * 既定の桁数を超えていないことを確認する
	 * 
	 * @param value     確認対象文字列
	 * @param maxDigits 最大桁数
	 * @return 確認結果
	 */
	private boolean checkDigits(String value, int maxDigits) {
		return (value.length() > maxDigits);
	}
}