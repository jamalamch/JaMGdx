package ja.mgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.HashMap;
import java.util.Map;

public class JaMGDx extends ApplicationAdapter {
    public static Skin skin;
    public static Map<String,Object> intent;

    public static String getStringExtra(String midletNameKey) {
        return (String)intent.get(midletNameKey);
    }

    @Override
    public void create() {
        super.create();
        intent = new HashMap<>();
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
    }

    static public class Id{
        public static final int action_layout_edit_mode =0;
        public static final int action_layout_scale_mode = 1;
        public static final int action_layout_edit_finish = 2;
        public static final int action_layout_switch = 3;
        public static final int action_hide_buttons = 4;
    }
    public static class ActivityInfo{
        public static final int SCREEN_ORIENTATION_FULL_SENSOR = 0;
        public static final int SCREEN_ORIENTATION_SENSOR_PORTRAIT = 1;
        public static final int SCREEN_ORIENTATION_SENSOR_LANDSCAPE = 2;
    }
}
