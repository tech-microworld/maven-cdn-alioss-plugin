/*
 * Copyright (c) 2016 - 2017, fengjx.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fengjx.maven.cdn;

import com.fengjx.maven.cdn.common.AliyunOssUtills;
import com.fengjx.maven.cdn.common.StrUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

/**
 * <b>Date:</b> 2017-02-09<br>
 * <b>Author:</b> FengJianxin fengjianxin2012@gmail.com
 */
@Mojo(name = "upload")
public final class OssMojo extends MyAbstractMojo {

    @Parameter
    private String endpoint;
    @Parameter
    private String accessKeyId;
    @Parameter
    private String accessKeySecret;
    @Parameter
    private String bucketName;
    @Parameter
    private boolean supportCname = true;
    @Parameter
    private String prefix;

    public void execute() throws MojoExecutionException, MojoFailureException {
        if (!StrUtils.notBlank(endpoint)) {
            throw new MojoExecutionException("endpoint can not be null");
        }

        if (!StrUtils.notBlank(accessKeyId)) {
            throw new MojoExecutionException("accessKeyId can not be null");
        }

        if (!StrUtils.notBlank(accessKeySecret)) {
            throw new MojoExecutionException("accessKeySecret can not be null");
        }

        if (!StrUtils.notBlank(bucketName)) {
            throw new MojoExecutionException("bucketName can not be null");
        }

        AliyunOssUtills.init(endpoint, accessKeyId, accessKeySecret, bucketName, supportCname);
        String[] upFiles = listSelectedFiles();
        if (null != upFiles && upFiles.length > 0) {
            getLog().info("files upload to aliyun oss begin");
            for (String upFile : upFiles) {
                File file = new File(getBasePath() + "/" + upFile);
                if (file.exists()) {
                    doUpload(file);
                } else {
                    warn("upload file not exists: %s", file.getName());
                }
            }
            getLog().info(upFiles.length + " files upload to aliyun oss finished");
        } else {
            getLog().warn("no files to upload");
        }
    }

    private void doUpload(File file) {
        String filePath = file.getPath().replace("\\", "/");
        String upPath = filePath.replace(getBasePath(), "").replace("src/main/webapp/", "");
        String path = this.prefix + (upPath.startsWith("/") ? upPath : "/" + upPath);
        AliyunOssUtills.putObject(path, file);
        info("%s is upload to aliyun oss, bucketName: %s, path: %s", filePath, bucketName, path);
    }

}
