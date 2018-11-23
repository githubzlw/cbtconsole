import com.stripe.model.Charge;
import com.stripe.model.Event;
import com.stripe.net.APIResource;
import net.sf.json.JSONObject;
import org.junit.Test;

/**
 * @author luohao
 * @date 2018/11/16
 */
public class JsonTest {


    @Test
    public void aa(){
        String json="<com.stripe.model.Event@686171275 id=evt_1DAxsEKee2F2IruTIe7Zq18v> JSON: {\n" +
                "  \"id\": \"evt_1DAxsEKee2F2IruTIe7Zq18v\",\n" +
                "  \"object\": \"event\",\n" +
                "  \"account\": null,\n" +
                "  \"api_version\": \"2018-02-28\",\n" +
                "  \"created\": 1537095186,\n" +
                "  \"data\": {\n" +
                "    \"object\": {\n" +
                "      \"id\": \"ch_1DAxsDKee2F2IruTXXBDwhcg\",\n" +
                "      \"object\": \"charge\",\n" +
                "      \"amount\": 38971,\n" +
                "      \"amount_refunded\": 0,\n" +
                "      \"application\": null,\n" +
                "      \"application_fee\": null,\n" +
                "      \"alternate_statement_descriptors\": null,\n" +
                "      \"balance_transaction\": \"txn_1DAxsEKee2F2IruT6GlLAxiZ\",\n" +
                "      \"captured\": true,\n" +
                "      \"created\": 1537095185,\n" +
                "      \"currency\": \"usd\",\n" +
                "      \"customer\": null,\n" +
                "      \"description\": \"import-express.com\",\n" +
                "      \"destination\": null,\n" +
                "      \"dispute\": null,\n" +
                "      \"failure_code\": null,\n" +
                "      \"failure_message\": null,\n" +
                "      \"fraud_details\": {\n" +
                "        \"user_report\": null,\n" +
                "        \"stripe_report\": null\n" +
                "      },\n" +
                "      \"invoice\": null,\n" +
                "      \"livemode\": true,\n" +
                "      \"metadata\": {\n" +
                "        \"order_no\": \"Q916119512775181\"\n" +
                "      },\n" +
                "      \"on_behalf_of\": null,\n" +
                "      \"order\": null,\n" +
                "      \"outcome\": {\n" +
                "        \"network_status\": \"approved_by_network\",\n" +
                "        \"reason\": null,\n" +
                "        \"risk_level\": \"normal\",\n" +
                "        \"rule\": null,\n" +
                "        \"seller_message\": \"Payment complete.\",\n" +
                "        \"type\": \"authorized\"\n" +
                "      },\n" +
                "      \"paid\": true,\n" +
                "      \"receipt_email\": \"Jadebargewellx@outlook.com\",\n" +
                "      \"receipt_number\": \"1570-0533\",\n" +
                "      \"refunded\": false,\n" +
                "      \"refunds\": {\n" +
                "        \"data\": [],\n" +
                "        \"total_count\": 0,\n" +
                "        \"has_more\": false,\n" +
                "        \"request_options\": null,\n" +
                "        \"request_params\": null,\n" +
                "        \"url\": \"/v1/charges/ch_1DAxsDKee2F2IruTXXBDwhcg/refunds\",\n" +
                "        \"count\": null\n" +
                "      },\n" +
                "      \"review\": null,\n" +
                "      \"shipping\": null,\n" +
                "      \"source\": {\n" +
                "        \"address_city\": null,\n" +
                "        \"address_country\": null,\n" +
                "        \"address_line1\": null,\n" +
                "        \"address_line1_check\": null,\n" +
                "        \"address_line2\": null,\n" +
                "        \"address_state\": null,\n" +
                "        \"address_zip\": null,\n" +
                "        \"address_zip_check\": null,\n" +
                "        \"available_payout_methods\": null,\n" +
                "        \"brand\": \"MasterCard\",\n" +
                "        \"country\": \"GB\",\n" +
                "        \"currency\": null,\n" +
                "        \"cvc_check\": \"pass\",\n" +
                "        \"default_for_currency\": null,\n" +
                "        \"dynamic_last4\": null,\n" +
                "        \"exp_month\": 1,\n" +
                "        \"exp_year\": 2021,\n" +
                "        \"fingerprint\": \"wg6j3oMPZIjFW5kI\",\n" +
                "        \"funding\": \"credit\",\n" +
                "        \"last4\": \"5641\",\n" +
                "        \"name\": \"homeinteriors12@mail.com\",\n" +
                "        \"recipient\": null,\n" +
                "        \"status\": null,\n" +
                "        \"three_d_secure\": null,\n" +
                "        \"tokenization_method\": null,\n" +
                "        \"description\": null,\n" +
                "        \"iin\": null,\n" +
                "        \"issuer\": null,\n" +
                "        \"type\": null,\n" +
                "        \"id\": \"card_1DAxsAKee2F2IruTlKuqKyMe\",\n" +
                "        \"object\": \"card\",\n" +
                "        \"account\": null,\n" +
                "        \"customer\": null,\n" +
                "        \"metadata\": {}\n" +
                "      },\n" +
                "      \"source_transfer\": null,\n" +
                "      \"statement_descriptor\": null,\n" +
                "      \"status\": \"succeeded\",\n" +
                "      \"transfer\": null,\n" +
                "      \"transfer_group\": null,\n" +
                "      \"card\": null,\n" +
                "      \"disputed\": null,\n" +
                "      \"statement_description\": null\n" +
                "    },\n" +
                "    \"previous_attributes\": null\n" +
                "  },\n" +
                "  \"livemode\": true,\n" +
                "  \"pending_webhooks\": 1,\n" +
                "  \"request\": {\n" +
                "    \"id\": \"req_6yjiiVzcfB2qRo\",\n" +
                "    \"idempotency_key\": null\n" +
                "  },\n" +
                "  \"type\": \"charge.succeeded\",\n" +
                "  \"user_id\": null\n" +
                "}";
        System.out.println(json);
        String substring = json.substring(json.indexOf("{"));
        Event event = APIResource.GSON.fromJson(substring, Event.class);
        Charge charge=(Charge)event.getData().getObject();
        //System.out.println(charge);

        String country = JSONObject.fromObject(substring).getJSONObject("data").getJSONObject("object").getJSONObject("source").getString("country");
        System.out.println(country);

    }
}
