/*
 * Copyright 2020 Fraunhofer Institute for Software and Systems Engineering
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.dataspaceconnector.controller.message.ids.validator;

import io.dataspaceconnector.common.exception.MessageResponseException;
import io.dataspaceconnector.controller.message.ids.validator.base.IdsResponseMessageValidator;
import io.dataspaceconnector.service.message.builder.type.SubscriptionRequestService;
import io.dataspaceconnector.service.message.handler.dto.Response;
import io.dataspaceconnector.service.message.handler.exception.InvalidResponseException;
import io.dataspaceconnector.service.message.handler.util.ProcessorUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Validates the response to a RequestMessage sent to create a subscription.
 */
@Component("SubscriptionResponseValidator")
@RequiredArgsConstructor
public class SubscriptionResponseValidator extends IdsResponseMessageValidator {

    /**
     * Service for message handling.
     */
    private final @NonNull SubscriptionRequestService subscriptionReqSvc;

    /**
     * Validates the response to a RequestMessage sent to create a subscription.
     *
     * @param response the response DTO.
     * @throws MessageResponseException if the received response is not valid.
     */
    @Override
    protected void processInternal(final Response response) throws MessageResponseException {
        final var map = ProcessorUtils.getResponseMap(response);

        if (!subscriptionReqSvc.validateResponse(map)) {
            // If the response is not a description response message, show the response.
            final var content = subscriptionReqSvc.getResponseContent(map);
            throw new InvalidResponseException(content, ERROR_MESSAGE);
        }
    }

}
