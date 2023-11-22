package net.haraxx.coresystem.api.data.base;

import net.haraxx.coresystem.api.data.model.Column;
import net.haraxx.coresystem.api.data.model.PrimaryKey;

import java.util.HashMap;

/**
 * @author Juyas
 * @version 22.11.2023
 * @since 14.11.2023
 */
public record DataModel(String modelSchema, String modelName, PrimaryKey primaryKey, HashMap<String, Column<?>> columns) {}