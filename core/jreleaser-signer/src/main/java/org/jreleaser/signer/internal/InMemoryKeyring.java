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
package org.jreleaser.signer.internal;

import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.operator.KeyFingerPrintCalculator;
import org.bouncycastle.openpgp.operator.bc.BcKeyFingerprintCalculator;
import org.jreleaser.signer.SigningException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;

/**
 * Adapted from {@code name.neuhalfen.projects.crypto.bouncycastle.openpgp.keys.keyrings.InMemoryKeyring}
 * Original author: Jens Neuhalfen
 */
public final class InMemoryKeyring {
    private final KeyFingerPrintCalculator keyFingerPrintCalculator = new BcKeyFingerprintCalculator();
    private PGPPublicKeyRingCollection publicKeyRings;
    private PGPSecretKeyRingCollection secretKeyRings;

    public InMemoryKeyring() throws IOException, PGPException {
        this.publicKeyRings = new PGPPublicKeyRingCollection(Collections.emptyList());
        this.secretKeyRings = new PGPSecretKeyRingCollection(Collections.emptyList());
    }

    public KeyFingerPrintCalculator getKeyFingerPrintCalculator() {
        return keyFingerPrintCalculator;
    }

    public void addPublicKey(byte[] encodedPublicKey) throws IOException, PGPException {
        try (
            InputStream raw = new ByteArrayInputStream(encodedPublicKey);
            InputStream decoded = org.bouncycastle.openpgp.PGPUtil.getDecoderStream(raw)) {
            addPublicKeyRing(new PGPPublicKeyRing(decoded, keyFingerPrintCalculator));
        }
    }

    public void addSecretKey(byte[] encodedPrivateKey) throws IOException, PGPException {
        try (
            InputStream raw = new ByteArrayInputStream(encodedPrivateKey);
            InputStream decoded = org.bouncycastle.openpgp.PGPUtil
                .getDecoderStream(raw)) {
            addSecretKeyRing(new PGPSecretKeyRing(decoded, keyFingerPrintCalculator));
        }
    }

    public void addSecretKeyRing(PGPSecretKeyRing keyring) {
        this.secretKeyRings = PGPSecretKeyRingCollection.addSecretKeyRing(this.secretKeyRings, keyring);
    }

    public void addPublicKeyRing(PGPPublicKeyRing keyring) {
        this.publicKeyRings = PGPPublicKeyRingCollection.addPublicKeyRing(this.publicKeyRings, keyring);
    }

    public PGPSecretKey getSecretKey() throws SigningException, PGPException {
        return secretKeyRings.getSecretKey(readPublicKey().getKeyID());
    }

    public PGPPublicKey readPublicKey() throws SigningException {
        Iterator<PGPPublicKeyRing> keyRingIter = publicKeyRings.getKeyRings();
        while (keyRingIter.hasNext()) {
            PGPPublicKeyRing keyRing = keyRingIter.next();

            Iterator<PGPPublicKey> keyIter = keyRing.getPublicKeys();
            while (keyIter.hasNext()) {
                PGPPublicKey key = keyIter.next();
                if (key.isEncryptionKey()) {
                    return key;
                }
            }
        }

        throw new SigningException("Did not find public key for signing.");
    }
}
