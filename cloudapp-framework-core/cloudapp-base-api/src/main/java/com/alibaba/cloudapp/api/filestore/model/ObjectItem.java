/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.alibaba.cloudapp.api.filestore.model;

import com.alibaba.cloudapp.model.BaseModel;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * The entity information for files storage
 */
public class ObjectItem<Delegation> extends BaseModel implements Serializable {
	private static final String UTF_8 = StandardCharsets.UTF_8.toString();

	private static final long serialVersionUID = 2951336268677521004L;

	/**
	 * The eTag of the object
	 */
	private String etag;

	private Delegation delegatingObjectItem;

	/**
	 * file name
	 */
	private String objectName;

	/**
	 * The header of content-type
	 */
	private String encodingType;

	/**
	 * the size of file
	 */
	private long size;

	/**
	 * Last modified time
	 */
    private Date lastModified;

	/**
	 * The version id of the object
	 */
	private String versionId;

	/**
	 * Whether the object is the latest version
	 */
	private boolean isLatest;

	public static <Delegation> ObjectItem.Builder<Delegation> builder() {
		return new ObjectItem.Builder<>();
	}

	public static class Builder<Delegation> extends
			BaseModel.Builder<ObjectItem.Builder<Delegation>, ObjectItem<Delegation>>  {

		public ObjectItem.Builder<Delegation> delegatingMetadata(
				Delegation delegate) {
			operations.add(obj -> obj.setDelegatingObjectItem(delegate));
			return this;
		}

		public ObjectItem.Builder<Delegation> objectName(String objectName) {
			operations.add(obj -> obj.setObjectName(objectName));
			return this;
		}

		public ObjectItem.Builder<Delegation> encodingType(String encodingType) {
			operations.add(obj -> obj.setEncodingType(encodingType));
			return this;
		}

		public ObjectItem.Builder<Delegation> etag(String etag) {
			operations.add(obj -> obj.setEtag(etag));
			return this;
		}

		public ObjectItem.Builder<Delegation> size(long size) {
			operations.add(obj -> obj.setSize(size));
			return this;
		}

		public ObjectItem.Builder<Delegation> lastModified(Date lastModified) {
			operations.add(obj -> obj.setLastModified(lastModified));
			return this;
		}

		public ObjectItem.Builder<Delegation> versionId(String versionId) {
			operations.add(obj -> obj.setVersionId(versionId));
			return this;
		}

		public ObjectItem.Builder<Delegation> isLatest(boolean isLatest) {
			operations.add(obj -> obj.setLatest(isLatest));
			return this;
		}

		@Override
		protected void validate(ObjectItem<Delegation> obj) {

		}
	}

	public String getEtag() {
		return etag;
	}

	public void setEtag(String etag) {
		this.etag = etag;
	}

	public String getObjectName() {
		try {
			return "url".equals(encodingType) ?
					URLDecoder.decode(objectName, UTF_8) : objectName;
		} catch (UnsupportedEncodingException e) {
			String msg = String.format("UnsupportedEncoding objectName: %s", objectName);
			throw new RuntimeException(msg, e);
		}
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}


	public String getEncodingType() {
		return encodingType;
	}

	public void setEncodingType(String encodingType) {
		this.encodingType = encodingType;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public boolean isLatest() {
		return isLatest;
	}

	public void setLatest(boolean latest) {
		isLatest = latest;
	}

	public Delegation getDelegatingObjectItem() {
		return delegatingObjectItem;
	}

	public void setDelegatingObjectItem(Delegation delegatingObjectItem) {
		this.delegatingObjectItem = delegatingObjectItem;
	}
}
