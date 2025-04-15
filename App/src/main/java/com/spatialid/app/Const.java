// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app;

import com.spatialid.app.model.RequestItemInfo;

/**
 * 定数定義クラス
 * プロジェクト内で利用する定数を定義する
 * 
 * @author Ishii Yuki
 * @version 1.0
 */
public class Const {
    // プロパティファイル情報
    // プロパティファイルパス
    public static final String PROPERTY_FILE_PATH = "src/main/resources/application.properties";
    // プロパティ名(SecretManager接続情報(シークレットID))
    public static final String PROPERTY_NAME_SECLET_MANAGER_NAME = "secretManagerName";
    // プロパティ名(SecretManager接続情報(AWSリージョン))
    public static final String PROPERTY_NAME_SECRET_MANAGER_REGION = "secretManagerRegion";

    // リクエスト形式(論理名, 物理名)
    public static final RequestItemInfo ITEM_NAME_ZIP_FILE_NAME = new RequestItemInfo("Zipファイル名", "zipFileName");
    public static final RequestItemInfo ITEM_NAME_INFRA_COMPANY_ID = new RequestItemInfo("インフラ事業者ID", "infraCompanyId");
    public static final RequestItemInfo ITEM_NAME_PROCESS_START_DATE = new RequestItemInfo("処理開始日時",
            "processStartDate");
    public static final RequestItemInfo ITEM_NAME_PROCESS_END_DATE = new RequestItemInfo("処理終了日時", "processEndDate");
    public static final RequestItemInfo ITEM_NAME_ADJUST_STATUS = new RequestItemInfo("整備状況", "adjustStatus");
    public static final RequestItemInfo ITEM_NAME_ADJUST_MESSAGE = new RequestItemInfo("整備メッセージ", "adjustMessage");
    // 処理開始日時のフォーマット
    public static final String PROCESS_START_DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
    // 処理終了日時のフォーマット
    public static final String PROCESS_END_DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";

    // リクエスト値チェック情報
    // 最大桁数(インフラ事業者ID)
    public static final int MAX_DIGITS_INFRA_COMPANY_ID = 20;

    // レスポンス
    // 項目名(ステータス)
    public static final String RESPONSE_NAME_HTTPSTATUS = "status";
    // 項目名(エラー内容詳細)
    public static final String RESPONSE_NAME_ERROR_DETAIL = "errorDetail";
    // 項目名(詳細メッセージ)
    public static final String RESPONSE_NAME_DETAIL_MESSAGE = "detailMessage";
    // HTTPステータスコード
    public static final int HTTP_200 = 200;
    public static final int HTTP_409 = 409;
    public static final int HTTP_422 = 422;
    public static final int HTTP_500 = 500;

    // SecretManager情報
    // YugabyteDBのホスト名
    public static final String SECRET_KEY_YDB_HOST = "YDB-HOST";
    // YugabyteDBのポート
    public static final String SECRET_KEY_YDB_PORT = "YDB-PORT";
    // YugabyteDBのDB名
    public static final String SECRET_KEY_YDB_NAME = "YDB-NAME";
    // YugabyteDBのユーザ名
    public static final String SECRET_KEY_YDB_USER = "YDB-B1-USER";
    // YugabyteDBのユーザパスワード
    public static final String SECRET_KEY_YDB_PASS = "YDB-B1-USER-PASSWORD";
    // YugabyteDB接続情報のフォーマット
    public static final String YDB_URL_FORMAT = "jdbc:postgresql://{0}:{1}/{2}";

    // ログメッセージ
    // 関数名称
    public static final String FUNCTION_NAME = "B2設備データ整備ステータス作成";
}