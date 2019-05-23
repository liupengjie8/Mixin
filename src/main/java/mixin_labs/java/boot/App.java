package main.java.mixin_labs.java.boot;

import main.java.mixin_labs.java.sdk.MixinBot;
import main.java.mixin_labs.java.sdk.MixinUtil;
import main.java.mixin_labs.java.sdk.MIXIN_Category;
import main.java.mixin_labs.java.sdk.MIXIN_Action;

import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
// import java.util.Base64;
import org.apache.commons.codec.binary.Base64;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;


public class App {

    public static void main(String[] args) {
        MixinBot.connectToRemoteMixin(new WebSocketListener() {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
          System.out.println("[onOpen !!!]");
          System.out.println("request header:" + response.request().headers());
          System.out.println("response header:" + response.headers());
          System.out.println("response:" + response);

          // Request unread messages
          MixinBot.sendListPendingMessages(webSocket);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
          System.out.println("[onMessage !!!]");
          System.out.println("text: " + text);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
          try {
            System.out.println("[onMessage !!!]");
            String msgIn = MixinUtil.bytesToJsonStr(bytes);
            System.out.println("json: " + msgIn);
            JsonObject obj = new JsonParser().parse(msgIn).getAsJsonObject();
            MIXIN_Action action = MIXIN_Action.parseFrom(obj);
            System.out.println(action);
            MIXIN_Category category = MIXIN_Category.parseFrom(obj);
            System.out.println(category);
            if (action == MIXIN_Action.CREATE_MESSAGE && obj.get("data") != null &&
                category != null ) {
              String userId;
              String messageId = obj.get("data").getAsJsonObject().get("message_id").getAsString();
              MixinBot.sendMessageAck(webSocket, messageId);
              switch (category) {
                case PLAIN_TEXT:
                    String conversationId =
                      obj.get("data").getAsJsonObject().get("conversation_id").getAsString();
                    userId =
                      obj.get("data").getAsJsonObject().get("user_id").getAsString();
                    byte[] msgData = Base64.decodeBase64(obj.get("data").getAsJsonObject().get("data").getAsString());
                    MixinBot.sendText(webSocket,conversationId,userId,new String(msgData,"UTF-8"));
                    break;
                default:
                    System.out.println("Category: " + category);
              }
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
          System.out.println("[onClosing !!!]");
          System.out.println("code: " + code);
          System.out.println("reason: " + reason);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
          System.out.println("[onClosed !!!]");
          System.out.println("code: " + code);
          System.out.println("reason: " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
          System.out.println("[onFailure !!!]");
          System.out.println("throwable: " + t);
          System.out.println("response: " + response);
        }
      }, Config.RSA_PRIVATE_KEY, Config.CLIENT_ID, Config.SESSION_ID);
    }
}
