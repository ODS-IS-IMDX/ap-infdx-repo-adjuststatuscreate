// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app;

import java.util.HashMap;
import java.util.Map;

/**
 * ログメッセージクラス
 * ログIDとログメッセージの組を定義する
 * 
 * @author Ishii Yuki
 * @version 1.0
 */
public class LogMessage {
	private static Map<String, String> logMessageMap = new HashMap<String, String>() {
		{
			put("info.ChangeStatus.00001",
					"{1}を開始しました。 メッセージID：[{0}]");
			put("info.ChangeStatus.00002",
					"{1}が正常終了しました。 メッセージID：[{0}]");
			put("info.ChangeStatus.00006",
					"{1}でレコード更新に成功しました。SQL：[{2}], メッセージID：[{0}]");
			put("error.ChangeStatus.00001",
					"システムエラーが発生したため、{1}が異常終了しました。エラー情報：[{2}], メッセージID：[{0}]");
			put("error.ChangeStatus.00002",
					"リクエスト項目エラーが発生したため、{1}が異常終了しました。エラー情報：[{2}], メッセージID：[{0}]");
			put("error.ChangeStatus.00004",
					"{1}でレコード更新に失敗しました。SQL：[{2}] , エラー情報：[{3}], メッセージID：[{0}]");
			put("error.ChangeStatus.00009",
					"プロパティファイルの読込に失敗しました。エラー情報：[{1}], メッセージID：[{0}]");
			put("error.ChangeStatus.00010",
					"SecretManagerの読込に失敗しました。エラー情報：[{1}], メッセージID：[{0}]");
			put("error.ChangeStatus.10001",
					"{1}は必須です。 メッセージID：[{0}]");
			put("error.ChangeStatus.10002",
					"{1}の桁数が不正です。 メッセージID：[{0}]");
			put("error.ChangeStatus.10003",
					"{1}の形式が不正です。 メッセージID：[{0}]");
			put("error.ChangeStatus.10004",
					"レスポンスの作成に失敗しました。 エラー情報：[{1}], メッセージID：[{0}]");
			put("error.ChangeStatus.20001",
					"システムエラーが発生しました。メッセージID：[{0}]");
			put("error.ChangeStatus.20003",
					"キー重複によりレコード作成に失敗しました。メッセージID：[{0}]");
			put("error.ChangeStatus.20004",
					"{1}で必須チェックエラーが発生しました。メッセージID：[{0}]");
			put("error.ChangeStatus.20005",
					"{1}で桁数チェックエラーが発生しました。メッセージID：[{0}]");
			put("error.ChangeStatus.20006",
					"{1}で形式チェックエラーが発生しました。メッセージID：[{0}]");
		}
	};

	public static Map<String, String> getLogMessageMap() {

		return logMessageMap;
	}
}