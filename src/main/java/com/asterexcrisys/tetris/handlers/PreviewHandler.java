package com.asterexcrisys.tetris.handlers;

import com.asterexcrisys.tetris.MainApplication;
import com.asterexcrisys.tetris.constants.ResourceConstants;
import javafx.scene.image.Image;
import java.util.Objects;

public class PreviewHandler {

    private final Image defaultPreview;
    private final Image spritePreview;

    public PreviewHandler() {
        defaultPreview = new Image(
                Objects.requireNonNull(MainApplication.class.getResourceAsStream(ResourceConstants.ICON_IMAGE))
        );
        spritePreview = new Image(
                Objects.requireNonNull(MainApplication.class.getResourceAsStream(ResourceConstants.TETROMINOES_IMAGE))
        );
    }

    public Image getDefaultPreview() {
        return defaultPreview;
    }

    public Image getSpritePreview() {
        return spritePreview;
    }

}