package authProviders;

import storeHouse.RequestStoreHouse;
import auxiliar.StringsUtil;

public abstract class AbstractAuthProvider implements AuthProviderInterface {

	private String authProviderId;

	protected AbstractAuthProvider(String authProviderId) {
		this.authProviderId = authProviderId;
	}

	public static AuthProviderInterface getInstance(String authProviderId) {
		if (StringsUtil.isEmptyString(authProviderId))
			return null;
		if (KCtes.Providers.fakeAuthProvider.equals(authProviderId))
			return new FakeAuthProvider(authProviderId);
		if (KCtes.Providers.google.equals(authProviderId))
			return new OpenAuth_2_0_Provider_google(authProviderId);
		if (KCtes.Providers.facebook.equals(authProviderId))
			return new OpenIdAuthProvider(authProviderId);
		if (KCtes.Providers.twitter.equals(authProviderId))
			return new OpenIdAuthProvider(authProviderId);
		if (KCtes.Providers.yahoo.equals(authProviderId))
			return new OpenIdAuthProvider(authProviderId);
		if (KCtes.Providers.hotmail.equals(authProviderId))
			return new OpenIdAuthProvider(authProviderId);
		if (KCtes.Providers.linkedin.equals(authProviderId))
			return new OpenIdAuthProvider(authProviderId);
		if (KCtes.Providers.foursquare.equals(authProviderId))
			return new OpenIdAuthProvider(authProviderId);
		if (KCtes.Providers.myspace.equals(authProviderId))
			return new OpenIdAuthProvider(authProviderId);
		if (KCtes.Providers.mendeley.equals(authProviderId))
			return new OpenIdAuthProvider(authProviderId);

		return null;
	}

	protected String getAuthProviderId() {
		return this.authProviderId;
	}

	public abstract AuthenticationResult authenticationFirstStep(String callbackURL) throws AuthProviderException;

	public abstract AuthenticationResult authenticationCallback(RequestStoreHouse requestStoreHouse) throws Exception;

	public abstract void deauthenticate();

	public abstract String getLocalUserName(boolean appIsInTestingMode);

}
