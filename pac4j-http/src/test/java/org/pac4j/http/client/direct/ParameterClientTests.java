/*
  Copyright 2012 - 2015 pac4j organization

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package org.pac4j.http.client.direct;

import junit.framework.TestCase;
import org.pac4j.core.context.MockWebContext;
import org.pac4j.core.exception.RequiresHttpAction;
import org.pac4j.core.profile.UserProfile;
import org.pac4j.core.util.TestsConstants;
import org.pac4j.core.util.TestsHelper;
import org.pac4j.http.credentials.TokenCredentials;
import org.pac4j.http.credentials.authenticator.TokenAuthenticator;
import org.pac4j.http.credentials.authenticator.test.SimpleTestTokenAuthenticator;
import org.pac4j.http.profile.creator.test.SimpleTestTokenProfileCreator;

/**
 * This class tests the {@link ParameterClient} class.
 *
 * @author Jerome Leleu
 * @since 1.8.0
 */
public final class ParameterClientTests extends TestCase implements TestsConstants {

    private final static String PARAMETER_NAME = "parameterName";
    private final static boolean SUPPORT_GET = true;
    private final static boolean SUPPORT_POST = false;

    public void testClone() {
        final ParameterClient oldClient = new ParameterClient();
        oldClient.setName(TYPE);
        final SimpleTestTokenProfileCreator profileCreator = new SimpleTestTokenProfileCreator();
        oldClient.setProfileCreator(profileCreator);
        final TokenAuthenticator authenticator = new SimpleTestTokenAuthenticator();
        oldClient.setAuthenticator(authenticator);
        oldClient.setParameterName(PARAMETER_NAME);
        oldClient.setSupportGetRequest(SUPPORT_GET);
        oldClient.setSupportPostRequest(SUPPORT_POST);
        final ParameterClient client = (ParameterClient) oldClient.clone();
        assertEquals(oldClient.getName(), client.getName());
        assertEquals(oldClient.getProfileCreator(), client.getProfileCreator());
        assertEquals(oldClient.getAuthenticator(), client.getAuthenticator());
        assertEquals(oldClient.getParameterName(), client.getParameterName());
        assertEquals(oldClient.isSupportGetRequest(), client.isSupportGetRequest());
        assertEquals(oldClient.isSupportPostRequest(), client.isSupportPostRequest());
    }

    public void testMissingTokendAuthenticator() {
        final ParameterClient client = new ParameterClient(null, new SimpleTestTokenProfileCreator());
        TestsHelper.initShouldFail(client, "authenticator cannot be null");
    }

    public void testMissingProfileCreator() {
        final ParameterClient client = new ParameterClient(new SimpleTestTokenAuthenticator(), null);
        TestsHelper.initShouldFail(client, "profileCreator cannot be null");
    }

    public void testMissingParameterName() {
        final ParameterClient client = new ParameterClient(new SimpleTestTokenAuthenticator(), new SimpleTestTokenProfileCreator());
        TestsHelper.initShouldFail(client, "parameterName cannot be blank");
    }

    public void testAuthentication() throws RequiresHttpAction {
        final ParameterClient client = new ParameterClient(new SimpleTestTokenAuthenticator(), new SimpleTestTokenProfileCreator());
        client.setParameterName(PARAMETER_NAME);
        client.setSupportGetRequest(SUPPORT_GET);
        client.setSupportPostRequest(SUPPORT_POST);
        final MockWebContext context = MockWebContext.create();
        context.addRequestParameter(PARAMETER_NAME, VALUE);
        context.setRequestMethod("GET");
        final TokenCredentials credentials = client.getCredentials(context);
        final UserProfile profile = client.getUserProfile(credentials, context);
        assertEquals(VALUE, profile.getId());
    }
}
