package org.but4reuse.adapters.json;

import org.but4reuse.adapters.IElement;

public class ObjectElement extends AbstractJsonObject
{
	public AbstractJsonObject parent;
	
	public ObjectElement(AbstractJsonObject parent)
	{
		this.parent = parent;
	}
	
	@Override
	public double similarity(IElement anotherElement)
	{
		if (anotherElement instanceof ObjectElement)
		{
			ObjectElement obj = (ObjectElement) anotherElement;
			
			if( this.parent.similarity(obj.parent) == 1 )
				return 1;
		}
		return 0;
	}

	@Override
	public String getText()
	{
		return (this.parent.getText() + " -> {}");
	}
	
	@Override
	public int getMaxDependencies(String dependencyID) {
		return Integer.MAX_VALUE;
	}

	@Override
	public int getMinDependencies(String dependencyID) {
		return Integer.MIN_VALUE;
	}
}
