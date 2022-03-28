package vip.phantom.system.user_interface.buttons.round_buttons;

import lombok.Getter;
import vip.phantom.system.Category;

public class CategoryButton extends RoundPictureButton {

    @Getter
    private final Category category;

    public CategoryButton(int id, int x, int y, int size, String pictureName, Category category) {
        super(id, x, y, size, pictureName);
        this.category = category;
    }


}
