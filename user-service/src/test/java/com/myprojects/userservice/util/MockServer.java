package com.myprojects.userservice.util;

import static java.util.Arrays.stream;

import lombok.experimental.Delegate;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.Times;
import org.mockserver.mock.Expectation;
import org.mockserver.verify.VerificationTimes;

public class MockServer {
    @Delegate
    private final ClientAndServer server;

    public MockServer(int port) {
        this.server = new ClientAndServer(port);
    }

    public void verifyAllExpectedRequests() {
        Expectation[] allExpectations = server.retrieveActiveExpectations(null);
        stream(allExpectations)
                .forEach(expectation -> server.verify(expectation.getHttpRequest(), verificationTimes(expectation.getTimes())));
    }

    private VerificationTimes verificationTimes(Times times) {
        return times.isUnlimited() ? VerificationTimes.between(0, Integer.MAX_VALUE) : VerificationTimes.exactly(times.getRemainingTimes());
    }
}
