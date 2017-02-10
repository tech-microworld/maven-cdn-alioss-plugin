/**
 * Copyright (C) 2008 http://code.google.com/p/maven-license-plugin/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fengjx.maven.cdn;

import com.fengjx.maven.cdn.common.Selection;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Arrays.deepToString;

/**
 * <b>Date:</b> 18-Feb-2008<br>
 * <b>Author:</b> Mathieu Carbou (mathieu.carbou@gmail.com) Modified by the Neo4j team.
 */
public abstract class MyAbstractMojo extends AbstractMojo {

    @Parameter
    protected String[] includes = new String[0];

    @Parameter
    protected String[] excludes = new String[0];

    @Parameter
    protected String encoding = System.getProperty("file.encoding");

    @Parameter
    protected boolean quiet = false;

    @Parameter
    protected File baseDir;

    protected final String[] listSelectedFiles() throws MojoFailureException {
        Selection selection = new Selection(getBaseDir(), includes, buildExcludes(), true);
        debug("From: %s", getBaseDir());
        debug("Including: %s", deepToString(selection.getIncluded()));
        debug("Excluding: %s", deepToString(selection.getExcluded()));
        return selection.getSelectedFiles();
    }

    @SuppressWarnings({ "unchecked" })
    private String[] buildExcludes() {
        List<String> ex = new ArrayList<String>();
        ex.addAll(asList(this.excludes));
        MavenProject project = getProject();
        if (project != null && project.getModules() != null) {
            for (String module : (List<String>) project.getModules()) {
                ex.add(module + "/**");
            }
        }
        return ex.toArray(new String[ex.size()]);
    }

    protected final void info(String format, Object... params) {
        if (!quiet) {
            getLog().info(format(format, params));
        }
    }

    protected final void debug(String format, Object... params) {
        if (!quiet) {
            getLog().debug(format(format, params));
        }
    }

    protected final void warn(String format, Object... params) {
        getLog().warn(format(format, params));
    }

    protected final String getBasePath() {
        return getBaseDir().getPath().replace("\\", "/");
    }

    protected final File getBaseDir() {
        if (baseDir == null) {
            return getProject().getBasedir();
        }
        return baseDir;
    }

    protected final MavenProject getProject() {
        return (MavenProject) getPluginContext().get("project");
    }

}
