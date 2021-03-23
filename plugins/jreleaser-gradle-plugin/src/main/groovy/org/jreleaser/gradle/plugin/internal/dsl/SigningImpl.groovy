/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020-2021 Andres Almiray.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jreleaser.gradle.plugin.internal.dsl

import groovy.transform.CompileStatic
import org.gradle.api.internal.provider.Providers
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal
import org.jreleaser.gradle.plugin.dsl.Signing

import javax.inject.Inject

/**
 *
 * @author Andres Almiray
 * @since 0.1.0
 */
@CompileStatic
class SigningImpl implements Signing {
    final Property<Boolean> enabled
    final Property<Boolean> armored
    final Property<String> passphrase
    final Property<String> publicKey
    final Property<String> secretKey

    @Inject
    SigningImpl(ObjectFactory objects) {
        enabled = objects.property(Boolean).convention(Providers.notDefined())
        armored = objects.property(Boolean).convention(Providers.notDefined())
        passphrase = objects.property(String).convention(Providers.notDefined())
        publicKey = objects.property(String).convention(Providers.notDefined())
        secretKey = objects.property(String).convention(Providers.notDefined())
    }

    @Internal
    boolean isSet() {
        return enabled.present ||
            armored.present ||
            passphrase.present ||
            publicKey.present ||
            secretKey.present
    }

    org.jreleaser.model.Signing toModel() {
        org.jreleaser.model.Signing sign = new org.jreleaser.model.Signing()
        if (enabled.present) sign.enabled = enabled.get()
        if (armored.present) sign.armored = armored.get()
        if (passphrase.present) sign.passphrase = passphrase.get()
        if (publicKey.present) sign.publicKey = publicKey.get()
        if (secretKey.present) sign.secretKey = secretKey.get()
        sign
    }
}
