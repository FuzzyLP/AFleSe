package authProviders;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.json.JSONObject;

import storeHouse.RequestStoreHouse;
import auxiliar.NextStep;
import constants.KConstants;
import constants.KUrls;

public class OpenAuth_2_0_Provider_google extends AbstractAuthProvider implements AuthProviderInterface {

	// The contents of this auth provider came from
	// https://cwiki.apache.org/confluence/display/OLTU/OAuth+2.0+Client+Quickstart

	String localUserName;

	protected OpenAuth_2_0_Provider_google(String authProviderId) {
		super(authProviderId);
	}

	@Override
	public AuthenticationResult authenticationFirstStep(String callbackURL) throws AuthProviderException {
		OAuthClientRequest oauthRequest;
		try {
			oauthRequest = OAuthClientRequest.authorizationProvider(getOAuthProviderType())
					.setClientId(getClientId()).setRedirectURI(callbackURL)
					.setResponseType(ResponseType.CODE.toString()).setScope(getScope()).buildQueryMessage();
		} catch (OAuthSystemException e) {
			e.printStackTrace();
			oauthRequest = null;
		}

		String nextURL = oauthRequest.getLocationUri();
		AuthenticationResult authResult = new AuthenticationResult();
		NextStep nextStep = new NextStep(KConstants.NextStep.redirect_to, null, nextURL);
		authResult.setNextStep(nextStep);
		return authResult;
	}

	@Override
	public AuthenticationResult authenticationCallback(RequestStoreHouse requestStoreHouse) throws Exception {
		// URL of YOUR application which will be called after authentication
		NextStep nextStep = new NextStep(KConstants.NextStep.redirect_to, KUrls.Auth.SocialAuthCallback, "");
		String callbackURL = nextStep.getUrl(true, true, false, requestStoreHouse.getRequest());

		OAuthAuthzResponse oar = OAuthAuthzResponse.oauthCodeAuthzResponse(requestStoreHouse.getRequest());
		String code = oar.getCode();

		OAuthClientRequest oAuthClientRequest = OAuthClientRequest.tokenProvider(getOAuthProviderType())
				.setGrantType(GrantType.AUTHORIZATION_CODE).setClientId(getClientId())
				.setClientSecret(getClientSecret()).setScope(getScope()).setCode(code)
				.setRedirectURI(callbackURL).buildBodyMessage();

		// create OAuth client that uses custom http client under the hood
		OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

		OAuthAccessTokenResponse accessTokenResponse = oAuthClient.accessToken(oAuthClientRequest);
		String accessToken = accessTokenResponse.getAccessToken();

		final OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(getUserInfoUrl())
				.setAccessToken(accessToken).buildHeaderMessage();

		OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest,
				OAuth.HttpMethod.GET, OAuthResourceResponse.class);
		String responseBody = resourceResponse.getBody();

		JSONObject jsonObject = new JSONObject(responseBody);
		localUserName = jsonObject.getString("email");

		// OAuthJSONAccessTokenResponse tokenResponse = oAuthClient.accessToken(request,
		// OAuthJSONAccessTokenResponse.class);

		// logger.log(Level.WARNING, "token: " + tokenResponse.getAccessToken()
		// + " valid to: " + tokenResponse.getExpiresIn());

		// localUserName = tokenResponse.getBody();

		return null;
	}

	@Override
	public void deauthenticate() {
		return;
	}

	@Override
	public String getLocalUserName(boolean appIsInTestingMode) {
		return localUserName;
	}

	private OAuthProviderType getOAuthProviderType() {
		String authProviderId = getAuthProviderId();
		OAuthProviderType result = null;

		switch (authProviderId) {
		case KCtes.Providers.google:
			result = OAuthProviderType.GOOGLE;
			break;

		case KCtes.Providers.facebook:
			result = OAuthProviderType.FACEBOOK;
		}
		return result;
	}

	private String getClientId() {
		String authProviderId = getAuthProviderId();
		String clientId = "your-provider-application-client-id";

		switch (authProviderId) {
		case KCtes.Providers.google:
			clientId = "617924078403.apps.googleusercontent.com";
			break;
		}
		return clientId;
	}

	private String getClientSecret() {
		String authProviderId = getAuthProviderId();
		String clientSecret = "your-provider-application-client-secret";

		switch (authProviderId) {
		case KCtes.Providers.google:
			clientSecret = "IETmYn-5Uh4ZXgxA6rZ463R3";
			break;
		}
		return clientSecret;
	}

	private String getScope() {
		String authProviderId = getAuthProviderId();
		String providerScope = "your-provider-scope";

		switch (authProviderId) {
		case KCtes.Providers.google:
			providerScope = "https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email";
			break;
		}
		return providerScope;
	}

	private String getUserInfoUrl() {
		String authProviderId = getAuthProviderId();
		String userInfoUrl = "your-provider-scope";

		switch (authProviderId) {
		case KCtes.Providers.google:
			userInfoUrl = "https://www.googleapis.com/oauth2/v1/userinfo";
			break;
		}
		return userInfoUrl;
	}
}
