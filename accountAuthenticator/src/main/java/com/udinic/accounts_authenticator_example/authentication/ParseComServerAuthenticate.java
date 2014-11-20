package com.udinic.accounts_authenticator_example.authentication;

import android.util.Log;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Query;


/**
 * Handles the comminication with Parse.com
 *
 * User: udinic
 * Date: 3/27/13
 * Time: 3:30 AM
 */
public class ParseComServerAuthenticate implements ServerAuthenticate{

    public static final String URL_BASE = "https://api.parse.com";
    public static final String URL_USERS_FRAG =  "/1/users";
    public static final String URL_AUTH_FRAG = "/1/login";
    public static final String URL_AUTH_SESSION = "/1/users/me";

    public static final String PARAM_USERNAME = "username";
    public static final String PARAM_PASSWORD = "password";

    @Override
    public String userSignUp(String name, String email, String pass, String authType) throws Exception {
        //用户注册代码在这里

        Log.d("udinic", "userSignUp");
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("X-Parse-Application-Id", "zpDBLWZb3QXcxHh9LnGeRGtZVRUva3RFpO0NOYEp");
                request.addHeader("X-Parse-REST-API-Key","anK8StLPPdJLOzzy3dKPwLAqx6xq4NIQFPvxXUbB");
                request.addHeader("Content-Type", "application/json");
            }
        };

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(URL_BASE)
                .setRequestInterceptor(requestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        userInterface user = restAdapter.create(userInterface.class);
        //String userbody = "{\"username\":\"" + email + "\",\"password\":\"" + pass + "\",\"phone\":\"415-392-0202\"}";

//        String userbody = "{\"username\":\"cooldude6\",\"password\":\"p_n7!-e8\",\"phone\":\"415-392-0202\"}";
        User signuser = new User();
        signuser.setUsername(name);
        signuser.email = email;
        signuser.password = "123456";
        signuser.phone = "6041234567";

        User nownuser = user.createUserbody(signuser);


        return nownuser.sessionToken;

//        String url = "https://api.parse.com/1/users";
//
//
//        DefaultHttpClient httpClient = new DefaultHttpClient();
//        HttpPost httpPost = new HttpPost(url);
//
////        httpPost.addHeader("X-Parse-Application-Id","XUafJTkPikD5XN5HxciweVuSe12gDgk2tzMltOhr");
//        httpPost.addHeader("X-Parse-Application-Id","zpDBLWZb3QXcxHh9LnGeRGtZVRUva3RFpO0NOYEp");
//
////        httpPost.addHeader("X-Parse-REST-API-Key", "8L9yTQ3M86O4iiucwWb4JS7HkxoSKo7ssJqGChWx");
//        httpPost.addHeader("X-Parse-REST-API-Key", "anK8StLPPdJLOzzy3dKPwLAqx6xq4NIQFPvxXUbB");
//
//        httpPost.addHeader("Content-Type", "application/json");
//
//        String user = "{\"username\":\"" + email + "\",\"password\":\"" + pass + "\",\"phone\":\"415-392-0202\"}";
//        HttpEntity entity = new StringEntity(user);
//        httpPost.setEntity(entity);
//
//        String authtoken = null;
//        try {
//            HttpResponse response = httpClient.execute(httpPost);
//            String responseString = EntityUtils.toString(response.getEntity());
//
//            if (response.getStatusLine().getStatusCode() != 201) {
//                ParseComError error = new Gson().fromJson(responseString, ParseComError.class);
//                throw new Exception("Error creating user["+error.code+"] - " + error.error);
//            }
//
//
//            User createdUser = new Gson().fromJson(responseString, User.class);
//
//            authtoken = createdUser.sessionToken;
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return authtoken;
    }

    @Override
    public String userSignIn(String user, String pass, String authType) throws Exception {
        //用户登录在这里

        return useRetrofit(user,pass);


//        Log.d("udini", "userSignIn");
//
//        DefaultHttpClient httpClient = new DefaultHttpClient();
//        String url = "https://api.parse.com/1/login";
//
//
//        String query = null;
//        try {
//            query = String.format("%s=%s&%s=%s", "username", URLEncoder.encode(user, "UTF-8"), "password", pass);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        url += "?" + query;
//
//        HttpGet httpGet = new HttpGet(url);
//
////        httpGet.addHeader("X-Parse-Application-Id", "XUafJTkPikD5XN5HxciweVuSe12gDgk2tzMltOhr");
//        httpGet.addHeader("X-Parse-Application-Id", "zpDBLWZb3QXcxHh9LnGeRGtZVRUva3RFpO0NOYEp");
//        httpGet.addHeader("X-Parse-REST-API-Key", "anK8StLPPdJLOzzy3dKPwLAqx6xq4NIQFPvxXUbB");
////        httpGet.addHeader("X-Parse-REST-API-Key", "8L9yTQ3M86O4iiucwWb4JS7HkxoSKo7ssJqGChWx");
//
//        HttpParams params = new BasicHttpParams();
//        params.setParameter("username", user);
//        params.setParameter("password", pass);
//        httpGet.setParams(params);
////        httpGet.getParams().setParameter("username", user).setParameter("password", pass);
//
//        String authtoken = null;
//        try {
//            HttpResponse response = httpClient.execute(httpGet);
//
//            String responseString = EntityUtils.toString(response.getEntity());
//            if (response.getStatusLine().getStatusCode() != 200) {
//                ParseComError error = new Gson().fromJson(responseString, ParseComError.class);
//                throw new Exception("Error signing-in ["+error.code+"] - " + error.error);
//            }
//
//            User loggedUser = new Gson().fromJson(responseString, User.class);
//            authtoken = loggedUser.sessionToken;
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return authtoken;
    }

    private String useRetrofit(String username, String pass) {

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("X-Parse-Application-Id", "zpDBLWZb3QXcxHh9LnGeRGtZVRUva3RFpO0NOYEp");
                request.addHeader("X-Parse-REST-API-Key","anK8StLPPdJLOzzy3dKPwLAqx6xq4NIQFPvxXUbB");
                //request.addHeader("Content-Type", "application/json");
            }
        };

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(URL_BASE)
                .setRequestInterceptor(requestInterceptor)
                .build();

        userInterface user = restAdapter.create(userInterface.class);
        User nownuser = user.authenticate(username,pass);
        Log.d("udinic", "userSignIn");
        return nownuser.getSessionToken();
    }


    private class ParseComError implements Serializable {
        int code;
        String error;
    }

    public interface userInterface {
        /**
         * The {@link retrofit.http.Query} values will be transform into query string paramters
         * via Retrofit
         *
         * @param email The users email
         * @param password The users password
         * @return A login response.
         */
        @GET(URL_AUTH_FRAG)
        User authenticate(@Query(PARAM_USERNAME) String email,
                          @Query(PARAM_PASSWORD) String password);


//        @FormUrlEncoded
//        @POST(URL_USERS_FRAG)
//        User createUser(@Field("username") String username,@Field("password") String password,
//                        @Field("phone") String phone);

        @POST(URL_USERS_FRAG)
        User createUserbody(@Body User newuser);

        @GET(URL_AUTH_SESSION)
        User getuserinfofromsession(@Header("X-Parse-Session-Token") String sessiontoken);

    }

    public class User implements Serializable {

        private String email;
        private String lastName;
        private String username;
        private String password;
        private String phone;
        private String objectId;
        public String sessionToken;
        private String gravatarId;
        private String avatarUrl;

        public String getEmail() {
            return email;
        }
//        public String getFirstName() {
//            return firstName;
//        }

//        public void setFirstName(String firstName) {
//            this.firstName = firstName;
//        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String getSessionToken() {
            return sessionToken;
        }

        public void setSessionToken(String sessionToken) {
            this.sessionToken = sessionToken;
        }

        public String getGravatarId() {
            return gravatarId;
        }

        public void setGravatarId(String gravatarId) {
            this.gravatarId = gravatarId;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }
    }
}
