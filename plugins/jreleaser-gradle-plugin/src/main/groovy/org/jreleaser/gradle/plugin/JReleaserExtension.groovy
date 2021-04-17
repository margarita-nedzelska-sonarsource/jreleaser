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
package org.jreleaser.gradle.plugin

import groovy.transform.CompileStatic
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.provider.Property
import org.jreleaser.gradle.plugin.dsl.Announce
import org.jreleaser.gradle.plugin.dsl.Artifact
import org.jreleaser.gradle.plugin.dsl.Assemble
import org.jreleaser.gradle.plugin.dsl.Distribution
import org.jreleaser.gradle.plugin.dsl.Environment
import org.jreleaser.gradle.plugin.dsl.Packagers
import org.jreleaser.gradle.plugin.dsl.Project
import org.jreleaser.gradle.plugin.dsl.Release
import org.jreleaser.gradle.plugin.dsl.Signing

/**
 *
 * @author Andres Almiray
 * @since 0.1.0
 */
@CompileStatic
interface JReleaserExtension {
    Property<Boolean> getEnabled()

    Property<Boolean> getDryrun()

    Environment getEnvironment()

    Project getProject()

    Release getRelease()

    Packagers getPackagers()

    Announce getAnnounce()

    Assemble getAssemble()

    Signing getSigning()

    NamedDomainObjectContainer<Distribution> getDistributions()

    void environment(Action<? super Environment> action)

    void project(Action<? super Project> action)

    void file(Action<? super Artifact> action)

    void release(Action<? super Release> action)

    void packagers(Action<? super Packagers> action)

    void announce(Action<? super Announce> action)

    void assemble(Action<? super Assemble> action)

    void signing(Action<? super Signing> action)
}
