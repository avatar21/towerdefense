package com.goonsquad.galactictd.systems.archetypes;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.BaseSystem;
import com.artemis.Component;
import com.badlogic.gdx.assets.AssetManager;
import com.goonsquad.galactictd.GalacticTDGame;
import com.goonsquad.galactictd.components.graphics.DrawBoxAround;
import com.goonsquad.galactictd.components.graphics.DrawInUi;
import com.goonsquad.galactictd.components.graphics.Renderable;
import com.goonsquad.galactictd.components.graphics.Text;
import com.goonsquad.galactictd.components.input.Touchable;
import com.goonsquad.galactictd.components.layers.Layer;
import com.goonsquad.galactictd.components.positional.Rotatable;
import com.goonsquad.galactictd.components.positional.RotationSpeed;
import com.goonsquad.galactictd.components.positional.Spatial;

import java.util.HashMap;
import java.util.Map;

public abstract class ArchetypeBuilderSystem extends BaseSystem {
    private Map<String, Archetype> createdArchetypes;
    protected GalacticTDGame gameInstance;
    public static final String invisibleButton = "invisible_button";
    public static final String sprite = "sprite";
    public static final String uiLabel = "ui_label";
    public static final String uiButton = "ui_button";
    public static final String textLabel = "text_label";

    public ArchetypeBuilderSystem(GalacticTDGame gameInstance) {
        createdArchetypes = new HashMap<String, Archetype>();
        createdArchetypes.put(null, null);
        this.setEnabled(false);
        this.gameInstance = gameInstance;
    }

    @Override
    protected final void initialize() {
        createDefaultArchetypes();
        createCustomArchetypes();
    }

    //Archetypes that are used in all worlds.
    private void createDefaultArchetypes() {
        this.addArchetypeToSystem(invisibleButton, Spatial.class, Rotatable.class, RotationSpeed.class, Layer.class, Touchable.class, DrawInUi.class);

        this.addArchetypeToSystem(sprite, Spatial.class, Rotatable.class, RotationSpeed.class, Renderable.class, DrawBoxAround.class, Layer.class);

        this.addArchetypeToSystem(uiLabel, sprite, DrawInUi.class);
        this.addArchetypeToSystem(uiButton, uiLabel, Touchable.class);

        this.addArchetypeToSystem(textLabel, Spatial.class, Text.class, Layer.class, DrawInUi.class);
    }

    //Overwrite to create archetypes specific to each world.
    protected abstract void createCustomArchetypes();


    //Creates an entity of the given archetype.
    //New entity will have all the components of the archetype.
    //Exception is thrown if the passed in archetype is not found.
    //
    public int buildArchetype(String archetypeKey) {
        if (createdArchetypes.containsKey(archetypeKey)) {
            return world.create(createdArchetypes.get(archetypeKey));
        } else {
            throw new RuntimeException("Key" + archetypeKey + " not found");
        }
    }

    //  ** Passed in components must have an empty constructor **
    //Adds an archetype to the pool of previously made archetypes,
    //archetype can then be accessed by the key name passed in,
    public void addArchetypeToSystem(String archetypeKey, Class<? extends Component>... components) {
        addArchetypeToSystem(archetypeKey, null, components);
    }

    //  ** Passed in components must have an empty constructor **
    //If parent archetype is found, the new archetype will inherit all the components of the parent.
    //Attempting to create a new archetype that is already set will result in an exception.
    public void addArchetypeToSystem(String newArcheTypeName, String parentArchetypeName, Class<? extends Component>... components) {
        if (!createdArchetypes.containsKey(newArcheTypeName)) {
            Archetype parent = createdArchetypes.get(parentArchetypeName);
            Archetype newArcheType = new ArchetypeBuilder(parent).add(components).build(world);
            createdArchetypes.put(newArcheTypeName, newArcheType);
        } else {
            throw new RuntimeException("Archetype with name " + newArcheTypeName + "already created.");
        }
    }

    @Override
    protected void processSystem() {
    }
}
