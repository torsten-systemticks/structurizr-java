package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.PropertyHolder;
import com.structurizr.util.StringUtils;
import com.structurizr.util.TagUtils;
import com.structurizr.util.Url;

import java.util.*;

/**
 * The base class for elements and relationships.
 */
public abstract class ModelItem implements PropertyHolder, Comparable<ModelItem> {

    private String id = "";
    private final Set<String> tags = new LinkedHashSet<>();

    private String url;
    private Map<String, String> properties = new HashMap<>();
    private final Set<Perspective> perspectives = new TreeSet<>();

    @JsonIgnore
    public abstract String getCanonicalName();

    @JsonIgnore
    public abstract Set<String> getDefaultTags();

    /**
     * Gets the ID of this item in the model.
     *
     * @return the ID, as a String
     */
    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the comma separated list of tags.
     *
     * @return  a comma separated list of tags,
     *          or an empty string if there are no tags
     */
    public String getTags() {
        return TagUtils.toString(getTagsAsSet());
    }

    @JsonIgnore
    public Set<String> getTagsAsSet() {
        Set<String> setOfTags = new LinkedHashSet<>(getDefaultTags());
        setOfTags.addAll(tags);

        return setOfTags;
    }

    void setTags(String tags) {
        this.tags.clear();

        if (tags == null) {
            return;
        }

        Collections.addAll(this.tags, tags.split(","));
    }

    public void addTags(String... tags) {
        if (tags == null) {
            return;
        }

        for (String tag : tags) {
            if (tag != null) {
                this.tags.add(tag.trim());
            }
        }
    }

    /**
     * Removes the given tag.
     *
     * @param tag       the tag to remove
     * @return          true if the tag was removed; will return false if a non-existent tag is passed, or if an attempt is
     *                  made to remove required tags, which cannot be removed.
     */
    public boolean removeTag(String tag) {
        if (tag != null) {
            return this.tags.remove(tag.trim());
        }
        return false;
    }

    /**
     * Determines whether this model item has the given tag.
     *
     * @param tag   the tag to check for
     * @return      true if tag is present as a tag on this item, or if it is one of the
     *              required tags defined by the model in getRequiredTags(), false otherwise
     */
    public boolean hasTag(String tag) {
        return getTagsAsSet().contains(tag.trim());
    }

    /**
     * Determines whether this model item has the given property with the given value.
     *
     * @param name      the name of the property
     * @param value     the value of the property
     * @return          true if the named property is present with the given value, false otherwise
     */
    public boolean hasProperty(String name, String value) {
        return getProperties().containsKey(name) && getProperties().get(name).equals(value);
    }

    /**
     * Gets the URL where more information about this item can be found.
     *
     * @return  a URL as a String
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL where more information about this item can be found.
     *
     * @param url   the URL as a String
     * @throws IllegalArgumentException     if the URL is not a well-formed URL
     */
    public void setUrl(String url) {
        if (StringUtils.isNullOrEmpty(url)) {
            this.url = null;
        } else {
            if (url.startsWith(Url.INTRA_WORKSPACE_URL_PREFIX)) {
                this.url = url;
            } else if (url.matches(Url.INTER_WORKSPACE_URL_REGEX)) {
                this.url = url;
            } else if (Url.isUrl(url)) {
                this.url = url;
            } else {
                throw new IllegalArgumentException(url + " is not a valid URL.");
            }
        }
    }

    /**
     * Gets the collection of name-value property pairs associated with this model item, as a Map.
     *
     * @return  a Map (String, String) (empty if there are no properties)
     */
    public Map<String, String> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    /**
     * Adds a name-value pair property to this model item.
     *
     * @param name      the name of the property
     * @param value     the value of the property
     */
    public void addProperty(String name, String value) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("A property name must be specified.");
        }

        if (value == null || value.trim().length() == 0) {
            throw new IllegalArgumentException("A property value must be specified.");
        }

        properties.put(name, value);
    }

    void setProperties(Map<String, String> properties) {
        if (properties != null) {
            this.properties = new HashMap<>(properties);
        }
    }

    /**
     * Gets the set of perspectives associated with this model item.
     *
     * @return  a Set of Perspective objects (empty if there are none)
     */
    public Set<Perspective> getPerspectives() {
        return new TreeSet<>(perspectives);
    }

    void setPerspectives(Set<Perspective> perspectives) {
        this.perspectives.clear();

        if (perspectives == null) {
            return;
        }

        this.perspectives.addAll(perspectives);
    }

    /**
     * Adds a perspective to this model item.
     *
     * @param name          the name of the perspective (e.g. "Security", must be unique)
     * @param description   the description of the perspective
     * @return              a Perspective object
     * @throws IllegalArgumentException     if perspective details are not specified, or the named perspective exists already
     */
    public Perspective addPerspective(String name, String description) {
        return addPerspective(name, description, "");
    }

    /**
     * Adds a perspective to this model item.
     *
     * @param name          the name of the perspective (e.g. "Technical Debt", must be unique)
     * @param description   the description of the perspective (e.g. "High")
     * @param value         the value of the perspective
     * @return              a Perspective object
     * @throws IllegalArgumentException     if perspective details are not specified, or the named perspective exists already
     */
    public Perspective addPerspective(String name, String description, String value) {
        if (StringUtils.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("A name must be specified.");
        }

        if (StringUtils.isNullOrEmpty(description)) {
            throw new IllegalArgumentException("A description must be specified.");
        }

        if (perspectives.stream().anyMatch(p -> p.getName().equals(name))) {
            throw new IllegalArgumentException("A perspective named \"" + name + "\" already exists.");
        }

        Perspective perspective = new Perspective(name, description, value);
        perspectives.add(perspective);

        return perspective;
    }

    @Override
    public int compareTo(ModelItem modelItem) {
        try {
            int id1 = Integer.parseInt(getId());
            int id2 = Integer.parseInt(modelItem.getId());

            return id1 - id2;
        } catch (NumberFormatException nfe) {
            return getId().compareTo(modelItem.getId());
        }
    }

}