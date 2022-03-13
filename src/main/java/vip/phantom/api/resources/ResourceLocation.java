/*
 * Copyright (c) 2022, for customer relationship management system by Yorck Heilmann.
 * All rights reserved.
 */

package vip.phantom.api.resources;

public class ResourceLocation {

    public final int textureId;

    public ResourceLocation(String path) {
        this.textureId = ResourceManager.INSTANCE.getResource(path);
    }
}
