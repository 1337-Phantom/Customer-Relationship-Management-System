/*
 * Copyright (c) 2022, for customer relationship management system by Yorck Heilmann.
 * All rights reserved.
 */

package vip.phantom.api.resources;

import vip.phantom.api.utils.RenderUtil;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class ResourceManager {

    protected static final ResourceManager INSTANCE = new ResourceManager();

    private HashMap<String, Integer> loadedResources = new HashMap<>();

    protected int getResource(String path) {
        if (loadedResources.containsKey(path)) {
            return loadedResources.get(path);
        } else {
            try {
                int textureId = RenderUtil.getTextureIdOf(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(path))));
                loadedResources.put(path, textureId);
                return textureId;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

}
