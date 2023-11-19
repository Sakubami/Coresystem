package net.haraxx.coresystem.api.data.impl;

import net.haraxx.coresystem.api.data.model.ModelProperty;
import net.haraxx.coresystem.api.data.model.PrimaryKey;

import java.util.HashMap;

/**
 * @author Juyas
 * @version 14.11.2023
 * @since 14.11.2023
 */
public record DataModel(String modelName, PrimaryKey primaryKey, HashMap<String, ModelProperty<?>> columns) {}