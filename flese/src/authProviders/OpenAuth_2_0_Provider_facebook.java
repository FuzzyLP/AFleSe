package authProviders;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.GitHubTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;

import storeHouse.RequestStoreHouse;
import auxiliar.NextStep;
import constants.KConstants;
import constants.KUrls;

public class OpenAuth_2_0_Provider_facebook extends AbstractAuthProvider implements AuthProviderInterface {

	// The contents of this auth provider came from
	// https://cwiki.apache.org/confluence/display/OLTU/OAuth+2.0+Client+Quickstart

	String localUserName;

	protected OpenAuth_2_0_Provider_facebook(String authProviderId) {
		super(authProviderId);
	}

	@Override
	public AuthenticationResult authenticationFirstStep(String callbackURL) throws AuthProviderException {
		OAuthClientRequest oauthRequest;
		try {
			oauthRequest = OAuthClientRequest.authorizationProvider(getOAuthProviderType()).setClientId(getClientId())
					.setRedirectURI(callbackURL).setResponseType(ResponseType.CODE.toString())
					.setScope(getScope())
					.buildQueryMessage();
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

		OAuthClientRequest request = OAuthClientRequest.tokenProvider(getOAuthProviderType())
				.setGrantType(GrantType.AUTHORIZATION_CODE).setClientId(getClientId()).setClientSecret(getClientSecret())
				.setScope(getScope()).setCode(code).setRedirectURI(callbackURL).buildQueryMessage();

		// create OAuth client that uses custom http client under the hood
		OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

		// Facebook is not fully compatible with OAuth 2.0 draft 10, access
		// token response is
		// application/x-www-form-urlencoded, not json encoded so we use
		// dedicated response class for that
		// Custom response classes are an easy way to deal with oauth providers
		// that introduce modifications to
		// OAuth 2.0 specification
		GitHubTokenResponse oAuthResponse = oAuthClient.accessToken(request, GitHubTokenResponse.class);

		@SuppressWarnings("unused")
		String accessToken = oAuthResponse.getAccessToken();
		@SuppressWarnings("unused")
		Long expiresIn = oAuthResponse.getExpiresIn();
		// oAuthResponse.getParam(param)

		localUserName = oAuthResponse.getBody();

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
}
