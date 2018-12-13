package net.craftingstore.core.http.util;

import com.google.gson.*;
import net.craftingstore.core.models.api.provider.ProviderInformation;
import net.craftingstore.core.models.api.provider.ProviderType;

import java.lang.reflect.Type;

public class InformationAdapter implements JsonDeserializer<ProviderInformation> {

    public ProviderInformation deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        JsonPrimitive prim = (JsonPrimitive) jsonObject.get("type");
        ProviderType providerType = ProviderType.valueOf(prim.getAsString());
        Class klass = providerType.getActualClass();
        return context.deserialize(jsonObject, klass);
    }
}
