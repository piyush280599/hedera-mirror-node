package com.hedera.mirror.web3.evm.exception;

/*-
 * ‌
 * Hedera Mirror Node
 * ​
 * Copyright (C) 2019 - 2022 Hedera Hashgraph, LLC
 * ​
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ‍
 */

import com.hedera.mirror.web3.exception.InvalidTransactionException;
import com.hedera.node.app.service.evm.contracts.execution.HederaEvmTransactionProcessingResult;

import com.hedera.node.app.service.evm.contracts.operations.HederaExceptionalHaltReason;

import com.hedera.node.app.service.evm.store.contracts.utils.BytesKey;

import com.hederahashgraph.api.proto.java.ResponseCodeEnum;
import lombok.experimental.UtilityClass;
import org.hyperledger.besu.evm.frame.ExceptionalHaltReason;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static com.hederahashgraph.api.proto.java.ResponseCodeEnum.CONTRACT_EXECUTION_EXCEPTION;
import static com.hederahashgraph.api.proto.java.ResponseCodeEnum.CONTRACT_REVERT_EXECUTED;
import static com.hederahashgraph.api.proto.java.ResponseCodeEnum.INSUFFICIENT_BALANCES_FOR_STORAGE_RENT;
import static com.hederahashgraph.api.proto.java.ResponseCodeEnum.INVALID_TRANSACTION;
import static com.hederahashgraph.api.proto.java.ResponseCodeEnum.MAX_CHILD_RECORDS_EXCEEDED;
import static com.hederahashgraph.api.proto.java.ResponseCodeEnum.MAX_CONTRACT_STORAGE_EXCEEDED;
import static com.hederahashgraph.api.proto.java.ResponseCodeEnum.MAX_ENTITIES_IN_PRICE_REGIME_HAVE_BEEN_CREATED;
import static com.hederahashgraph.api.proto.java.ResponseCodeEnum.MAX_STORAGE_IN_PRICE_REGIME_HAS_BEEN_USED;
import static com.hederahashgraph.api.proto.java.ResponseCodeEnum.SUCCESS;
import static java.util.stream.Collectors.toMap;

@UtilityClass
public class ResponseCodeUtil {
    static final Map<BytesKey, ResponseCodeEnum> RESOURCE_EXHAUSTION_REVERSIONS =
            Stream.of(
                            MAX_CHILD_RECORDS_EXCEEDED,
                            MAX_CONTRACT_STORAGE_EXCEEDED,
                            MAX_STORAGE_IN_PRICE_REGIME_HAS_BEEN_USED,
                            MAX_ENTITIES_IN_PRICE_REGIME_HAVE_BEEN_CREATED,
                            INSUFFICIENT_BALANCES_FOR_STORAGE_RENT,
                            INVALID_TRANSACTION)
                    .collect(
                            toMap(
                                    status ->
                                            new BytesKey(
                                                    new InvalidTransactionException(status)
                                                            .messageBytes()
                                                            .toArrayUnsafe()),
                                    status -> status));

    public static ResponseCodeEnum getStatusOrDefault(
            final HederaEvmTransactionProcessingResult result) {
        if (result.isSuccessful()) {
            return SUCCESS;
        }
        var maybeHaltReason = result.getHaltReason();
        if (maybeHaltReason.isPresent()) {
            var haltReason = maybeHaltReason.get();
            if (HederaExceptionalHaltReason.SELF_DESTRUCT_TO_SELF == haltReason) {
                return ResponseCodeEnum.OBTAINER_SAME_CONTRACT_ID;
            } else if (HederaExceptionalHaltReason.INVALID_SOLIDITY_ADDRESS == haltReason) {
                return ResponseCodeEnum.INVALID_SOLIDITY_ADDRESS;
            } else if (ExceptionalHaltReason.INSUFFICIENT_GAS == haltReason) {
                return ResponseCodeEnum.INSUFFICIENT_GAS;
            } else if (ExceptionalHaltReason.ILLEGAL_STATE_CHANGE == haltReason) {
                return ResponseCodeEnum.LOCAL_CALL_MODIFICATION_EXCEPTION;
            }
        }

        return result.getRevertReason()
                .map(
                        status ->
                                RESOURCE_EXHAUSTION_REVERSIONS.getOrDefault(
                                        new BytesKey(
                                                result.getRevertReason().get().toArrayUnsafe()),
                                        CONTRACT_REVERT_EXECUTED))
                .orElse(CONTRACT_EXECUTION_EXCEPTION);
    }

    public static HederaEvmTransactionProcessingResult emptyFailProcessingResponse() {
        return HederaEvmTransactionProcessingResult.failed(
                0, 0, 0,
                Optional.empty(), Optional.empty());
    }
}
