package com.brotherlogic.proxycache.discogs;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Request;
import org.scribe.model.RequestTuner;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.scribe.services.Base64Encoder;
import org.scribe.utils.MapUtils;

public class DiscogsOAuthService implements OAuthService
{

   private static final String VERSION = "1.0";

   private final OAuthConfig config;
   private final DefaultApi10a api;

   /**
    * Default constructor
    * 
    * @param api
    *           OAuth1.0a api information
    * @param config
    *           OAuth 1.0a configuration param object
    */
   public DiscogsOAuthService(DefaultApi10a api, OAuthConfig config)
   {
      this.api = api;
      this.config = config;
   }

   @Override
   public Token getAccessToken(Token requestToken, Verifier verifier)
   {
      return getAccessToken(requestToken, verifier, 2, TimeUnit.SECONDS);
   }

   /**
    * {@inheritDoc}
    */
   public Token getAccessToken(Token requestToken, Verifier verifier, int timeout, TimeUnit unit)
   {
      return getAccessToken(requestToken, verifier, new TimeoutTuner(timeout, unit));
   }

   public Token getAccessToken(Token requestToken, Verifier verifier, RequestTuner tuner)
   {
      config.log("obtaining access token from " + api.getAccessTokenEndpoint());
      OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(),
            api.getAccessTokenEndpoint());
      request.addOAuthParameter(OAuthConstants.TOKEN, requestToken.getToken());
      request.addOAuthParameter(OAuthConstants.VERIFIER, verifier.getValue());

      config.log("setting token to: " + requestToken + " and verifier to: " + verifier);
      addOAuthParams(request, requestToken);
      appendSignature(request);
      Response response = request.send(tuner);
      return api.getAccessTokenExtractor().extract(response.getBody());
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getAuthorizationUrl(Token requestToken)
   {
      return api.getAuthorizationUrl(requestToken);
   }

   @Override
   public Token getRequestToken()
   {
      return getRequestToken(2, TimeUnit.SECONDS);
   }

   /**
    * {@inheritDoc}
    */
   public Token getRequestToken(int timeout, TimeUnit unit)
   {
      return getRequestToken(new TimeoutTuner(timeout, unit));
   }

   public Token getRequestToken(RequestTuner tuner)
   {
      config.log("obtaining request token from " + api.getRequestTokenEndpoint());
      OAuthRequest request = new OAuthRequest(api.getRequestTokenVerb(),
            api.getRequestTokenEndpoint());

      config.log("setting oauth_callback to " + config.getCallback());
      request.addOAuthParameter(OAuthConstants.CALLBACK, config.getCallback());
      addOAuthParams(request, OAuthConstants.EMPTY_TOKEN);
      appendSignature(request);

      config.log("sending request...");
      Response response = request.send(tuner);
      String body = response.getBody();

      config.log("response status code: " + response.getCode());
      config.log("response body: " + body);
      return api.getRequestTokenExtractor().extract(body);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getVersion()
   {
      return VERSION;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void signRequest(Token token, OAuthRequest request)
   {
      config.log("signing request: " + request.getCompleteUrl());

      // Do not append the token if empty. This is for two legged OAuth calls.
      if (!token.isEmpty())
         request.addOAuthParameter(OAuthConstants.TOKEN, token.getToken());
      config.log("setting token to: " + token);
      addOAuthParams(request, token);
      appendSignature(request);
   }

   private void addOAuthParams(OAuthRequest request, Token token)
   {
      request.addOAuthParameter(OAuthConstants.TIMESTAMP, api.getTimestampService()
            .getTimestampInSeconds());
      request.addOAuthParameter(OAuthConstants.NONCE, api.getTimestampService().getNonce());
      request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, config.getApiKey());
      request.addOAuthParameter(OAuthConstants.SIGN_METHOD, api.getSignatureService()
            .getSignatureMethod());
      request.addOAuthParameter(OAuthConstants.VERSION, getVersion());
      if (config.hasScope())
         request.addOAuthParameter(OAuthConstants.SCOPE, config.getScope());
      request.addOAuthParameter(OAuthConstants.SIGNATURE, getSignature(request, token));

      config.log("appended additional OAuth parameters: "
            + MapUtils.toString(request.getOauthParameters()));
   }

   private void appendSignature(OAuthRequest request)
   {
      switch (config.getSignatureType())
      {
      case Header:
         config.log("using Http Header signature");

         String oauthHeader = api.getHeaderExtractor().extract(request);
         request.addHeader(OAuthConstants.HEADER, oauthHeader);
         request.addHeader("User-Agent", Discogs.USER_AGENT);
         break;
      case QueryString:
         config.log("using Querystring signature");

         for (Map.Entry<String, String> entry : request.getOauthParameters().entrySet())
            request.addQuerystringParameter(entry.getKey(), entry.getValue());
         break;
      }
   }

   private String getSignature(OAuthRequest request, Token token)
   {
      config.log("generating signature...");
      config.log("using base64 encoder: " + Base64Encoder.type());
      String baseString = api.getBaseStringExtractor().extract(request);
      String signature = api.getSignatureService().getSignature(baseString, config.getApiSecret(),
            token.getSecret());

      config.log("base string is: " + baseString);
      config.log("signature is: " + signature);
      return signature;
   }

   private static class TimeoutTuner extends RequestTuner
   {
      private final int duration;
      private final TimeUnit unit;

      public TimeoutTuner(int duration, TimeUnit unit)
      {
         this.duration = duration;
         this.unit = unit;
      }

      @Override
      public void tune(Request request)
      {
         request.setReadTimeout(duration, unit);
      }
   }

}
