package org.but4reuse.adapters.json;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.adapters.json.tools.AdapterTools;

public class ObjectElement extends AbstractElement {
	public AbstractElement parent;
	public int id;
	public List<ObjectElement> similarObjects;

	public ObjectElement(AbstractElement parent) {
		this.parent = parent;
		this.id = AdapterTools.getUniqueId();
		this.similarObjects = new ArrayList<ObjectElement>();
		this.similarObjects.add(this);
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof ObjectElement) {
			ObjectElement objectElement = (ObjectElement) anotherElement;

			if (this.id == objectElement.id)
				return 1;

			if (this.parent.similarity(objectElement.parent) == 1) {
				List<ObjectElement> similarObjects = new ArrayList<ObjectElement>();
				similarObjects.addAll(this.similarObjects);
				similarObjects.addAll(objectElement.similarObjects);

				for (ObjectElement currentObject : similarObjects) {
					currentObject.id = this.id;
					currentObject.similarObjects = similarObjects;
				}

				return 1;

			}
		}
		return 0;
	}

	@Override
	public String getText() {
		return parent.getText() + "_{}";
	}
}
