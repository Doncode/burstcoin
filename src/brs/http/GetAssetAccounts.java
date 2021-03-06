package brs.http;

import brs.Account;
import brs.Asset;
import brs.BurstException;
import brs.db.BurstIterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;

import javax.servlet.http.HttpServletRequest;

public final class GetAssetAccounts extends APIServlet.APIRequestHandler {

  static final GetAssetAccounts instance = new GetAssetAccounts();

  private GetAssetAccounts() {
    super(new APITag[] {APITag.AE}, "asset", "height", "firstIndex", "lastIndex");
  }

  @Override
  JSONStreamAware processRequest(HttpServletRequest req) throws BurstException {

    Asset asset = ParameterParser.getAsset(req);
    int firstIndex = ParameterParser.getFirstIndex(req);
    int lastIndex = ParameterParser.getLastIndex(req);
    int height = ParameterParser.getHeight(req);

    JSONArray accountAssets = new JSONArray();
    try (BurstIterator<Account.AccountAsset> iterator = asset.getAccounts(height, firstIndex, lastIndex)) {
      while (iterator.hasNext()) {
        Account.AccountAsset accountAsset = iterator.next();
        accountAssets.add(JSONData.accountAsset(accountAsset));
      }
    }

    JSONObject response = new JSONObject();
    response.put("accountAssets", accountAssets);
    return response;

  }

}
