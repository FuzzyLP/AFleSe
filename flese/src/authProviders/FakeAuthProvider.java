package authProviders;

import storeHouse.RequestStoreHouse;
import auxiliar.NextStep;
import auxiliar.StringsUtil;
import constants.KConstants;

public class FakeAuthProvider extends AbstractAuthProvider implements AuthProviderInterface {

	protected FakeAuthProvider(String authProviderId) {
		super(authProviderId);
	}

	@Override
	public AuthenticationResult authenticationFirstStep(String callbackURL) throws AuthProviderException {
		callbackURL = removeApplicationRoute(callbackURL);
		AuthenticationResult authResult = new AuthenticationResult();
		NextStep nextStep = new NextStep(KConstants.NextStep.forward_to, null, callbackURL);
		authResult.setNextStep(nextStep);
		return authResult;
	}

	@Override
	public AuthenticationResult authenticationCallback(RequestStoreHouse requestStoreHouse) throws Exception {
		return null;
	}

	@Override
	public void deauthenticate() {
		return;
	}

	@Override
	public String getLocalUserName(boolean appIsInTestingMode) {
		return "unknown@fakeAuthProv.org";
	}

	private String removeApplicationRoute(String callbackURL) {
		if (StringsUtil.isEmptyString(callbackURL))
			return "";
		int position = callbackURL.lastIndexOf("/");
		if (position > 0) {
			callbackURL = callbackURL.substring(position);
		}
		return callbackURL;
	}
}
