package authProviders;

import java.util.Iterator;
import java.util.List;

import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.Permission;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.SocialAuthConfig;
import org.brickred.socialauth.SocialAuthManager;
import org.brickred.socialauth.util.SocialAuthUtil;

import storeHouse.RequestStoreHouse;
import auxiliar.NextStep;
import constants.KConstants;

public class OpenIdAuthProvider extends AbstractAuthProvider implements AuthProviderInterface {

	private SocialAuthManager socialAuthManager;
	private AuthProvider socialAuthProvider;
	private Profile profile;

	public OpenIdAuthProvider(String authProviderId) {
		super(authProviderId);
	}

	public AuthenticationResult authenticationFirstStep(String callbackURL) throws AuthProviderException {

		AuthenticationResult authResult = new AuthenticationResult();

		if (getAuthProviderId() == null) {
			throw new AuthProviderException("Provider id is not valid");
		}

		if (socialAuthManager == null) {
			// Create an instance of SocialAuthConfgi object
			SocialAuthConfig config = SocialAuthConfig.getDefault();
			// config.setApplicationProperties()
			// load configuration. By default load the configuration from
			// oauth_consumer.properties.
			// You can also pass input stream, properties object or
			// properties file name.
			try {
				config.load();
			} catch (Exception e) {
				e.printStackTrace();
				config = null;
			}

			if (config != null) {
				// Create an instance of SocialAuthManager and set config
				socialAuthManager = new SocialAuthManager();
				try {
					socialAuthManager.setSocialAuthConfig(config);
				} catch (Exception e) {
					e.printStackTrace();
					socialAuthManager = null;
				}
			}
		}

		if (socialAuthManager != null) {
			// get Provider URL to which you should redirect for authentication.
			// id can have values "facebook", "twitter", "yahoo" etc. or the
			// OpenID URL
			socialAuthManager.setPermission(getAuthProviderId(), Permission.AUTHENTICATE_ONLY);
			String nextURL = null;
			try {
				nextURL = socialAuthManager.getAuthenticationUrl(getAuthProviderId(), callbackURL);
			} catch (Exception e) {
				e.printStackTrace();
				nextURL = null;
				authResult.addMessage(e.getMessage());
			}

			if ((nextURL != null) && (!"".equals(nextURL))) {
				authResult.setNextStep(new NextStep(KConstants.NextStep.redirect_to, null, nextURL));
			}
		}

		return authResult;
	}

	public AuthenticationResult authenticationCallback(RequestStoreHouse requestStoreHouse) throws Exception {

		// Re-Initialize in session.
		// If an exception occurs this avoids problems.
		socialAuthProvider = null;
		profile = null;

		if (socialAuthManager == null) {
			throw new Exception("Social Auth Manager is null");
		}

		// call connect method of manager which returns the provider object.
		// Pass request parameter map while calling connect method.
		try {
			socialAuthProvider = socialAuthManager.connect(SocialAuthUtil.getRequestParametersMap(requestStoreHouse.getRequest()));
		} catch (Exception e) {
			socialAuthProvider = null;
			e.printStackTrace();
			throw new Exception("Error connecting social authentication provider.");
		}

		if (socialAuthProvider == null)
			throw new Exception("social auth provider is null");

		// Retrieve the provider id to rebuild the initial query.
		// NO: authManager.getCurrentAuthProvider().getProviderId();
		String providerId = socialAuthProvider.getProviderId();
		if (providerId == null)
			throw new Exception("providerId is null in social auth provider");

		profile = socialAuthProvider.getUserProfile();
		if (profile == null)
			throw new Exception("profile is null in social auth provider");

		// Save new computed results in session.
		requestStoreHouse.getSession().setAuthProvider(this);
		requestStoreHouse.getSession().setAuthProviderId(providerId);

		return null;
	}

	public void deauthenticate() {

		if (socialAuthManager != null) {
			List<String> connectedProvidersIds = socialAuthManager.getConnectedProvidersIds();
			if (connectedProvidersIds != null) {
				Iterator<String> connectedProvidersIdsIterator = connectedProvidersIds.iterator();
				if (connectedProvidersIdsIterator != null) {
					while (connectedProvidersIdsIterator.hasNext()) {
						String id = connectedProvidersIdsIterator.next();
						if (id != null) {
							socialAuthManager.disconnectProvider(id);
						}
					}
				}
			}
		}
	}

	public Profile getUserProfile() {
		return profile;
	}

	public SocialAuthManager getSocialAuthManager() {
		return socialAuthManager;
	}

	public String getLocalUserName(boolean appIsInTestingMode) {
		String localUserName = null;

		if (profile == null) {
			if (appIsInTestingMode) {
				Utils.ifNullThenSetUserNameFrom(localUserName, "Testing User", "localhost.localnet", "testing", "testing");
			}
		} else {
			localUserName = Utils.ifNullThenSetUserNameFrom(localUserName, profile.getEmail(), profile.getProviderId(), "email", "providerId");
			localUserName = Utils.ifNullThenSetUserNameFrom(localUserName, profile.getDisplayName(), profile.getProviderId(), "displayName", "providerId");
			localUserName = Utils.ifNullThenSetUserNameFrom(localUserName, profile.getFullName(), profile.getProviderId(), "fullName", "providerId");
			localUserName = Utils.ifNullThenSetUserNameFrom(localUserName, profile.getFirstName(), profile.getProviderId(), "firstName", "providerId");
			localUserName = Utils.ifNullThenSetUserNameFrom(localUserName, profile.getLastName(), profile.getProviderId(), "lastName", "providerId");
		}

		// you can obtain profile information
		// System.out.println(profile.getFirstName());
		// OR also obtain list of contacts
		// List<Contact> contactsList = provider.getContactList();
		return localUserName;
	}
}
