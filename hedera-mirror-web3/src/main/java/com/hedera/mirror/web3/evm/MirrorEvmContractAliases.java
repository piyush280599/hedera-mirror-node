package com.hedera.mirror.web3.evm;

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

import com.hedera.mirror.common.domain.entity.Entity;
import com.hedera.mirror.web3.evm.store.contract.MirrorEntityAccess;
import com.hedera.services.evm.accounts.HederaEvmContractAliases;

import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;

public class MirrorEvmContractAliases extends HederaEvmContractAliases {

    private final MirrorEntityAccess mirrorEntityAccess;

    public MirrorEvmContractAliases(MirrorEntityAccess mirrorEntityAccess) {
        this.mirrorEntityAccess = mirrorEntityAccess;
    }

    @Override
    public Address resolveForEvm(Address addressOrAlias) {
        final var entity = mirrorEntityAccess.findEntity(addressOrAlias);
        return Address.wrap(Bytes.wrap(entity.map(Entity::getEvmAddress).orElse(new byte[0])));
    }
}
